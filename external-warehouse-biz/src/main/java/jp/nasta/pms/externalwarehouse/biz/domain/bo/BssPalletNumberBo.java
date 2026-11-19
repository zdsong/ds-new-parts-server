package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import jp.nasta.pms.common.biz.domain.entity.BssPalletNumberDetailEntity;
import jp.nasta.pms.common.biz.domain.entity.BssPalletNumberEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.List;

/**
 * パレット番号 登録・更新パラメータ
 *
 * パレット情報（ヘッダ）と積載明細（明細）をまとめて受け取る。
 *
 * @author nextosd
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BssPalletNumberBo extends BssPalletNumberEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 積載明細
     */
    private List<BssPalletNumberDetailEntity> details;
}
