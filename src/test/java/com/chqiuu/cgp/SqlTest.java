package com.chqiuu.cgp;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.chqiuu.cgp.db.BaseDatabase;
import com.chqiuu.cgp.db.DatabaseFactory;
import com.chqiuu.cgp.db.entity.TableEntity;
import com.chqiuu.cgp.db.enums.DriverClassEnum;

import java.util.List;

public class SqlTest {
    public static void main(String[] args) {
        String sql = "CREATE TABLE `auth_user_role`  (\n" +
                "  `user_id` bigint(20) UNSIGNED NOT NULL COMMENT '用户ID',\n" +
                "  `role_id` int(10) UNSIGNED NOT NULL COMMENT '角色ID',\n" +
                "  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',\n" +
                "  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',\n" +
                "  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,\n" +
                "  INDEX `Index_user_id`(`user_id`) USING BTREE\n" +
                ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;" +
                "CREATE TABLE `auth_role` (\n" +
                "  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色唯一ID',\n" +
                "  `role_name` varchar(32) DEFAULT '' COMMENT '角色名称',\n" +
                "  `role_code` varchar(32) DEFAULT '' COMMENT '角色代码',\n" +
                "  `role_description` varchar(256) DEFAULT '' COMMENT '角色描述',\n" +
                "  `is_deleted` tinyint(3) unsigned DEFAULT '0' COMMENT '是否删除',\n" +
                "  `del_time` datetime DEFAULT NULL COMMENT '删除时间',\n" +
                "  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',\n" +
                "  PRIMARY KEY (`role_id`),\n" +
                "  KEY `Index_role_key` (`role_code`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';" +
                "CREATE TABLE `base_declaration` (\n" +
                "  `declaration_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '基地申报唯一ID',\n" +
                "  `batch_id` int(11) NOT NULL COMMENT '批次唯一ID',\n" +
                "  `base_id` bigint(20) NOT NULL COMMENT '基地唯一ID',\n" +
                "  `unit_address_province` varchar(8) DEFAULT '' COMMENT '单位地址省',\n" +
                "  `unit_address_city` varchar(8) DEFAULT '' COMMENT '单位地址地市',\n" +
                "  `unit_address_county` varchar(8) DEFAULT '' COMMENT '单位地址区县',\n" +
                "  `unit_address_detail` varchar(128) DEFAULT '' COMMENT '单位详细地址（不含省市县）',\n" +
                "  `legal_representative` varchar(32) DEFAULT '' COMMENT '法定代表人',\n" +
                "  `business_license_url` varchar(256) DEFAULT '' COMMENT '营业执照URL',\n" +
                "  `unified_social_credit_identifier` char(18) DEFAULT '' COMMENT '统一社会信用代码',\n" +
                "  `competent_department` varchar(32) DEFAULT '' COMMENT '上级主管部门',\n" +
                "  `postal_address_province` varchar(16) DEFAULT '' COMMENT '通讯地址省',\n" +
                "  `postal_address_city` varchar(16) DEFAULT '' COMMENT '通讯地址地市',\n" +
                "  `postal_address_county` varchar(16) DEFAULT '' COMMENT '通讯地址区县',\n" +
                "  `postal_address_detail` varchar(128) DEFAULT '' COMMENT '通讯详细地址（不含省市县）',\n" +
                "  `post_code` char(6) DEFAULT '' COMMENT '邮政编码',\n" +
                "  `contact_person` varchar(32) DEFAULT '' COMMENT '联系人姓名',\n" +
                "  `fixed_telephone` varchar(32) DEFAULT '' COMMENT '固定电话',\n" +
                "  `mobile_phone` char(11) DEFAULT '' COMMENT '移动电话',\n" +
                "  `fax` varchar(32) DEFAULT '' COMMENT '传真',\n" +
                "  `public_email` varchar(32) DEFAULT '' COMMENT '公共邮箱',\n" +
                "  `base_type` varchar(32) DEFAULT '' COMMENT '基地类型',\n" +
                "  `industry_field` varchar(32) DEFAULT '' COMMENT '基地领域',\n" +
                "  `full_time_staff` int(10) unsigned DEFAULT NULL COMMENT '专职人员人数',\n" +
                "  `part_time_staff` int(10) unsigned DEFAULT NULL COMMENT '兼职人员人数',\n" +
                "  `total_area` decimal(8,2) DEFAULT NULL COMMENT '科普展示场所总面积(平方米)',\n" +
                "  `indoor_area` decimal(8,2) DEFAULT NULL COMMENT '室内场馆面积(平方米)',\n" +
                "  `outdoor_area` decimal(8,2) DEFAULT NULL COMMENT '室外面积(平方米)',\n" +
                "  `report_hall_area` decimal(8,2) DEFAULT NULL COMMENT '多功能报告厅面积(平方米)',\n" +
                "  `opening_days_per_year` int(11) DEFAULT NULL COMMENT '对外开放天数(每年)',\n" +
                "  `free_days_per_year` int(11) DEFAULT NULL COMMENT '免费开放天数(每年)',\n" +
                "  `reception_amount_per_year` int(11) DEFAULT NULL COMMENT '接待公众人数(每年)',\n" +
                "  `government_investment_amount` decimal(8,2) DEFAULT NULL COMMENT '政府投资(万元)',\n" +
                "  `social_assistance_amount` decimal(8,2) DEFAULT NULL COMMENT '社会赞助(万元)',\n" +
                "  `unit_self_raised_amount` decimal(8,2) DEFAULT NULL COMMENT '单位自筹(万元)',\n" +
                "  `other_sources_amount` decimal(8,2) DEFAULT NULL COMMENT '其他资金(万元)',\n" +
                "  `official_website` varchar(128) DEFAULT '' COMMENT '官网地址',\n" +
                "  `other_url` varchar(256) DEFAULT '' COMMENT '其他网页地址',\n" +
                "  `weibo_name` varchar(32) DEFAULT '' COMMENT '微博名称',\n" +
                "  `wechat_name` varchar(32) DEFAULT '' COMMENT '微信名称',\n" +
                "  `base_overview` varchar(1200) DEFAULT '' COMMENT '基地总体概况',\n" +
                "  `content_system` varchar(1200) DEFAULT '' COMMENT '科普展示内容体系',\n" +
                "  `commentary` varchar(2400) DEFAULT '' COMMENT '基地科普解说词',\n" +
                "  `submit_time` datetime DEFAULT NULL COMMENT '基地提交时间',\n" +
                "  `audit_status` int(11) DEFAULT '0' COMMENT '总体审核状态',\n" +
                "  `city_audit_dept` varchar(16) DEFAULT NULL COMMENT '市级审核部门',\n" +
                "  `city_auditor` varchar(16) DEFAULT NULL COMMENT '市级审核人',\n" +
                "  `city_audit_time` datetime DEFAULT NULL COMMENT '市级审核时间',\n" +
                "  `city_audit_opinion` varchar(256) DEFAULT NULL COMMENT '市级审核意见',\n" +
                "  `city_audit_status` int(11) DEFAULT '0' COMMENT '市级审核状态',\n" +
                "  `province_audit_dept` varchar(16) DEFAULT NULL COMMENT '省级审核部门',\n" +
                "  `province_auditor` varchar(16) DEFAULT NULL COMMENT '省级审核人',\n" +
                "  `province_audit_time` datetime DEFAULT NULL COMMENT '省级审核时间',\n" +
                "  `province_audit_opinion` varchar(256) DEFAULT NULL COMMENT '省级审核意见',\n" +
                "  `province_audit_status` int(11) DEFAULT '0' COMMENT '省级审核状态',\n" +
                "  `province_audit_file_json` varchar(512) DEFAULT NULL COMMENT '省级审核文件',\n" +
                "  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',\n" +
                "  PRIMARY KEY (`declaration_id`)\n" +
                ") ENGINE=InnoDB AUTO_INCREMENT=1265486604411813891 DEFAULT CHARSET=utf8mb4 COMMENT='基地申报信息表';";
        String dbType = JdbcConstants.MYSQL;
        // 格式化输出
        String result = SQLUtils.format(sql, dbType);
        BaseDatabase database = new DatabaseFactory().create(DriverClassEnum.MYSQL);
        List<TableEntity> tableList = database.getTableList(sql);
        for (TableEntity tableEntity : tableList) {
            System.out.println(tableEntity);
        }
    }
}
