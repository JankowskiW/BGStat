package pl.wj.bgstat.domain.rulebook.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

/**
 * LanguageISO enum contains most common used
 * languages ISO 639-1 codes
 */
public enum LanguageISO {
    /** Polish */
    PL,
    /** English */
    EN,
    /** German */
    DE,
    /** Japanese */
    JA,
    /** Chinese */
    ZH,
    /** Russian */
    RU,
    /** Ukrainian */
    UK,
    /** French */
    FR,
    /** Spanish */
    ES,
    /** Czech */
    CS,
    /** Slovak */
    SK;

    @JsonCreator
    public static LanguageISO getLanguageISO(String value) {
        LanguageISO languageISO =
                Arrays.stream(LanguageISO.values()).filter(v -> v.toString().equals(value)).findFirst().orElse(null);
        return languageISO;
    }
}
