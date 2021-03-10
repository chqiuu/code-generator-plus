import { post } from '@/utils/request'
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
}
