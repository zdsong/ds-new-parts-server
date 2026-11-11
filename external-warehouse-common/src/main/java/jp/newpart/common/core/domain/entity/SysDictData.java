package jp.newpart.common.core.domain.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jp.newpart.common.annotation.Excel;
import jp.newpart.common.constant.UserConstants;
import jp.newpart.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 辞書データシート sys_dict_data
 * 
 * @author nextosd
 */
public class SysDictData extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 辞書コード */
    @Excel(name = "辞書コード", cellType = Excel.ColumnType.STRING)
    private String dictCode;

    /** 辞書のソート */
    @Excel(name = "辞書ソート", cellType = Excel.ColumnType.NUMERIC)
    private Long dictSort;

    /** 辞書ラベル */
    @Excel(name = "辞書ラベル")
    private String dictLabel;

    /** 辞書キー値 */
    @Excel(name = "辞書キー値")
    private String dictValue;

    /** 辞書の種類 */
    @Excel(name = "辞書種類")
    private String dictType;

    /** スタイルプロパティ */
    private String cssClass;

    /** 辞書スタイル */
    private String listClass;

    /** デフォルト（Y/N） */
    @Excel(name = "デフォルト", readConverterExp = "Y=Yes、N=No")
    private String isDefault;

    /** ステータス（0通常 1停止） */
    @Excel(name = "ステータス", readConverterExp = "0=正常,1=停止")
    private String status;

    /** 備考 */
    private String remark;

    public String getDictCode()
    {
        return dictCode;
    }

    public void setDictCode(String dictCode)
    {
        this.dictCode = dictCode;
    }

    public Long getDictSort()
    {
        return dictSort;
    }

    public void setDictSort(Long dictSort)
    {
        this.dictSort = dictSort;
    }

    @NotBlank(message = "辞書ラベルを空にすることはできません")
    @Size(min = 0, max = 100, message = "辞書ラベルの長さは100文字以内")
    public String getDictLabel()
    {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel)
    {
        this.dictLabel = dictLabel;
    }

    @NotBlank(message = "辞書キー値は空にできません")
    @Size(min = 0, max = 100, message = "辞書キーの長さは100文字以内")
    public String getDictValue()
    {
        return dictValue;
    }

    public void setDictValue(String dictValue)
    {
        this.dictValue = dictValue;
    }

    @NotBlank(message = "辞書タイプを空にすることはできません")
    @Size(min = 0, max = 100, message = "辞書タイプの長さは100文字以内")
    public String getDictType()
    {
        return dictType;
    }

    public void setDictType(String dictType)
    {
        this.dictType = dictType;
    }

    @Size(min = 0, max = 100, message = "スタイル属性の長さは100文字以内")
    public String getCssClass()
    {
        return cssClass;
    }

    public void setCssClass(String cssClass)
    {
        this.cssClass = cssClass;
    }

    public String getListClass()
    {
        return listClass;
    }

    public void setListClass(String listClass)
    {
        this.listClass = listClass;
    }

    public boolean getDefault()
    {
        return UserConstants.YES.equals(this.isDefault);
    }

    public String getIsDefault()
    {
        return isDefault;
    }

    public void setIsDefault(String isDefault)
    {
        this.isDefault = isDefault;
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
            .append("dictCode", getDictCode())
            .append("dictSort", getDictSort())
            .append("dictLabel", getDictLabel())
            .append("dictValue", getDictValue())
            .append("dictType", getDictType())
            .append("cssClass", getCssClass())
            .append("listClass", getListClass())
            .append("isDefault", getIsDefault())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
