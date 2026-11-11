package jp.newpart.common.biz.domain.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jp.newpart.common.annotation.Excel;
import jp.newpart.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * パラメータ設定テーブル sys_config
 * 
 * @author nextosd
 */
public class SysConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** パラメータキー */
    @Excel(name = "パラメータキー", cellType = Excel.ColumnType.STRING)
    private String configId;

    /** パラメータ名 */
    @Excel(name = "パラメータ名")
    private String configName;

    /** パラメータキー名 */
    @Excel(name = "パラメータキー名")
    private String configKey;

    /** パラメータキー値 */
    @Excel(name = "パラメータキー値")
    private String configValue;

    /** システム組み込み（Y Yes N No） */
    @Excel(name = "システム組み込み", readConverterExp = "Y=はい,N=いいえ")
    private String configType;

    /**
     * システム組み込み（Y Yes N No）
     */
    private String configTypeLabel;

    /** 備考 */
    private String remark;

    public String getConfigId()
    {
        return configId;
    }

    public void setConfigId(String configId)
    {
        this.configId = configId;
    }

    @NotBlank(message = "パラメータ名を空にすることはできません")
    @Size(min = 0, max = 100, message = "パラメータ名は100文字を超えることはできません")
    public String getConfigName()
    {
        return configName;
    }

    public void setConfigName(String configName)
    {
        this.configName = configName;
    }

    @NotBlank(message = "パラメータキー名の長さを空にすることはできません")
    @Size(min = 0, max = 100, message = "パラメータキー名の長さは100文字以内")
    public String getConfigKey()
    {
        return configKey;
    }

    public void setConfigKey(String configKey)
    {
        this.configKey = configKey;
    }

    @NotBlank(message = "パラメータキー値を空にすることはできません")
    public String getConfigValue()
    {
        return configValue;
    }

    public void setConfigValue(String configValue)
    {
        this.configValue = configValue;
    }

    public String getConfigType()
    {
        return configType;
    }

    public void setConfigType(String configType)
    {
        this.configType = configType;
    }

    public String getConfigTypeLabel() {
        return configTypeLabel;
    }

    public void setConfigTypeLabel(String configTypeLabel) {
        this.configTypeLabel = configTypeLabel;
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
            .append("configId", getConfigId())
            .append("configName", getConfigName())
            .append("configKey", getConfigKey())
            .append("configValue", getConfigValue())
            .append("configType", getConfigType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
