package com.chqiuu.cgp.common.enums;

import lombok.Getter;

/**
 * 生成方法枚举类
 */
@Getter
public enum GeneralMethodEnum {
    /**
     * 新建
     */
    ADD("add", "新建", new String[]{}),
    /**
     * 更新
     */
    UPDATE("update", "更新", new String[]{}),
    /**
     * 插入
     */
    INSERT_IGNORE("insertIgnore", "插入一条数据（选择字段插入），如果中已经存在相同的记录，则忽略当前新数据", new String[]{}),
    /**
     * 替换
     */
    REPLACE("replace", "替换一条数据（选择字段插入），存在则替换，不存在则插入", new String[]{}),
    /**
     * 根据唯一ID获取详细信息
     */
    GET_DETAIL_BY_ID("getDetailById", "根据唯一ID获取详细信息", new String[]{"DetailDTO.java.ftl"}),
    /**
     * 列表
     */
    GET_LIST("getList", "列表", new String[]{"ListQuery.java.ftl"}),
    /**
     * 分页查询
     */
    GET_PAGE("getPage", "分页查询", new String[]{"PageQuery.java.ftl"});

    private final String method;
    private final String desc;
    private final String[] ftls;

    GeneralMethodEnum(String method, String desc, String[] ftls) {
        this.method = method;
        this.desc = desc;
        this.ftls = ftls;
    }

    public static GeneralMethodEnum getByMethod(String method) {
        for (GeneralMethodEnum e : values()) {
            if (e.getMethod().equals(method)) {
                return e;
            }
        }
        return null;
    }
}
