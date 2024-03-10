package com.chqiuu.cgp.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 浏览器操作工具类
 *
 * @author chqiu
 */
public class BrowseUtil {

    public static void openBrowseByUrl(String url) throws IOException {
        if (openEdgeBrowseByUrl(url)) {
            return;
        }
        if (openChromeBrowseByUrl(url)) {
            return;
        }
        if (openFirefoxBrowseByUrl(url)) {
            return;
        }
        //启用cmd运行默认浏览器
        Runtime.getRuntime().exec("cmd /c start " + url);
    }

    public static boolean openEdgeBrowseByUrl(String url) throws IOException {
        String path;
        String path64 = "C:\\Program Files\\Microsoft\\Edge\\Application\\msedge.exe";
        String path86 = "C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe";
        if (new File(path64).exists()) {
            path = path64;
        } else if (new File(path86).exists()) {
            path = path86;
        } else {
            return false;
        }
        ProcessBuilder process = new ProcessBuilder(path, url);
        process.start();
        return true;
    }

    public static boolean openChromeBrowseByUrl(String url) throws IOException {
        String path;
        String path64 = "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe";
        String path86 = "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe";
        if (new File(path64).exists()) {
            path = path64;
        } else if (new File(path86).exists()) {
            path = path86;
        } else {
            return false;
        }
        ProcessBuilder process = new ProcessBuilder(path, url);
        process.start();
        return true;
    }

    public static boolean openFirefoxBrowseByUrl(String url) throws IOException {
        String path;
        String path64 = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
        String path86 = "C:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe";
        if (new File(path64).exists()) {
            path = path64;
        } else if (new File(path86).exists()) {
            path = path86;
        } else {
            return false;
        }
        ProcessBuilder process = new ProcessBuilder(path, url);
        process.start();
        return true;
    }

    private static void openDefaultBrowseByUrl(String url) throws Exception {
        // 获取操作系统的名字
        String osName = System.getProperty("os.name", "");
        if (osName.startsWith("Windows")) {
            // windows
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } else if (osName.startsWith("Mac OS")) {
            // Mac
            Class fileMgr = Class.forName("com.apple.eio.FileManager");
            Method openURL = fileMgr.getDeclaredMethod("openURL", String.class);
            openURL.invoke(null, url);
        } else {
            // Unix or Linux
            String[] browsers = {"firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape"};
            String browser = null;
            // 执行代码，在brower有值后跳出，
            for (int count = 0; count < browsers.length && browser == null; count++) {
                // 这里是如果进程创建成功了，==0是表示正常结束。
                if (Runtime.getRuntime().exec(new String[]{"which", browsers[count]}).waitFor() == 0) {
                    browser = browsers[count];
                }
            }
            if (browser == null) {
                throw new RuntimeException("未找到任何可用的浏览器");
            } else {
                // 这个值在上面已经成功的得到了一个进程。
                Runtime.getRuntime().exec(new String[]{browser, url});
            }
        }
    }
}
