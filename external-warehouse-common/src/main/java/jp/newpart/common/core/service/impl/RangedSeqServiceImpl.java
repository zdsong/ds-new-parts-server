package jp.newpart.common.core.service.impl;

import jp.newpart.common.core.domain.entity.RangedSeq;
import jp.newpart.common.core.service.IRangedSeqService;
import jp.newpart.common.mapper.RangedSeqMapper;
import jp.newpart.common.utils.DateUtils;
import jp.newpart.common.utils.StringUtils;
import jp.newpart.common.utils.ValueUtils;
import jp.newpart.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * シーケンス状況情報サービス層処理
 *
 * @author nextosd
 */
@Service
public class RangedSeqServiceImpl implements IRangedSeqService {

    /**
     * 顧客シリアルキー
     */
    private static final String SERIAL_PREFIX_CUSTOMER = "C";
    /**
     * 顧客シリアルキー
     */
    private static final String SERIAL_PREFIX_FARMER = "F";
    /**
     * お問い合わせシリアルキー
     */
    private static final String SERIAL_PREFIX_ASK = "Y";

    @Autowired
    private RangedSeqMapper rangedSeqMapper;

    /**
     * シリアル取得済
     * @param serialType シリアルタイプ
     * @return シリアル
     */
    @Override
    public String generateSerialNo(String serialType) {
        Integer seq = 0;
        StringBuilder result = new StringBuilder();
        if( ValueUtils.isEqual(SERIAL_TYPE_CUSTOMER, serialType)) {
            seq = this.increaseSequence(SERIAL_PREFIX_CUSTOMER,SERIAL_PREFIX_CUSTOMER,serialType,serialType,null,null);
            String serialNo = StringUtils.leftPad(String.valueOf(seq), 10, '0');
            result.append(SERIAL_PREFIX_CUSTOMER);
            result.append(serialNo);
        } else if( ValueUtils.isEqual(SERIAL_TYPE_FARMER, serialType)) {
            seq = this.increaseSequence(SERIAL_PREFIX_FARMER,SERIAL_PREFIX_FARMER,serialType,serialType,null,null);
            String serialNo = StringUtils.leftPad(String.valueOf(seq), 8, '0');
            result.append(SERIAL_PREFIX_FARMER);
            result.append(serialNo);
        } else if( ValueUtils.isEqual(SERIAL_TYPE_ASK, serialType)) {
            String dateTimeNowStr = DateUtils.dateTimeNow(DateUtils.YYYYMMDD);
            seq = this.increaseSequence(SERIAL_PREFIX_FARMER,dateTimeNowStr,serialType,serialType,null,null);
            String serialNo = StringUtils.leftPad(String.valueOf(seq), 7, '0');
            result.append(SERIAL_PREFIX_ASK);
            result.append(dateTimeNowStr);
            result.append(serialNo);
        }

        return result.toString();
    }

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
    @Override
    public  Integer increaseSequence( String key1, String value1, String key2, String value2, String key3, String value3) {
        RangedSeq condition = new RangedSeq();
        condition.setKey1(key1);
        condition.setValue1(value1);
        condition.setKey2(key2);
        condition.setValue2(value2);
        condition.setKey3(key3);
        condition.setValue3(value3);
        return this.increaseSequence(condition);
    }

    /**
     * RangedSeqを見つけてシーケンスを上げて返します
     *
     * @param condition シーケンス
     * @return シーケンス結果
     */
    private  Integer increaseSequence(RangedSeq condition) {

        RangedSeq rs = rangedSeqMapper.selectRangedSeq(condition);

        if(rs == null) {
            rs = condition;
            rs.setId(IdUtils.nextIdStr());
            rs.setSeq(1);
            rangedSeqMapper.insert(rs);
        } else {
            rs.setSeq(rs.getSeq() + 1);
            rangedSeqMapper.update(rs);
        }

        return rs.getSeq();
    }

}
