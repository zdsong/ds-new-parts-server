package jp.nasta.pms.common.biz.utils;

import jp.nasta.pms.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 登録・更新の共通項目を設定する
 */
public final class EntityAuditHelper {

    private EntityAuditHelper() {
    }

    /**
     * 新規登録時の登録者・更新者を設定する
     *
     * @param entity   対象
     * @param now      登録日時
     * @param operator 登録者
     * @param pgId     プログラムＩＤ
     */
    public static void fillForInsert(BaseEntity entity, Date now, String operator, String pgId) {
        entity.setCreateTime(now);
        entity.setCreateBy(operator);
        entity.setCreatePgId(pgId);
        entity.setUpdateTime(now);
        entity.setUpdateBy(operator);
        entity.setUpdatePgId(pgId);
    }
}
