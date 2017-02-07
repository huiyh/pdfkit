package com.huiyh.pdfkit;

import com.huiyh.pdfkit.lowagie.BookmarkHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.huiyh.pdfkit.lowagie.BookmarkConst.*;

public class Main {

    public static final String DATA_STR = "";



    public static void main(String[] args) throws IOException {
        // write your code here
        // 读入pdf文件
        String fileName = "C:\\Users\\huiyh\\Desktop\\新建文件夹\\批判性思维[美]理查德.保罗.pdf";
        String destName = fileName + ".bookmark.pdf";
//        PdfReader reader = new PdfReader(fileName);
//        List<HashMap<String, Object>> bookmarks = SimpleBookmark.getBookmark(reader);
        List<HashMap<String, Object>> bookmarks = new ArrayList<>() ;

//        Gson gson = new Gson();
//        System.out.println(gson.toJson(bookmarks));
//        changePage(bookmarks);
//        for (HashMap<String,Object> bookmark : bookmarks){
//            String title = (String) bookmark.get(KEY_TITLE);
//            System.out.printf(title);
//            if(title.startsWith("第") && title.contains("章　")){
//                String replace = title.substring(1, title.length()).replace("章　", ". ");
//                bookmark.put(KEY_TITLE,replace);
//                System.out.println(" --> " + replace);
//            }
//        }
//        System.out.println(gson.toJson(bookmarks));
        String[] datas = DATA_STR.split("\n");
        for(String dataStr : datas) {
            String[] split = dataStr.split(" ");
            String s = split[split.length - 1];
            try {
                int i = Integer.parseInt(s);
                HashMap<String, Object> markItem = BookmarkHelper.createMarkItem(dataStr, i + 13);
                bookmarks.add(markItem);
            } catch (Exception e){
                e.printStackTrace();
                HashMap<String, Object> markItem = BookmarkHelper.createMarkItem(dataStr, 1);
                bookmarks.add(markItem);
            }
        };
        BookmarkHelper helper = new BookmarkHelper();
        helper.setOutlines(fileName,destName,bookmarks);
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
}
