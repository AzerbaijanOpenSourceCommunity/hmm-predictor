package com.owary.textparser;

import java.io.Serializable;
import java.util.Objects;

public class WordPair implements Comparable<WordPair>, Serializable {

    private Word word;
    private Word next;
    private int occurred = 1;
    private int pairOccurred = 1;

    public WordPair(String word, String next) {
        this.word = new Word(word);
        this.next = new Word(next);
    }

    public void wordOccurred(int i){
        this.occurred = i;
    }

    public void pairOccurred(){
        pairOccurred++;
    }

    public Word getWord() {
        return word;
    }

    public Word getNext() {
        return next;
    }

    public int getOccurred() {
        return occurred;
    }

    public int getPairOccurred() {
        return pairOccurred;
    }

    @Override
    public int compareTo(WordPair o) {
        return Integer.compare(o.getPairOccurred(), this.getPairOccurred());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WordPair)) return false;
        WordPair wordPair = (WordPair) o;
        return Objects.equals(getWord(), wordPair.getWord()) &&
                Objects.equals(getNext(), wordPair.getNext());
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, next);
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %d", word, next, pairOccurred);
    }
}
