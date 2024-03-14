package com.chqiuu.cgp.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.chqiuu.cgp.db.entity.ColumnEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;

import java.io.Serializable;

@Data
// @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 22)
@HeadFontStyle(fontHeightInPoints = 11)
@ContentFontStyle(fontHeightInPoints = 11)
@ApiModel(value = "质检员排名统计导出列表")
public class ExportColumnDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 字段名
     * `COLUMN_NAME` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    @ExcelProperty(value = "字段名称")
    private String columnName;
    /**
     * 字段类型。比如float(9,3)，varchar(50)。
     * `COLUMN_TYPE` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
     */

    @ExcelProperty(value = "字段类型")
    private String columnType;
    /**
     * 字段是否可以是NULL。
     * 该列记录的值是YES或者NO。
     * `IS_NULLABLE` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    @ExcelProperty(value = "可否为空")
    private String isNullable;
    /**
     * 字段注释
     * `COLUMN_COMMENT` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
     */
    @ExcelProperty(value = "注释")
    private String columnComment;

    public static ExportColumnDTO importEntity(ColumnEntity columnEntity) {
        ExportColumnDTO dto = new ExportColumnDTO();
        dto.setColumnName(columnEntity.getColumnName());
        dto.setColumnType(columnEntity.getColumnType());
        dto.setIsNullable(columnEntity.getIsNullable());
        dto.setColumnComment(columnEntity.getColumnComment());
        return dto;
    }
}
