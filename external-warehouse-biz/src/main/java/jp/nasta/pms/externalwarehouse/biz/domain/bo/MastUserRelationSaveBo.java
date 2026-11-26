package jp.nasta.pms.externalwarehouse.biz.domain.bo;

import jp.nasta.pms.externalwarehouse.biz.domain.vo.MastUserRelationTypeVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ユーザー倉庫関係の保存・削除対象
 *
 * @author nextosd
 */
@Data
public class MastUserRelationSaveBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ユーザーID（sys_user.user_name）
     */
    private String userName;

    /**
     * 倉庫コード
     */
    private String whCd;

    /**
     * 保存後に有効にする関係タイプ
     */
    private List<String> relationTypes = new ArrayList<>();

    /**
     * 画面表示時点の関係タイプ（楽観ロック）
     */
    private List<MastUserRelationTypeVo> typeRows = new ArrayList<>();
}
