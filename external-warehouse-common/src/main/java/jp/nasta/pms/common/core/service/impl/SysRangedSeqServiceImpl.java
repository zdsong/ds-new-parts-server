package jp.nasta.pms.common.core.service.impl;

import jp.nasta.pms.common.core.domain.entity.SysRangedSeq;
import jp.nasta.pms.common.core.service.ISysRangedSeqService;
import jp.nasta.pms.common.mapper.SysRangedSeqMapper;
import jp.nasta.pms.common.utils.DateUtils;
import jp.nasta.pms.common.utils.StringUtils;
import jp.nasta.pms.common.utils.ValueUtils;
import jp.nasta.pms.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static jp.nasta.pms.common.constant.Constants.*;

/**
 * シーケンス状況情報サービス層処理
 *
 * @author nextosd
 */
@Service
public class SysRangedSeqServiceImpl implements ISysRangedSeqService {




    @Autowired
    private SysRangedSeqMapper rangedSeqMapper;

    /**
     * シリアル取得済
     * @param serialType シリアルタイプ
     * @return シリアル
     */
    @Override
    public String generateSerialNo(String serialType) {
        Integer seq = 0;
        StringBuilder result = new StringBuilder();
         if( ValueUtils.isEqual(SERIAL_PREFIX_PALLET, serialType)) {
            String dateTimeNowStr = DateUtils.dateTimeNow(DateUtils.YYYYMMDD);
            String subDateTimeNowStr =dateTimeNowStr.substring(2,dateTimeNowStr.length());
            seq = this.increaseSequence(SERIAL_TYPE_PALLET,subDateTimeNowStr,serialType,serialType,null,null);
            String serialNo = StringUtils.leftPad(String.valueOf(seq), 3, '0');
            result.append(SERIAL_PREFIX_PALLET);
            result.append(subDateTimeNowStr);
            result.append(HORIZONTAL_LINE);
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
        SysRangedSeq condition = new SysRangedSeq();
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
    private  Integer increaseSequence(SysRangedSeq condition) {

        SysRangedSeq rs = rangedSeqMapper.selectSysRangedSeq(condition);

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
