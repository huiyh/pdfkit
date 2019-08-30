package com.huiyh.pdfkit;

import com.google.gson.annotations.SerializedName;
import com.huiyh.pdfkit.lowagie.BookmarkCreater;
import com.huiyh.pdfkit.lowagie.BookmarkHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.huiyh.pdfkit.lowagie.BookmarkConst.KEY_ACTION;
import static com.huiyh.pdfkit.lowagie.BookmarkConst.KEY_KIDS;
import static com.huiyh.pdfkit.lowagie.BookmarkConst.KEY_PAGE;
import static com.huiyh.pdfkit.lowagie.BookmarkConst.KEY_TITLE;
import static com.huiyh.pdfkit.lowagie.BookmarkConst.VALUE_ACTION_GOTO;
import static com.huiyh.pdfkit.lowagie.BookmarkConst.VALUE_PAGE_FIT;

/**
 * @author : 惠远航
 * @date : 2019-03-04 16:07
 */
public class Bookmark {

    /**
     * Action : GoTo
     * Title : www.PacktPub.com
     * Page : 13 XYZ 72 792 null
     * Kids : [{"Action":"GoTo","Title":"Support files, eBooks, discount offers, and more","Page":"13 XYZ 72 677 null","Kids":[{"Action":"GoTo","Title":"Why subscribe?","Page":"14 XYZ 72 720 null"},{"Action":"GoTo","Title":"Free access for Packt account holders","Page":"15 XYZ 72 720 null"}]}]
     */

    @SerializedName("Action")
    private String Action;
    @SerializedName("Title")
    private String Title;
    @SerializedName("Page")
    private String Page;
    @SerializedName("Kids")
    private List<Bookmark> Kids;

    public String getAction() {
        return Action;
    }

    public void setAction(String Action) {
        this.Action = Action;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getPage() {
        return Page;
    }

    public void setPage(String Page) {
        this.Page = Page;
    }

    public List<Bookmark> getKids() {
        return Kids;
    }

    public void setKids(List<Bookmark> Kids) {
        this.Kids = Kids;
    }

    public Map<String, Object> asOutline() {
        HashMap<String, Object> item = new HashMap<>();
        item.put(KEY_ACTION, VALUE_ACTION_GOTO);
        item.put(KEY_TITLE, Title);
        item.put(KEY_PAGE, Page);
        if (Kids != null && !Kids.isEmpty()){
            List<Map<String, Object>> bookmarks = new ArrayList<>();
            for (Bookmark bookmark : Kids){
                bookmarks.add(bookmark.asOutline());
            }
            item.put(KEY_KIDS, bookmarks);
        }
        return item;
    }
}
