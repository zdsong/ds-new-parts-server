package jp.newpart.common.biz.service;

import jp.newpart.common.biz.domain.entity.BssUserCertificationEntity;

import java.util.List;


/**
 * ユーザー認証情報Serviceインタフェース
 *
 * @author nextosd
 */
public interface IBssUserCertificationBaseService {

     /**
     * ユーザー認証情報を検索
     *
     * @param id ユーザー認証情報プライマリ・キー
     * @return ユーザー認証情報
     */
    public BssUserCertificationEntity selectById(String id);

     /**
     * ユーザー認証情報を検索リスト
     *
     * @param bssUserCertification ユーザー認証情報パラメータ対象
     * @return ユーザー認証情報リスト
     */
    public List<BssUserCertificationEntity> selectList(BssUserCertificationEntity bssUserCertification);

     /**
     * ユーザー認証情報を登録
     *
     * @param bssUserCertification ユーザー認証情報
     * @return  結果
     */
    public int insert(BssUserCertificationEntity bssUserCertification);

     /**
     * ユーザー認証情報を修正
     *
     * @param bssUserCertification ユーザー認証情報
     * @return  結果
     */
    public int update(BssUserCertificationEntity bssUserCertification);

     /**
     * ユーザー認証情報を削除
     *
     * @param id ユーザー認証情報プライマリ・キー
     * @return  結果
     */
    public int deleteById(String id);

    /**
     * 農家IDによる認証記録を廃止
     *
     * @param bssUserCertification
     * @return
     */
    int deleteLogicByFarmId(BssUserCertificationEntity bssUserCertification);

    /**
     * IDによる認証記録を廃止
     *
     * @param bssUserCertification
     * @return
     */
    int deleteLogicById(BssUserCertificationEntity bssUserCertification);
}
