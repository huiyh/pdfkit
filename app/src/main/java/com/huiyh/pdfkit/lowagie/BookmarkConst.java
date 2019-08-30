package com.huiyh.pdfkit.lowagie;

/**
 * Created by huiyh on 2017/2/6.
 */
public class BookmarkConst {

    public static final String KEY_TITLE = "Title";
    public static final String KEY_PAGE = "Page";
    public static final String KEY_INDEX = "Index";
    public static final String KEY_KIDS = "Kids";
    public static final String KEY_ACTION = "Action";
    public static final String VALUE_ACTION_GOTO = "GoTo";
    public static final String VALUE_PAGE_FIT = " Fit";
    public static final String VALUE_PAGE_XYZ_FORM = " XYZ 0 0 0";

    public static String formatPageFit(int pageNum){
        return String.format("%d Fit", pageNum);
    }

    public static String formatPageXYZ(int pageNum, int pageY){
        return String.format("%d XYZ 0 %d 0", pageNum, pageY);
    }
}
