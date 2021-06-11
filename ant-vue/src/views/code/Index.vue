<template>
  <div>
    <a-spin :spinning="spinning">
      <page-header-wrapper :title="false">
        <a-card :bordered="false">
          <div class="table-page-search-wrapper">
            <a-form layout="inline">
              <a-row :gutter="48">
                <a-col :md="8" :sm="24">
                  <a-form-item label="项目主包名">
                    <a-input
                      v-model="queryParam.rootPackage"
                      placeholder="请输入项目主包名。如：com.chqiuu"
                    />
                  </a-form-item>
                </a-col>
                <a-col :md="8" :sm="24">
                  <a-form-item label="创建人">
                    <a-input
                      placeholder="请输入创建人。如：chqiuu"
                      v-model="queryParam.author"
                    />
                  </a-form-item>
                </a-col>
                <a-col :md="8" :sm="24">
                  <a-form-item label="MyBatis-Plus">
                    <a-checkbox v-model="queryParam.isPlus">
                      是否生成MyBatis-Plus模式代码
                    </a-checkbox>
                  </a-form-item>
                </a-col>

                <a-col :md="24" :sm="48">
                  <a-form-item label="选择生成方法">
                    <a-checkbox
                      :indeterminate="indeterminate"
                      :checked="checkGenMethodAll"
                      @change="onCheckGenMethodAllChange"
                    >
                      全选
                    </a-checkbox>
                    <a-checkbox-group
                      v-model="queryParam.genMethods"
                      :options="genMethodOptions"
                      @change="onGenMethodChange"
                    />
                  </a-form-item>
                </a-col>

                <a-col :md="8" :sm="48">
                  <a-form-item>
                    <a-tooltip
                      placement="bottom"
                      title="为选中的表设置模块，在生成代码之前必须设置"
                      arrow-point-at-center
                    >
                      <a-button @click="handleBatchSetModule">
                        批量设置模块
                      </a-button>
                    </a-tooltip>
                    <a-tooltip
                      placement="bottom"
                      title="给选中的表生成代码，文件格式ZIP"
                      arrow-point-at-center
                    >
                      <a-button
                        style="margin-left: 8px"
                        type="primary"
                        @click="handleGenerateSubmit"
                      >
                        生成代码
                      </a-button>
                    </a-tooltip>
                  </a-form-item>
                </a-col>
                <a-col :md="16" :sm="48" :hidden="hiddenSwitchDatabaseSelect">
                  <a-form-item label="切换数据库">
                    <a-select
                      :default-value="this.$route.params.dbName"
                      style="width: 130px"
                      @change="handleSwitchDatabaseChange"
                    >
                      <a-select-option
                        v-for="database in databases"
                        :key="database.schemaName"
                      >
                        {{ database.schemaName }}
                      </a-select-option>
                    </a-select>
                    【当前链接：{{ this.$route.params.dbType }} {{ this.$route.params.dbServer }}:{{ this.$route.params.dbPort }} {{ this.$route.params.dbName }}】
                  </a-form-item>
                </a-col>
              </a-row>
            </a-form>
          </div>
          <a-alert type="success" show-icon>
            <template slot="message">
              <span
                style="margin-right: 12px"
              >已选择:
                <a style="font-weight: 600">
                  {{ this.selectedRowKeys.length }}
                </a>
                张表</span>
            </template>
          </a-alert>
          <a-table
            bordered
            ref="tables-table"
            size="small"
            :row-key="(record, index) => index"
            :row-selection="{
              selectedRowKeys: selectedRowKeys,
              onChange: onSelectChangeTable
            }"
            :pagination="false"
            :columns="columns"
            :data-source="tables"
            :alert="true"
          >
            <span slot="columns" slot-scope="text">
              {{ text.length }}
            </span>
            <span slot="module" slot-scope="text">
              <a style="font-weight: 600">
                {{
                  text === null ||
                    text === "" ||
                    text === undefined ||
                    text.length === 0
                    ? "-"
                    : text
                }}
              </a>
            </span>
            <span slot="mappingName" slot-scope="text, record">
              <editable-cell
                :value="text"
                @change="
                  onMappingNameCellChange(
                    record.tableName,
                    'mappingName',
                    $event
                  )
                "
              />
            </span>
            <span slot="action" slot-scope="text, record">
              <template>
                <a-button
                  @click="handlePreviewTable(record)"
                >查看字段信息</a-button>
                <a-divider type="vertical" />
                <a-button
                  type="primary"
                  @click="handlePreviewCode(record)"
                >预览代码</a-button>
              </template>
            </span>
          </a-table>
        </a-card>
      </page-header-wrapper>
    </a-spin>
    <a-modal
      title="设置模块名"
      v-model="modalSetModuleVisible"
      :after-close="setModalSetModuleAfterClose"
      @ok="handleSetModuleOk"
    >
      <a-form>
        <a-form-item label="模块名">
          <a-input
            id="moduleNameInput"
            v-model="moduleName"
            placeholder="请输入模块名"
            v-decorator="[
              'module',
              { rules: [{ required: true, min: 1, message: '请输入模块名' }] }
            ]"
            @keyup.enter="handleSetModuleOk"
          />
        </a-form-item>
      </a-form>
    </a-modal>
    <a-modal
      :after-close="setModalPreviewTableAfterClose"
      :width="1000"
      v-model="modalPreviewTableVisible"
      :title="previewTableName"
      :footer="null"
    >
      <a-table
        ref="columns-table"
        size="small"
        :row-key="(record, index) => index"
        :columns="innerTableColumns"
        :data-source="innerTableColumnData"
        :alert="true"
        :pagination="false"
      />
    </a-modal>
    <a-modal
      :after-close="setModalPreviewCodeAfterClose"
      v-model="modalPreviewCodeVisible"
      :title="previewTableName"
      width="95%"
      :dialog-style="{ top: '20px' }"
      style="min-height: 700px;height: 100%"
    >
      <a-tabs
        type="card"
        v-model="codePreviewActiveName"
        @change="handleCodePreviewTabsChange"
      >
        <a-tab-pane
          v-for="(item, index) in previewCodeList"
          :key="item.fileName"
          :item="item"
          :index="index"
          :tab="item.fileName"
        />
      </a-tabs>
      <div
        id="code-preview"
        style="min-height: 700px;height: 100%;width: 100%;"
      />
    </a-modal>
  </div>
