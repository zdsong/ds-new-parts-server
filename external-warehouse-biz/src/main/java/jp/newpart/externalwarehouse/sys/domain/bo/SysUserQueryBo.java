package jp.newpart.externalwarehouse.sys.domain.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * ユーザ検索
 */
@Data
@Schema(description = "ユーザ検索")
public class SysUserQueryBo implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ユーザ番号
     */
    private String userId;

    /**
     * ユーザーアカウント
     */
   private String userName;

    /**
     * ユーザー名称
     */
    private String nickName;

    /** 利用状態（0利用中 1停止中） */
    private String status;

    /**
     * 権限
     */
    private String roleId;

    /**
     * 利用状態（0利用中 1停止中）
     */
    private List<String> statusList;

    /**
     * 権限
     */
    private List<String> roleIdList;
}
