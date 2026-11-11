package jp.newpart.common.core.domain.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jp.newpart.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import jp.newpart.common.core.domain.BaseEntity;

/**
 * 辞書タイプテーブル sys_dict_type
 * 
 * @author nextosd
 */
public class SysDictType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 辞書キー */
    @Excel(name = "辞書キー", cellType = Excel.ColumnType.STRING)
    private String dictId;

    /** 辞書名 */
    @Excel(name = "辞書名")
    private String dictName;

    /** 辞書の種類 */
    @Excel(name = "辞書種類")
    private String dictType;

    /** ステータス（0通常 1停止） */
    @Excel(name = "ステータス", readConverterExp = "0=通常,1=停止")
    private String status;

    /** 備考 */
    private String remark;

    public String getDictId()
    {
        return dictId;
    }

    public void setDictId(String dictId)
    {
        this.dictId = dictId;
    }

    @NotBlank(message = "辞書名を空白にすることはできません")
    @Size(min = 0, max = 100, message = "辞書タイプ名の長さは100文字以内")
    public String getDictName()
    {
        return dictName;
    }

    public void setDictName(String dictName)
    {
        this.dictName = dictName;
    }

    @NotBlank(message = "辞書タイプを空にすることはできません")
    @Size(min = 0, max = 100, message = "辞書タイプの長さは100文字以内")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "辞書タイプは文字で始まる必要があり、（小文字、数字、下降線）のみ")
    public String getDictType()
    {
        return dictType;
    }

    public void setDictType(String dictType)
    {
        this.dictType = dictType;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dictId", getDictId())
            .append("dictName", getDictName())
            .append("dictType", getDictType())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