</template>

<script>
import API from '@/api/index'
import * as monaco from 'monaco-editor'

const columns = [
  {
    title: '表名',
    width: '10%',
    dataIndex: 'tableName',
    scopedSlots: { customRender: 'tableName' },
    ellipsis: true,
  },
  {
    title: '字符集',
    width: '150px',
    dataIndex: 'tableCollation',
    scopedSlots: { customRender: 'tableCollation' },
    ellipsis: true,
  },
  {
    title: '字段数',
    dataIndex: 'columns',
    width: '70px',
    sorter: true,
    needTotal: true,
    scopedSlots: { customRender: 'columns' },
  },
  {
    title: '表注释',
    dataIndex: 'tableComment',
    width: '30%',
    ellipsis: true,
    scopedSlots: { customRender: 'tableComment' },
  },
  {
    title: '所属模块',
    width: '80px',
    dataIndex: 'module',
    scopedSlots: { customRender: 'module' },
  },
  {
    title: 'URL映射名称',
    width: '170px',
    dataIndex: 'mappingName',
    scopedSlots: { customRender: 'mappingName' },
  },
  {
    title: '操作',
    dataIndex: 'action',
    width: '210px',
    scopedSlots: { customRender: 'action' },
  },
]

const innerTableColumns = [
  {
    title: '字段名',
    width: '200px',
    dataIndex: 'columnName',
    scopedSlots: { customRender: 'columnName' },
    ellipsis: true,
  },
  {
    title: '字段类型',
    width: '200px',
    dataIndex: 'columnType',
    ellipsis: true,
    scopedSlots: { customRender: 'columnType' },
  },
  {
    title: '字段注释',
    width: '500px',
    dataIndex: 'columnComment',
    ellipsis: true,
    scopedSlots: { customRender: 'columnComment' },
  },
]

