package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * ユーザー倉庫関係の検索条件
 *
 * @author nextosd
 */
@Data
public class MastUserRelationSearchBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 保管倉庫コード
     */
    private String whCd;

    /**
     * ユーザー名（ログインIDまたは表示名）
     */
    private String userName;

    /**
     * 関係タイプ（辞書値をカンマ区切り）
     */
    private String relationType;
}
