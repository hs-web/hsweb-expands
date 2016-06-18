package org.hsweb.expands.office.word.api.poi;


import org.hsweb.expands.office.word.WordApi;

/**
 * Created by 浩 on 2015-12-18 0018.
 */
public class POIWordApi {
    public static final WordApi getDocxInstance() {
        return POIWordApi4Docx.getInstance();
    }
}
