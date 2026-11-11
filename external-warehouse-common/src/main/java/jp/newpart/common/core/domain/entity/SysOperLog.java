package jp.newpart.common.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jp.newpart.common.annotation.Excel;
import jp.newpart.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * オペレーションログテーブル oper_log
 * 
 * @author nextosd
 */
public class SysOperLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 番号 */
    @Excel(name = "番号", cellType = Excel.ColumnType.STRING)
    private String operId;

    /** オペレーションモジュール */
    @Excel(name = "オペレーションモジュール")
    private String title;

    /** ビジネス・タイプ（0=その他,1=新規,2=修正,3=削除,4=承認,5=エクスポート,6=インポート,7=強退,8=コード生成,9=データ消去） */
    @Excel(name = "ビジネス・タイプ", readConverterExp = "0=その他,1=新規,2=修正,3=削除,4=承認,5=エクスポート,6=インポート,7=強退,8=コード生成,9=データ消去")
    private Integer businessType;

    /** ビジネスタイプ配列 */
    private Integer[] businessTypes;

    /** 方法 */
    @Excel(name = "方法")
    private String method;

    /** リクエストモード */
    @Excel(name = "リクエストモード")
    private String requestMethod;

    /** 操作カテゴリ（0=その他,1=バックグラウンドユーザ,2=携帯端末ユーザ） */
    @Excel(name = "操作カテゴリ", readConverterExp = "0=その他,1=バックグラウンドユーザ,2=携帯端末ユーザ")
    private Integer operatorType;

    /** オペレータ */
    @Excel(name = "オペレータ")
    private String operName;

    /** リクエストアドレス */
    @Excel(name = "リクエストアドレス")
    private String operUrl;

    /** オペレーションIP */
    @Excel(name = "オペレーションIP")
    private String operIp;

    /** オペレーションサイト */
    @Excel(name = "オペレーションサイト")
    private String operLocation;

    /** パラメータ */
    @Excel(name = "パラメータ")
    private String operParam;

    /** 戻り値 */
    @Excel(name = "戻り値")
    private String jsonResult;

    /** 状態（0=正常、1=異常） */
    @Excel(name = "状態", readConverterExp = "0=正常,1=異常")
    private Integer status;

    /** エラーメッセージ */
    @Excel(name = "エラーメッセージ")
    private String errorMsg;

    /** 時間 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "時間", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    /** 消費時間 */
    @Excel(name = "消費時間", suffix = "ミリ秒")
    private Long costTime;

    /**
     * ログイン開始時間
     */
    private String beginTime;

    /**
     * ログイン終了時間
     */
    private String endTime;

    public String getOperId()
    {
        return operId;
    }

    public void setOperId(String operId)
    {
        this.operId = operId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Integer getBusinessType()
    {
        return businessType;
    }

    public void setBusinessType(Integer businessType)
    {
        this.businessType = businessType;
    }

    public Integer[] getBusinessTypes()
    {
        return businessTypes;
    }

    public void setBusinessTypes(Integer[] businessTypes)
    {
        this.businessTypes = businessTypes;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getRequestMethod()
    {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod)
    {
        this.requestMethod = requestMethod;
    }

    public Integer getOperatorType()
    {
        return operatorType;
    }

    public void setOperatorType(Integer operatorType)
    {
        this.operatorType = operatorType;
    }

    public String getOperName()
    {
        return operName;
    }

    public void setOperName(String operName)
    {
        this.operName = operName;
    }

    public String getOperUrl()
    {
        return operUrl;
    }

    public void setOperUrl(String operUrl)
    {
        this.operUrl = operUrl;
    }

    public String getOperIp()
    {
        return operIp;
    }

    public void setOperIp(String operIp)
    {
        this.operIp = operIp;
    }

    public String getOperLocation()
    {
        return operLocation;
    }

    public void setOperLocation(String operLocation)
    {
        this.operLocation = operLocation;
    }

    public String getOperParam()
    {
        return operParam;
    }

    public void setOperParam(String operParam)
    {
        this.operParam = operParam;
    }

    public String getJsonResult()
    {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult)
    {
        this.jsonResult = jsonResult;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public String getErrorMsg()
    {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {
        this.errorMsg = errorMsg;
    }

    public Date getOperTime()
    {
        return operTime;
    }

    public void setOperTime(Date operTime)
    {
        this.operTime = operTime;
    }

    public Long getCostTime()
    {
        return costTime;
    }

    public void setCostTime(Long costTime)
    {
        this.costTime = costTime;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
