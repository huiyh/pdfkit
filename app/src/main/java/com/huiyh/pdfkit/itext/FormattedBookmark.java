package com.huiyh.pdfkit.itext;

/**
 * Created by huiyh on 2016/2/24.
 */
public class FormattedBookmark {
    private int level;
    private Object title;
    private String page;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Object getTitle() {
        return title;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