const EditableCell = {
  template: `
    <div class="editable-cell">
    <div v-if="editable" class="editable-cell-input-wrapper">
      <a-input :value="value" v-focus="editable" @change="handleChange" @pressEnter="check" @blur="check"/>
    </div>
    <div v-else class="editable-cell-text-wrapper">
      {{ value || '-' }}
      <a v-if=" value !== null && value !== '' && value !== undefined && value.length > 0"
         style="font-weight: 600"
         class="editable-cell-icon" @click="edit"
      >修改</a>
    </div>
    </div>
  `,
  props: {
    value: String,
  },
  data () {
    return {
      editable: false,
    }
  },
  directives: {
    // 获取焦点
    focus: {
      inserted: function (el) {
        el.focus()
      },
    },
  },
  created: function () {},
  methods: {
    handleChange (e) {
      this.$emit('change', e.target.value)
    },
    check () {
      this.editable = false
      this.$emit('change', this.value)
    },
    edit () {
      this.editable = true
    },
  },
}
// 需要生成方法的选型
const genMethodOptions = [
  'add',
  'update',
  'insertIgnore',
  'replace',
  'getDetailById',
  'getList',
  'getPage',
]

// 默认选中项
const defaultGenMethodCheckedList = [
  'add',
  'update',
  'getDetailById',
  'getList',
  'getPage',
]

