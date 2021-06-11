<template>
  <div>
    <a-spin :spinning="spinning">
      <page-header-wrapper
        :title="false"
        content="本工具提供了两种数据库表导入方式，连接数据库和导入SQL创建表脚本"
      >
        <a-card :body-style="{ padding: '24px 32px' }" :bordered="false">
          <a-tabs default-active-key="1" type="card">
            <a-tab-pane key="1" tab="连接数据库">
              <a-form
                :form="connectDatabaseForm"
                :label-col="{ span: 5 }"
                :wrapper-col="{ span: 12 }"
                @submit="handleConnectDatabaseSubmit"
              >
                <a-form-item label="数据库类型">
                  <a-radio-group
                    @change="onChangeDbType"
                    button-style="solid"
                    v-decorator="[
                      'dbType',
                      {
                        initialValue: 'MySQL',
                        rules: [
                          { required: true, message: '请选择数据库类型！' }
                        ]
                      }
                    ]"
                  >
                    <a-radio-button
                      v-for="(item, index) in dbTypes"
                      :key="index"
                      :value="item.type"
                      v-show="item.isShow"
                    >
                      {{ item.type }}
                    </a-radio-button>
                  </a-radio-group>
                </a-form-item>
                <a-form-item label="服务器地址">
                  <a-input
                    placeholder="请输入服务器地址！"
                    v-decorator="[
                      'server',
                      {
                        initialValue: dbServer,
                        rules: [
                          { required: true, message: '请输入服务器地址！' }
                        ]
                      }
                    ]"
                  />
                </a-form-item>
                <a-form-item label="端口号">
                  <a-input-number
                    placeholder="请输入端口号！"
                    :min="1"
                    :max="65535"
                    v-decorator="[
                      'port',
                      {
                        initialValue: dbPort,
                        rules: [{ required: true, message: '请输入端口号！' }]
                      }
                    ]"
                  />
                </a-form-item>
                <a-form-item label="数据库名">
                  <a-input
                    placeholder="请输入需要连接的数据库名！"
                    v-decorator="[
                      'database',
                      {
                        initialValue: dbName,
                        rules: [{ required: true, message: '请输入数据库名！' }]
                      }
                    ]"
                  />
                </a-form-item>
                <a-form-item label="登录名">
                  <a-input
                    placeholder="请输入数据库登录名！"
                    v-decorator="[
                      'username',
                      {
                        initialValue: dbUser,
                        rules: [{ required: true, message: '请输入登录名！' }]
                      }
                    ]"
                  />
                </a-form-item>
                <a-form-item label="登录密码">
                  <a-input-password
                    placeholder="请输入数据库登录密码！"
                    v-decorator="[
                      'password',
                      {
                        initialValue: dbPass,
                        rules: [{ required: true, message: '请输入登录密码！' }]
                      }
                    ]"
                  />
                </a-form-item>
                <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
                  <a-button type="primary" html-type="submit">
                    开始连接
                  </a-button>
                  <a-button
                    :style="{ marginLeft: '8px' }"
                    @click="handleConnectDatabaseReset"
                  >
                    重置
                  </a-button>
                </a-form-item>
              </a-form>
            </a-tab-pane>
            <a-tab-pane key="2" tab="导入SQL脚本" force-render>
              <a-form
                :form="importSqlForm"
                :label-col="{ span: 2 }"
                :wrapper-col="{ span: 15 }"
                @submit="handleImportSqlSubmit"
              >
                <a-form-item label="数据库类型">
                  <a-radio-group
                    button-style="solid"
                    v-decorator="[
                      'dbType',
                      {
                        initialValue: 'MySQL',
                        rules: [
                          { required: true, message: '请选择数据库类型！' }
                        ]
                      }
                    ]"
                  >
                    <a-radio-button
                      v-for="(item, index) in dbTypes"
                      :key="index"
                      :value="item.type"
                    >
                      {{ item.type }}
                    </a-radio-button>
                  </a-radio-group>
                </a-form-item>
                <a-form-item label="SQL脚本">
                  <a-textarea
                    :rows="40"
                    placeholder="请输入SQL CREATE脚本，可以直接多表，建议使用Navicat工具生成的DDL脚本"
                    v-decorator="[
                      'ddl',
                      {
                        rules: [{ required: true, message: '请输入SQL脚本！' }]
                      }
                    ]"
                  />
                </a-form-item>
                <a-form-item :wrapper-col="{ span: 12, offset: 5 }">
                  <a-button type="primary" html-type="submit">
                    提交
                  </a-button>
                  <a-button
                    :style="{ marginLeft: '8px' }"
                    @click="handleImportSqlReset"
                  >
                    重置
                  </a-button>
                </a-form-item>
              </a-form>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </page-header-wrapper>
    </a-spin>
  </div>
