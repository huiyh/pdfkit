package com.huiyh.pdfkit;

import com.google.gson.Gson;
import com.huiyh.pdfkit.lowagie.BookmarkHelper2;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static final String PAGE = "Page";
    public static final String TITLE = "Title";
    public static final String KIDS = "Kids";

    public static void main(String[] args) throws IOException {
        // write your code here
        // 读入pdf文件
        String fileName = "D:/图书(技术类)/C & C++/C++ Primer 第4版 中文版.pdf";
        String destName = "C:/Users/huiyh/Desktop/C++ Primer 第4版 中文版.bookmark.pdf";
        PdfReader reader = new PdfReader(fileName);
        List<HashMap<String, Object>> bookmarks = SimpleBookmark.getBookmark(reader);

        Gson gson = new Gson();
        System.out.println(gson.toJson(bookmarks));
        changePage(bookmarks);
        for (HashMap<String,Object> bookmark : bookmarks){
            String title = (String) bookmark.get(TITLE);
            System.out.printf(title);
            if(title.startsWith("第") && title.contains("章　")){
                String replace = title.substring(1, title.length()).replace("章　", ". ");
                bookmark.put(TITLE,replace);
                System.out.println(" --> " + replace);
            }
        }
        System.out.println(gson.toJson(bookmarks));
        BookmarkHelper2 helper = new BookmarkHelper2();
        helper.setOutlines(fileName,destName,bookmarks);
    }

    private static void changePage(List<HashMap<String, Object>> bookmarks) {
        for (HashMap<String,Object> bookmark : bookmarks){
            String page = (String) bookmark.get(PAGE);
            if(page != null && page.length() > 0){
                String pageNum = page.substring(0, page.indexOf(" "));
                bookmark.put(PAGE,pageNum + " XYZ 0 2040 0.0");
                System.out.println(bookmark.get(TITLE) + " " + bookmark.get(PAGE));
            }
            List<HashMap<String, Object>> kids = (List<HashMap<String, Object>>) bookmark.get(KIDS);
            if(kids != null && kids.size() > 0){
                changePage(kids);
            }
        }
    }
}