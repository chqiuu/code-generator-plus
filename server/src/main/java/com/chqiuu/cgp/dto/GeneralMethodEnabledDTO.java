package com.chqiuu.cgp.dto;

import lombok.Data;

/**
 * 是否生成通用方法
 */
@Data
public class GeneralMethodEnabledDTO {

    /**
     * 是否启用 新建接口 方法
     */
    private Integer addEnabled = 0;
    /**
     * 是否启用 修改接口 方法
     */
    private Integer updateEnabled = 0;
    /**
     * 是否启用 插入，已有不替换 方法
     */
    private Integer insertIgnoreEnabled = 0;
    /**
     * 是否启用 替换 方法
     */
    private Integer replaceEnabled = 0;
    /**
     * 是否启用根据唯一ID获取详细信息方法
     */
    private Integer getDetailByIdEnabled = 0;
    /**
     * 是否启用 列表 方法
     */
    private Integer getListEnabled = 0;
    /**
     * 是否启用 分页查询 方法
     */
    private Integer getPageEnabled = 0;
}
