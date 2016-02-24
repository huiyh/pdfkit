package com.huiyh.pdfkit.lowagie;

import java.util.List;

/**
 * Created by huiyh on 2016/2/24.
 */
public class Line {
    private String title;
    private String action;
    private String page;
    private String index;
    private List<Line> kids;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<Line> getKids() {
        return kids;
    }

    public void setKids(List<Line> kids) {
        this.kids = kids;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
