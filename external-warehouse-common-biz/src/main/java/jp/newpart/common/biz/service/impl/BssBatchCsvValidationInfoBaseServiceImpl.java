package jp.newpart.common.biz.service.impl;

import jp.newpart.common.biz.domain.entity.BssBatchCsvValidationInfoEntity;
import jp.newpart.common.biz.mapper.BssBatchCsvValidationInfoBaseMapper;
import jp.newpart.common.biz.service.IBssBatchCsvValidationInfoBaseService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * バッチ実行CSV検証情報 Service実装クラス
 *
 * @author nextosd
 */
@Service
public class BssBatchCsvValidationInfoBaseServiceImpl implements IBssBatchCsvValidationInfoBaseService {

    @Resource
    private BssBatchCsvValidationInfoBaseMapper bssBatchCsvValidationInfoBaseMapper;

    @Override
    public BssBatchCsvValidationInfoEntity selectById(String id) {
        return bssBatchCsvValidationInfoBaseMapper.selectById(id);
    }

    @Override
    public List<BssBatchCsvValidationInfoEntity> selectList(BssBatchCsvValidationInfoEntity bssBatchCsvValidationInfo) {
        return bssBatchCsvValidationInfoBaseMapper.selectList(bssBatchCsvValidationInfo);
    }

    @Override
    public int insert(BssBatchCsvValidationInfoEntity bssBatchCsvValidationInfo) {
        return bssBatchCsvValidationInfoBaseMapper.insert(bssBatchCsvValidationInfo);
    }

    @Override
    public int update(BssBatchCsvValidationInfoEntity bssBatchCsvValidationInfo) {
        return bssBatchCsvValidationInfoBaseMapper.update(bssBatchCsvValidationInfo);
    }

    @Override
    public int deleteById(String id) {
        return bssBatchCsvValidationInfoBaseMapper.deleteById(id);
    }

    /**
     * バッチ実行CSV検証情報を検索（CSVファイルの種類）
     *
     * @return バッチ実行CSV検証情報リスト
     */
    public List<BssBatchCsvValidationInfoEntity> selectByCsvFileType(String csvFileType) {
        BssBatchCsvValidationInfoEntity entity = new BssBatchCsvValidationInfoEntity();
        entity.setCsvFileType(csvFileType);
        return bssBatchCsvValidationInfoBaseMapper.selectList(entity);
    }
}
