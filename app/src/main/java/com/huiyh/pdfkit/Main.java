package com.huiyh.pdfkit;

import com.google.gson.Gson;
import com.huiyh.common.IOUtils;
import com.huiyh.common.NumberUtils;
import com.huiyh.pdfkit.lowagie.BookmarkHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.huiyh.pdfkit.lowagie.BookmarkConst.*;

public class Main {

    public static final String DATA_STR = "";
    /**
     * 页码偏移量
     */
    public static final int PAGE_INDEX_OFFSET = 13;
    // 读入pdf文件
    public static final String fileName = "/Users/HYH/Desktop/哈佛大学经济课.pdf";
    public static final int PAGE_Y = 650;
    //    public static final String fileName = "/Users/HYH/Documents/Books/图书-技术类/Web/React快速上手开发.pdf";

    public static void main(String[] args) throws IOException {

//        setBookmarksFromLinesFile(fileName, "/Users/HYH/Desktop/哈佛大学经济课.txt");
//        outputBookmarks(fileName + ".bookmark.pdf");
        setBookmarksFromJsonFile(fileName, "/Users/HYH/Desktop/哈佛大学经济课.json");

    }

    private static void outputBookmarks(String fileName) throws IOException {
        PdfReader reader = new PdfReader(fileName);
        List<HashMap<String, Object>> bookmarks = SimpleBookmark.getBookmark(reader);

        Gson gson = new Gson();
        System.out.println(gson.toJson(bookmarks));
    }

    public static void setBookmarksFromLinesStr(String fileName, String bookmarks){
        String[] datas = DATA_STR.split("\n");
        List<String> markLines = Arrays.asList(datas);
        setBookmarksFromLines(fileName, markLines);
    }

    public static void setBookmarksFromLinesFile(String fileName, String bookmarksFileName){
        List<String> markLines = IOUtils.readLines(new File(bookmarksFileName));
        setBookmarksFromLines(fileName, markLines);
    }

    private static void setBookmarksFromLines(String fileName, List<String> markLines) {
        List<Map<String, Object>> bookmarks = new ArrayList<>() ;
        for(String title : markLines) {
            if (title == null || title.trim().length() == 0){
                continue;
            }
            int pageNum = getPageNumFromTitleEnd(title);
            Map<String, Object> markItem = BookmarkHelper.createXYZMarkItem(title, pageNum + PAGE_INDEX_OFFSET, PAGE_Y);
            bookmarks.add(markItem);
        }

        BookmarkHelper helper = new BookmarkHelper();
        helper.setOutlines(fileName,getDestFileName(fileName),bookmarks);
    }

    private static int getPageNumFromTitleEnd(String title) {
        System.out.println(title);
        String pageStr = title.trim().substring(title.lastIndexOf(" ")).trim();
        return NumberUtils.parseInt(pageStr, 1);
    }

    public static void setBookmarksFromJsonFile(String fileName, String bookmarksFileName){
        List<Map<String, Object>> maps = BookmarkHelper.readOutlinesJsonFile(bookmarksFileName);
        BookmarkHelper.setOutlines(fileName, getDestFileName(fileName), maps);
    }

    private static String getDestFileName(String fileName) {
        return fileName + ".bookmark.pdf";
    }

    private static void changePage(List<HashMap<String, Object>> bookmarks) {
        for (HashMap<String,Object> bookmark : bookmarks){
            String page = (String) bookmark.get(KEY_PAGE);
            if(page != null && page.length() > 0){
                String pageNum = page.substring(0, page.indexOf(" "));
                bookmark.put(KEY_PAGE,pageNum + VALUE_PAGE_FIT);
                System.out.println(bookmark.get(KEY_TITLE) + " " + bookmark.get(KEY_PAGE));
            }
            List<HashMap<String, Object>> kids = (List<HashMap<String, Object>>) bookmark.get(KEY_KIDS);
            if(kids != null && kids.size() > 0){
                changePage(kids);
            }
        }
    }
    private static void resetPageNum(List<Map<String, Object>> bookmarks) {
        for (Map<String,Object> bookmark : bookmarks){
            String title = (String) bookmark.get(KEY_TITLE);
            if (title.contains(" ")){
                int pageNumFromTitleEnd = getPageNumFromTitleEnd(title);
                String page = (String) bookmark.get(KEY_PAGE);
                if(page != null && page.length() > 0){
                    bookmark.put(KEY_PAGE,(pageNumFromTitleEnd + PAGE_INDEX_OFFSET) + page.substring(page.indexOf(" ")));
                    System.out.println(bookmark.get(KEY_TITLE) + " " + bookmark.get(KEY_PAGE));
                }
            }
            List<Map<String, Object>> kids = (List<Map<String, Object>>) bookmark.get(KEY_KIDS);
            if(kids != null && kids.size() > 0){
                resetPageNum(kids);
            }
        }

        System.out.println("#################################");
        System.out.println(new Gson().toJson(bookmarks));
    }
}
