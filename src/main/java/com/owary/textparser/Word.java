package com.owary.textparser;

import java.io.Serializable;
import java.util.Objects;

public class Word implements Serializable {

    private String text;
    private String partOfSpeech;

    public Word(String text) {
        this.text = text;
        this.partOfSpeech = "";
    }

    public Word(String text, String partOfSpeech) {
        this.text = text;
        this.partOfSpeech = partOfSpeech;
    }

    public String getText() {
        return text;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    @Override
    public String toString(){
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;
        Word word = (Word) o;
        return getText().equals(word.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getText(), getPartOfSpeech());
    }
}
