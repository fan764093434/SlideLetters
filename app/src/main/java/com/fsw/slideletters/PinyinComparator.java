package com.fsw.slideletters;


import android.text.TextUtils;

import com.fsw.slidelettersmenu.CharacterParser;

import java.util.Comparator;

public class PinyinComparator implements Comparator<String> {

    private CharacterParser characterParser;

    public PinyinComparator() {
        characterParser = CharacterParser.getInstance();
    }


    public int compare(String o1, String o2) {
        String o1Ping = characterParser.getSelling(o1);
        String o2Ping = characterParser.getSelling(o2);
        if (TextUtils.isEmpty(o1Ping)) {
            o1Ping = "1";
        }
        if (TextUtils.isEmpty(o2Ping)) {
            o2Ping = "1";
        }
        String sortO1 = o1Ping.substring(0, 1).toUpperCase();
        String sortO2 = o2Ping.substring(0, 1).toUpperCase();
        if (sortO1.equals("@") || sortO2.equals("#")) {
            return -1;
        } else if (sortO1.equals("#") || sortO2.equals("@")) {
            return 1;
        } else {
            return sortO1.compareTo(sortO2);
        }
    }

}