</template>

<script>
import API from '@/api/index'
export default {
  name: 'ConnectPage',
  data: function () {
    return {
      connectDatabaseForm: this.$form.createForm(this, {
        name: 'connect-database-form',
      }),
      importSqlForm: this.$form.createForm(this, { name: 'import-sql-form' }),
      spinning: false,
      dbType: 'MySQL',
      dbServer: '127.0.0.1',
      dbPort: 3306,
      dbName: 'mysql',
      dbUser: 'root',
      dbPass: 'root',
      dbTypes: [
        {
          type: 'MySQL',
          port: 3306,
          isShow: true,
        },
        {
          type: 'SQLServer',
          port: 1433,
          isShow: true,
        },
        {
          type: 'Oracle',
          port: 1521,
          isShow: true,
        },
        {
          type: 'SQLite',
          port: '',
          isShow: false,
        },
        {
          type: 'H2',
          port: '',
          isShow: false,
        },
      ],
    }
  },
  components: {},
  mounted: function () {
    // 实例被挂载后调
    this.$nextTick(function () {
      const isDev = process.env.NODE_ENV === 'development'
      if (isDev) {
        // 测试环境默认数据库连接
        this.dbServer = '127.0.0.1'
        this.dbPort = 3306
        this.dbName = 'spider'
        this.dbUser = 'root'
        this.dbPass = 'root'

        // this.dbServer = '192.168.1.204'
        // this.dbPort = 3309
        // this.dbName = 'wechat_oa'
        // this.dbUser = 'test_user'
        // this.dbPass = 'test_user'
      }
    })
  },
  methods: {
    onChangeDbType (e) {
      // 数据库默认端口自动补全
      this.dbTypes.forEach(d => {
        if (d.type === e.target.value) {
          this.dbPort = d.port
        }
      })
    },
    handleConnectDatabaseSubmit (e) {
      // 使用that记录this 防止 this.$router 报错
      const that = this
      this.spinning = true
      e.preventDefault()
      this.connectDatabaseForm.validateFields((err, values) => {
        // 验证通过执行请求
        if (!err) {
          try {
            API.connectDatabase({ ...values })
            this.spinning = false
            setTimeout(function () {
              // 跳转到生成代码页面
              that.$router.push({
                name: 'code',
                // path: '/code/index',
                params: {
                  connectType: 'db',
                  dbType: that.dbType,
                  dbServer: that.dbServer,
                  dbPort: that.dbPort,
                  dbName: that.dbName,
                  dbUser: that.dbUser,
                  dbPass: that.dbPass,
                },
              })
            }, 1200)
          } catch (error) {
            this.$notification.error({
              message: '错误',
              description:
                '数据连接失败：' + error + '，请检查配置项是否正确！',
              duration: 3,
            })
          } finally {
            this.spinning = false
          }
        }
      })
      this.spinning = false
    },
    handleConnectDatabaseReset () {
      // 重置连接数据库输入框
      this.connectDatabaseForm.resetFields()
    },
    handleImportSqlSubmit (e) {
      // 使用that记录this 防止 this.$router 报错
      const that = this
      this.spinning = true
      e.preventDefault()
      this.importSqlForm.validateFields((err, values) => {
        // 验证通过执行请求
        if (!err) {
          try {
            API.importSql({ ...values })
            this.spinning = false
            // 跳转到生成代码页面
            that.$router.push({
                name: 'code',
              // path: '/code/index',
              params: {
                connectType: 'sql',
              },
            })
          } catch (error) {
            this.$notification.error({
              message: '错误',
              description: 'SQL导入失败：' + error,
              duration: 3,
            })
          } finally {
            this.spinning = false
          }
        }
      })
      this.spinning = false
    },
    handleImportSqlReset () {
      // 重置导入SQL输入框
      this.importSqlForm.resetFields()
    },
  },
}
</script>

<style lang="less" scoped>
.spin-content {
  border: 1px solid #91d5ff;
  background-color: #e6f7ff;
  padding: 30px;
}
</style>
