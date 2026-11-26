package jp.nasta.pms.externalwarehouse.biz.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * ユーザー倉庫関係の明細行（関係タイプごとに1行）
 *
 * @author nextosd
 */
@Data
public class MastUserRelationRowVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String userName;

    private String nickName;

    private String whCd;

    private String whNm;

    private String mappingType;

    private String memo;

    private Integer version;
}
