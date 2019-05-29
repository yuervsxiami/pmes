package com.cnuip.pmes2.domain.core;

import lombok.Data;
import lombok.Getter;

import java.util.Objects;

/**
 * @auhor Crixalis
 * @date 2018/10/17 17:05
 */
@Getter
public class WordAndNature {

    private String word;
    private String nature;

    public WordAndNature(String word, String nature) {
        this.word = word;
        this.nature = nature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordAndNature that = (WordAndNature) o;
        if(this.word.equals(that.word) && this.nature.equals(that.nature)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "分词:'" + word + "\'" +
            ", 词性:'" + nature + "\'";
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, nature);
    }
};

