package com.huiyh.pdfkit.lowagie;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huiyh.util.IOUtils;
import com.huiyh.pdfkit.Bookmark;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;


import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.huiyh.pdfkit.lowagie.BookmarkConst.*;

public class BookmarkHelper {


    public static void setOutlines(String oldFile, String newFile, List<HashMap<String ,Object>> data) {
        try {
            //create a reader for a certain document
            PdfReader reader = new PdfReader(oldFile);

            // we create a stamper that will copy the document to a new file
            PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(newFile));

            //set the outlines
            stamp.setOutlines(data);

            // closing PdfStamper will generate the new PDF file
            stamp.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    public static Map<String, Object> createFitMarkItem(String title, int pageNum) {
        HashMap<String, Object> item = new HashMap<>();
        item.put(KEY_ACTION, VALUE_ACTION_GOTO);
        item.put(KEY_TITLE, title);
        String page = BookmarkConst.formatPageFit(pageNum);
        item.put(KEY_PAGE, page);
        return item;
    }

    public static HashMap<String, Object> createXYZMarkItem(String title, int pageNum, int pageY) {
        HashMap<String, Object> item = new HashMap<>();
        item.put(KEY_ACTION, VALUE_ACTION_GOTO);
        item.put(KEY_TITLE, title);
        String page = BookmarkConst.formatPageXYZ(pageNum, pageY);
        item.put(KEY_PAGE, page);
        return item;
    }

    public static Map<String, Object> createFitMarkItem(String title, int pageNum, List<Map<String, Object>> kids) {
        HashMap<String, Object> item = new HashMap<>();
        item.put(KEY_ACTION, VALUE_ACTION_GOTO);
        item.put(KEY_TITLE, title);
        String page = BookmarkConst.formatPageFit(pageNum);
        item.put(KEY_PAGE, page);
        if (kids != null && !kids.isEmpty()){
            item.put(KEY_KIDS, kids);
        }
        return item;
    }

    public static Map<String, Object> createXYZMarkItem(String title, int pageNum, List<Map<String, Object>> kids, int pageY) {
        HashMap<String, Object> item = new HashMap<>();
        item.put(KEY_ACTION, VALUE_ACTION_GOTO);
        item.put(KEY_TITLE, title);
        String page = BookmarkConst.formatPageXYZ(pageNum, pageY);
        item.put(KEY_PAGE, page);
        if (kids != null && !kids.isEmpty()){
            item.put(KEY_KIDS, kids);
        }
        return item;
    }

    /*
     * List item:
     * String[] = {INDEX, PAGE, TITLE}
     */
    public static List<Map> readOutlinesFile(String file) {
        FileReader fileReader = null;
        BufferedReader reader = null;
        List<Map> lines = null;

        try {
            fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);
            lines = new ArrayList();

            String line = null;
            String subStr = null;
            Map map = null;
            int tabIndex = 0;
            int spaceIndex = 0;

            while ((line = reader.readLine()) != null) {
                map = new HashMap();

                tabIndex = line.indexOf("/t");
                tabIndex = (tabIndex >= 0) ? tabIndex : Integer.MAX_VALUE;
                spaceIndex = line.indexOf(" ");
                spaceIndex = (spaceIndex >= 0) ? spaceIndex : Integer.MAX_VALUE;
                subStr = line.substring(0, tabIndex < spaceIndex ? tabIndex : spaceIndex);
                map.put(KEY_INDEX, subStr);
                line = line.substring(subStr.length() + 1).trim();

                tabIndex = line.indexOf("/t");
                tabIndex = (tabIndex >= 0) ? tabIndex : Integer.MAX_VALUE;
                spaceIndex = line.indexOf(" ");
                spaceIndex = (spaceIndex >= 0) ? spaceIndex : Integer.MAX_VALUE;
                subStr = line.substring(0, tabIndex < spaceIndex ? tabIndex : spaceIndex);
                map.put(KEY_PAGE, subStr);

                map.put(KEY_TITLE, line.substring(subStr.length() + 1).trim());

                lines.add(map);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.close(reader);
            IOUtils.close(fileReader);
        }

        return lines;
    }
    public static List<HashMap<String, Object>> readOutlinesJsonFile(String file) {
        FileReader fileReader = null;
        BufferedReader reader = null;
        List<HashMap<String, Object>> lines = new ArrayList();

        try {
            fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);
            StringBuilder builder = new StringBuilder();


            String line = null;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            Type type = new TypeToken<List<Bookmark>>() {
            }.getType();
            List<Bookmark> bookmarks = new Gson().fromJson(builder.toString(), type);
            if (bookmarks != null && !bookmarks.isEmpty()){
                for (Bookmark bookmark : bookmarks){

                    lines.add((HashMap<String, Object>) bookmark.asOutline());
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.close(reader);
            IOUtils.close(fileReader);
        }
        return lines;
    }
}
