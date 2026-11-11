package jp.newpart.common.core.domain.entity;

import lombok.Data;

/**
 * シーケンス ranged_seq
 *
 * @author nextosd
 */
@Data
public class RangedSeq {

    /**
     * Id
     */
    private String id;

    /**
     * キー1
     */
    private String key1;

    /**
     * 値1
     */
    private String value1;

    /**
     * キー2
     */
    private String key2;

    /**
     * 値2
     */
    private String value2;


    /**
     * キー3
     */
    private String key3;

    /**
     * 値3
     */
    private String value3;

    private Integer seq;


}
