import { get, post, postDownload } from '@/utils/request'

export default {
  /**
   * 连接数据库
   * @param {object} data
   * @param {string} data.dbType
   * @param {string} data.server
   * @param {number} data.port
   * @param {string} data.database
   * @param {string} data.username
   * @param {string} data.password
   * @returns 是否连接成功
   */
  connectDatabase (data) {
    return post('/generator/connectDatabase', data)
  },

  /**
   * SQL脚本导入
   * @param {object} data
   * @param {string} data.dbType
   * @param {string} data.ddl
   * @returns 是否导入成功
   */
  importSql (data) {
    return post('/generator/importSql', data)
  },

  /**
   * 数据库表列表
   * @param {object} params
   * @param {string} params.tableName
   * @returns {*}
   */
  queryTableList (params) {
    return get('/generator/queryTableList', params)
  },

  /**
   * 获取数据库列表
   * @returns 数据库列表
   */
   getAllDatabaseList () {
    return get('/generator/getAllDatabases')
  },
  /**
   * 获取数据库表列表
   * @returns 数据库表列表
   */
  getAllTableList (params) {
    return get('/generator/getAllTables', params)
  },

  /**
   * 多表批量生成代码
   * @returns {*}
   */
  generatorCodes (data) {
    return postDownload('/generator/generatorCodes', data)
  },

  /**
   * 预览生成的代码
   * @param {object} params
   * @param {string} params.rootPackage
   * @param {string} params.moduleName
   * @param {string} params.author
   * @param {string} params.table
   * @param {string} params.mappingName
   * @param {string} params.isPlus
   * @returns {*}
   */
  preview (data) {
    return post('/generator/preview', data)
  },
}
