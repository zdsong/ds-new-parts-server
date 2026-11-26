package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * ユーザー倉庫関係の関係タイプ1件
 *
 * @author nextosd
 */
@Data
public class MastUserRelationTypeVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * mast_user_relations.id
     */
    private String id;

    /**
     * 関係タイプ（辞書 bss_user_relation_type の値）
     */
    private String mappingType;

    /**
     * 楽観ロック用バージョン
     */
    private Integer version;
}
