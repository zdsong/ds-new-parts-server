package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ユーザー倉庫関係の一括設定
 *
 * @author nextosd
 */
@Data
public class MastUserRelationBatchSaveBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 選択行へ一律に適用する関係タイプ
     */
    private List<String> relationTypes = new ArrayList<>();

    /**
     * 設定対象（ユーザーと倉庫の組み合わせ）
     */
    private List<MastUserRelationSaveBo> targets = new ArrayList<>();
}