export default {
  name: 'GeneratorPage',
  data: function () {
    return {
      generateTermForm: this.$form.createForm(this, {
        name: 'generate-term-form',
      }),
      spinning: false,
      indeterminate: true,
      // 是否全选
      checkGenMethodAll: false,
      genMethodOptions,
      // 数据库表显示字段定义
      columns: columns,
      // 数据库表显示数据
      tables: [],
      // 模块名称
      moduleName: null,
      // 是否显示切换数据库下拉框
      hiddenSwitchDatabaseSelect: true,
      // 数据库列表
      databases: [],
      // 当前预览的表名
      previewTableName: null,
      // 批量设置表所属模块弹出框是否显示
      modalSetModuleVisible: false,
      // 预览表中字段弹出框是否显示
      modalPreviewTableVisible: false,
      // 表中字段显示列定义
      innerTableColumns: innerTableColumns,
      // 表中字段数据
      innerTableColumnData: [],
      // 预览表生成后的代码弹出框是否显示
      modalPreviewCodeVisible: false,
      // 预览代码列表
      previewCodeList: [],
      codePreviewActiveName: null,
      monacoEditor: null,
      // 查询参数
      queryParam: {
        rootPackage: 'com.chqiuu',
        author: 'chqiuu',
        isPlus: true,
        genMethods: defaultGenMethodCheckedList,
      },
      selectedRowKeys: [],
    }
  },
  components: {
    EditableCell,
  },
  created () {
    // 加载完成后执行
    this.fetchTableStructure()
    this.initSwitchDatabaseSelect()
  },
  mounted: function () {
    // 实例被挂载后调
    this.$nextTick(function () {})
  },
  methods: {
    async initSwitchDatabaseSelect () {
      const connectType = this.$route.params.connectType
      if (connectType === 'db') {
        this.hiddenSwitchDatabaseSelect = false
        this.databases = await API.getAllDatabaseList()
      }
    },
    async fetchTableStructure () {
      this.spinning = true
      try {
        this.tables = await API.getAllTableList()
      } catch (error) {
        this.$notification.error({
          message: '错误',
          description: '获取数据库表失败：【'
            .concat(error)
            .concat('】，即将跳转到到数据源设置页面'),
          duration: 3,
        })
        setTimeout(() => {
          // 跳转到数据源设置页面
          this.$router.push('/connect/index')
        }, 3000)
      } finally {
        this.spinning = false
      }
    },
    onSelectChangeTable (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    setModalSetModuleAfterClose () {
      this.modalSetModuleVisible = false
    },
    onGenMethodChange (checkedGenMethodList) {
      this.indeterminate =
        !!checkedGenMethodList.length &&
        checkedGenMethodList.length < genMethodOptions.length
      this.checkGenMethodAll =
        checkedGenMethodList.length === genMethodOptions.length
    },
    onCheckGenMethodAllChange (e) {
      this.queryParam.genMethods = e.target.checked
        ? this.genMethodOptions
        : []
      this.indeterminate = false
      this.checkGenMethodAll = e.target.checked
    },
    async handleSwitchDatabaseChange (value) {
      // 切换数据库
      try {
        const result = await API.connectDatabase({
          dbType: this.$route.params.dbType,
          server: this.$route.params.dbServer,
          port: this.$route.params.dbPort,
          database: value,
          username: this.$route.params.dbUser,
          password: this.$route.params.dbPass,
        })
        // 加载表列表
        this.fetchTableStructure()
        this.$notification.success({
          message: '成功',
          description: '切换数据库成功',
          duration: 3,
        })
      } catch (error) {
        this.$notification.error({
          message: '错误',
          description: error,
          duration: 3,
        })
      }
    },
    handleSetModuleOk (e) {
      // 批量设置模块名
      console.info('批量设置模块名')
      this.modalSetModuleVisible = false
      this.selectedRowKeys.forEach(key => {
        this.tables[key].module = this.moduleName
        if (this.tables[key].mappingName === undefined) {
          this.tables[key].mappingName = this.getMappingName(
            this.tables[key].tableName,
            this.moduleName,
          )
        }
      })
    },
    getMappingName (tableName, moduleName) {
      // Controller中URL映射名称，如：/admin/user。用于 Controller中@RequestMapping注解
      return '/'
        .concat(moduleName)
        .concat('/')
        .concat(this.getLastWord(tableName))
    },
    getLastWord (tableName) {
      // 获取字符串中最后一个单词，若最好单词为info则忽略，向前移一个单词
      tableName = tableName.replaceAll('_info', '')
      if (tableName.search('_') !== -1) {
        return tableName.substring(tableName.lastIndexOf('_') + 1)
      }
      return tableName
    },
    onMappingNameCellChange (tableName, dataIndex, value) {
      // 修改Controller中URL映射名称
      const tables = [...this.tables]
      const target = tables.find(item => item.tableName === tableName)
      if (target) {
        target[dataIndex] = value
        this.tables = tables
      }
    },
    handlePreviewTable (record) {
      // 查看字段信息
      this.previewTableName = record.tableName + ' 表字段信息'
      this.innerTableColumnData = record.columns
      this.modalPreviewTableVisible = true
    },
    setModalPreviewTableAfterClose () {
      // 关闭预览字段窗口
      this.modalPreviewTableVisible = false
      this.previewTableName = null
      this.innerTableColumnData = []
    },
    async handlePreviewCode (row) {
      // 预览代码
      if (row.module == null) {
        // 选中的表没有设置模块名
        this.$notification.error({
          message: '错误',
          description: '请给该表指定功能模块名',
          duration: 3,
        })
        // 选中该行
        for (let i = 0; i < this.tables.length; i++) {
          if (row.tableName === this.tables[i].tableName) {
            this.selectedRowKeys.push(i)
            break
          }
        }
        // 弹出设置模块名弹窗
        this.handleBatchSetModule()
        return
      }

      try {
        this.monacoEditor && this.monacoEditor.dispose()
        this.previewTableName = row.tableName + '表生成代码预览'
        const result = await API.preview({
          rootPackage: this.queryParam.rootPackage,
          moduleName: row.module,
          author: this.queryParam.author,
          table: row.tableName,
          mappingName: row.mappingName,
          isPlus: this.queryParam.isPlus,
          genMethods: this.queryParam.genMethods,
        })
        this.$notification.success({
          message: '成功',
          description: '代码获取成功',
          duration: 3,
        })
        this.previewCodeList = result
        // 显示弹出框
        this.modalPreviewCodeVisible = true
        // 延时加载后面的内容，不然导致编辑器初始化失败
        setTimeout(() => {
          // 初始化编辑器，确保dom已经渲染
          this.initMonacoEditor()
        }, 100)
        setTimeout(() => {
          this.codePreviewActiveName = this.previewCodeList[0].fileName
          // 更改editor内容
          this.monacoEditor.setValue(this.previewCodeList[0].content)
          // 触发：格式化文档，更多支持项：editor._actions
          //  this.monacoEditor.trigger('a', 'editor.action.formatDocument')
        }, 100)
      } catch (error) {
        this.$notification.error({
          message: '错误',
          description: error,
          duration: 3,
        })
      }
    },
    handleCodePreviewTabsChange (tab) {
      // 预览代码文件选择
      this.previewCodeList.forEach(row => {
        if (tab === row.fileName) {
          // 更改editor内容
          this.monacoEditor.setValue(row.content)
        }
      })
    },
    getLanguage (name) {
      var language = 'java'
      if (name.indexOf('.sql') !== -1) {
        language = 'mysql'
      } else if (name.indexOf('.xml') !== -1) {
        language = 'xml'
      } else if (name.indexOf('.vue') !== -1) {
        language = 'typescript'
      }
      return language
    },
    setModalPreviewCodeAfterClose () {
      // 关闭预览代码窗口
      this.modalPreviewCodeVisible = false
      this.previewTableName = null
      this.previewCodeList = []
    },
    handleBatchSetModule (e) {
      if (this.selectedRowKeys.length === 0) {
        this.$notification.warning({
          message: '提示',
          description: '请先选择表',
          duration: 3,
        })
        return
      }
      // 批量设置模块
      this.modalSetModuleVisible = true
      setTimeout(() => {
        document.getElementById('moduleNameInput').focus()
      }, 10)
    },
    async handleGenerateSubmit (e) {
      // 开始生成代码
      if (this.selectedRowKeys.length === 0) {
        this.$notification.info({
          message: '提示',
          description: '请先选择表',
          duration: 3,
        })
        return
      }

      // 当前选中的表列表
      const selectedTables = []
      this.selectedRowKeys.forEach(key => {
        selectedTables.push(this.tables[key])
      })

      const mappingNames = []
      for (const key of this.selectedRowKeys) {
        if (this.tables[key].module == null) {
          this.$notification.error({
            message: '错误',
            description: '请给需要生成都表设置模块名',
            duration: 3,
          })
          // 跳转到数据源设置页面
          this.modalSetModuleVisible = true
          setTimeout(() => {
            document.getElementById('moduleNameInput').focus()
          }, 10)
          return
        }
        mappingNames.push(this.tables[key].mappingName)
      }

      // 检查URL映射名称是否有重复
      const sortMappingNames = mappingNames.slice().sort()
      let errorMessage = ''
      for (let i = 0; i < mappingNames.length; i++) {
        if (sortMappingNames[i] === sortMappingNames[i + 1]) {
          errorMessage = errorMessage.concat(sortMappingNames[i]).concat('，')
        }
      }
      console.info('errorMessage', errorMessage)
      if (errorMessage !== '') {
        this.$notification.error({
          message: '错误',
          description: 'URL映射名称不能重复，存在以下重复项：'
            .concat(errorMessage)
            .concat('请修改后再生成代码'),
          duration: 10,
        })
        return
      }

      try {
        this.queryParam.tables = selectedTables
        await API.generatorCodes(this.queryParam)
        this.$notification.success({
          message: '成功',
          description: '代码下载成功',
          duration: 3,
        })
      } catch (error) {
        this.$notification.error({
          message: '错误',
          description: '代码下载失败' + error,
          duration: 3,
        })
      } finally {
        this.spinning = false
      }
    },
    initMonacoEditor () {
      this.monacoEditor = monaco.editor.create(
        document.getElementById('code-preview'),
        {
          value: '', // 编辑器初始显示文字
          language: 'java', // 语言支持自行查阅demo
          automaticLayout: true, // 自动布局
          theme: 'vs-dark', // 官方自带三种主题vs, hc-black, or vs-dark
          minimap: {
            // 不要小地图
            enabled: false,
          },
        },
      )
    },
  },
}
</script>

<style lang="less" scoped>
.editable-cell {
  position: relative;
}

.editable-cell-input-wrapper,
.editable-cell-text-wrapper {
  padding-right: 24px;
}

.editable-cell-input-wrapper > input {
  width: 80%;
}

.editable-cell-text-wrapper {
  padding: 5px 24px 5px 5px;
}

.editable-cell-icon,
.editable-cell-icon-check {
  position: absolute;
  right: 0;
  width: 20px;
  cursor: pointer;
}

.editable-cell-icon {
  line-height: 18px;
  display: none;
}

.editable-cell-icon-check {
  line-height: 28px;
}

.editable-cell:hover .editable-cell-icon {
  display: inline-block;
}

.editable-cell-icon:hover,
.editable-cell-icon-check:hover {
  color: #108ee9;
}

.editable-add-btn {
  margin-bottom: 8px;
}
</style>
