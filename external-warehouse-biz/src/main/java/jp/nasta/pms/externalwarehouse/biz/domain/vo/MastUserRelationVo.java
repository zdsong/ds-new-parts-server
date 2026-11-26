package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ユーザーと倉庫の組み合わせでまとめた関係
 *
 * @author nextosd
 */
@Data
public class MastUserRelationVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 一覧の行キー（ユーザー名|倉庫コード）
     */
    private String id;

    private String userName;

    private String nickName;

    private String whCd;

    private String whNm;

    private String memo;

    /**
     * 付与されている関係タイプ
     */
    private List<String> relationTypes = new ArrayList<>();

    /**
     * 更新・削除時の楽観ロック情報
     */
    private List<MastUserRelationTypeVo> typeRows = new ArrayList<>();
}
