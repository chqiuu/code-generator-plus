package com.chqiuu.cgp.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 生成代码预览DTO
 *
 * @author chqiu
 */
@Data
public class CodePreviewDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 生成文件包名
     */
    private String packageName;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件名称（显示名称）
     */
    private String showName;
    /**
     * 文件内容
     */
    private String content;
    /**
     * 代码语言
     */
    private String language;
}
