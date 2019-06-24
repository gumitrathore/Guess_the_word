package com.example.test0.models;

import java.util.ArrayList;

public class Level {
 private String type;
 private ArrayList<Word> words;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }
}
