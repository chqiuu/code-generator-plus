'use strict'
import { app, protocol, BrowserWindow, Menu, globalShortcut } from 'electron'
import { createProtocol } from 'vue-cli-plugin-electron-builder/lib'
import installExtension, { VUEJS_DEVTOOLS } from 'electron-devtools-installer'
const isDevelopment = process.env.NODE_ENV !== 'production'
const log = require('electron-log')

// Scheme must be registered before the app is ready
protocol.registerSchemesAsPrivileged([
  { scheme: 'app', privileges: { secure: true, standard: true } },
])

// 定义spawn命令
const spawn = require('child_process').spawn
// 定义exec命令
const exec = require('child_process').exec

const platform = process.platform
// 后台服务启动状态
let appStarted = false
let serverProcess

if (isDevelopment) {
  serverProcess = true
} else {
  if (platform === 'win32') {
    // serverProcess = require('child_process').spawn(
    //   app.getAppPath() + '/server/start.bat',
    //   null,
    //   { cwd: app.getAppPath() + '/server' },
    // )
    serverProcess = require('child_process').spawn(
      app.getAppPath() + '/server/jre/bin/java.exe',
      ['-jar', 'code-generator-plus.jar'],
      { cwd: app.getAppPath() + '/server' },
    )
    log.info('获取 serverProcess=' + serverProcess)
    log.info('获取 serverProcess.pid=' + serverProcess.pid)
  } else {
    const chmod = require('child_process').spawn('chmod', [
      '+x',
      app.getAppPath() + '/server/start',
    ])
    chmod.on('close', code => {
      const chmod2 = require('child_process').spawn('chmod', [
        '+x',
        app.getAppPath() + '/server/jre/bin/java',
      ])
      chmod2.on('close', () => {
        serverProcess = require('child_process').spawn(
          app.getAppPath() + '/server/start',
        )
      })
    })
  }
}

// 热加载
try {
  require('electron-reloader')(module, {})
} catch (_) {}

async function createWindow () {
  // 创建浏览器窗口
  const win = new BrowserWindow({
    width: 1024,
    height: 768,
    webPreferences: {
      // Use pluginOptions.nodeIntegration, leave this alone
      // See nklayman.github.io/vue-cli-plugin-electron-builder/guide/security.html#node-integration for more info
      nodeIntegration: process.env.ELECTRON_NODE_INTEGRATION,
    },
  })

  win.webContents.on(
    'new-window',
    (event, url, frameName, disposition, options, additionalFeatures) => {
      // open window as modal
      event.preventDefault()
      Object.assign(options, {
        parent: win,
        center: true,
      })
      event.newGuest = new BrowserWindow(options)
      event.newGuest.loadURL(url)
    },
  )

  globalShortcut.register('CommandOrControl+Shift+i', function () {
    win.webContents.toggleDevTools()
  })

  if (process.env.WEBPACK_DEV_SERVER_URL) {
    // Load the url of the dev server if in development mode
    await win.loadURL(process.env.WEBPACK_DEV_SERVER_URL)
    if (!process.env.IS_TEST) win.webContents.openDevTools()
  } else {
    createProtocol('app')
    // Load the index.html when not in development
    win.loadURL('app://./index.html')
  }
}

// Quit when all windows are closed.
app.on('window-all-closed', e => {
  if (serverProcess && process.platform !== 'darwin') {
    e.preventDefault()
    const kill = require('tree-kill')
    kill(serverProcess.pid, 'SIGTERM', function () {
      console.log('Server process killed')
      serverProcess = null
      app.quit()
    })
  } else {
    // On macOS it is common for applications and their menu bar
    // to stay active until the user quits explicitly with Cmd + Q
    if (process.platform !== 'darwin') {
      app.quit()
    }
  }
})

app.on('activate', () => {
  // On macOS it's common to re-create a window in the app when the
  // dock icon is clicked and there are no other windows open.
  if (BrowserWindow.getAllWindows().length === 0 && appStarted) createWindow()
})

const appUrl = 'http://localhost:803/generator/health'

const startUp = function () {
  log.log('startUp start')
  const requestPromise = require('minimal-request-promise')
  requestPromise.get(appUrl).then(
    function (response) {
      log.log('startUp http success')
      createWindow()
      appStarted = true
    },
    function (response) {
      setTimeout(function () {
        startUp()
        log.log('startUp sleep 500')
      }, 500)
    },
  )
}

// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.on('ready', async () => {
  if (isDevelopment && !process.env.IS_TEST) {
    // Install Vue Devtools
    try {
      await installExtension(VUEJS_DEVTOOLS)
    } catch (e) {
      console.error('Vue Devtools failed to install:', e.toString())
    }
  }
  startUp()
})

// Exit cleanly on request from parent process in development mode.
if (isDevelopment) {
  if (process.platform === 'win32') {
    process.on('message', data => {
      if (data === 'graceful-exit') {
        app.quit()
      }
    })
  } else {
    process.on('SIGTERM', () => {
      app.quit()
    })
  }
}
