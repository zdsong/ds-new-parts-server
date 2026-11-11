package jp.newpart.common.enums;

import java.util.function.Function;
import jp.newpart.common.utils.DesensitizedUtil;

/**
 * 敏感化タイプ
 *
 * @author nextosd
 */
public enum DesensitizedType
{
    /**
     * 名前
     */
    USERNAME(s -> s.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),

    /**
     * パスワード、すべての文字を*で置換
     */
    PASSWORD(DesensitizedUtil::password),

    /**
     * 身分証明書、中間10桁アスタリスク置換
     */
    ID_CARD(s -> s.replaceAll("(\\d{4})\\d{10}(\\d{4})", "$1** **** ****$2")),

    /**
     * 携帯電話番号、中央4桁アスタリスク置換
     */
    PHONE(s -> s.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")),

    /**
     * 電子メールボックス、最初の文字と@の後ろのアドレスだけを表示して表示して、その他のアスタリスク置換
     */
    EMAIL(s -> s.replaceAll("(^.)[^@]*(@.*$)", "$1****$2")),

    /**
     * 銀行カード番号、最後の4桁を保持、その他のアスタリスク置換
     */
    BANK_CARD(s -> s.replaceAll("\\d{15}(\\d{3})", "**** **** **** **** $1")),

    /**
     * 一般車両、新エネルギー車両を含むナンバープレート
     */
    CAR_LICENSE(DesensitizedUtil::carLicense);

    private final Function<String, String> desensitizer;

    DesensitizedType(Function<String, String> desensitizer)
    {
        this.desensitizer = desensitizer;
    }

    public Function<String, String> desensitizer()
    {
        return desensitizer;
    }
}
