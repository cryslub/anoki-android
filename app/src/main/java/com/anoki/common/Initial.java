package com.anoki.common;

import android.provider.ContactsContract;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015-08-13.
 */
public class Initial {


    public static final char HANGUL_BEGIN_UNICODE = 44032; // 가
    public static final char HANGUL_LAST_UNICODE = 55203; // 힣
    public static final char HANGUL_BASE_UNIT = 588;//각자음 마다 가지는 글자수

    public static final char[] INITIAL_SOUND = { 'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'};
    public static Map<Character,Character> initialSoundMap = new HashMap<Character,Character>();



    static{
        for(char c : INITIAL_SOUND){
            initialSoundMap.put(c,c);
        }
    }


    public static boolean isInitialSound(char searchar){
        Character c = initialSoundMap.get(searchar);

        return c!=null;
    }

    public static int getInitialSound(char c) {
        int hanBegin = (c - HANGUL_BEGIN_UNICODE);
        int index = hanBegin / HANGUL_BASE_UNIT;
        return index;
    }

    public static boolean isHangul(char c) {
        return HANGUL_BEGIN_UNICODE <= c && c <= HANGUL_LAST_UNICODE;
    }


}
