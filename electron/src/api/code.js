import { get, post, postDownload } from '@/utils/request'

export default {
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
  preview (params) {
    return get('/generator/preview', params)
  },
}
