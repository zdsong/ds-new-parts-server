package jp.newpart.common.core.service;

/**
 * シーケンス状況情報サービス層処理
 *
 * @author nextosd
 */
public interface IRangedSeqService {
    /**
     * 顧客シリアル
     */
    public static final String SERIAL_TYPE_CUSTOMER = "customer";

    /**
     * 顧客シリアル
     */
    public static final String SERIAL_TYPE_FARMER = "farmer";

    /**
     * お問い合わせ
     */
    public static final String SERIAL_TYPE_ASK= "askNo";

    /**
     * RangedSeqを見つけてシーケンスを上げて返します
     * @param key1 キー1
     * @param value1 値1
     * @param key2 キー2
     * @param value2 値2
     * @param key3 キー3
     * @param value3 値3
     * @return シーケンス
     */
    public Integer increaseSequence(String key1, String value1, String key2, String value2, String key3, String value3);

    /**
     * シリアル取得済
     * @param serialType シリアルタイプ
     * @return シリアル
     */
    public String generateSerialNo(String serialType);
}
