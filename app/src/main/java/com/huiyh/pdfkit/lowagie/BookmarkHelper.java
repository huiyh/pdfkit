package com.huiyh.pdfkit.lowagie;

import com.huiyh.common.IOUtils;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import java.io.*;
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

    public static HashMap<String, Object> createMarkItem(String title, int pageNum) {
        HashMap<String, Object> item = new HashMap<>();
        item.put(KEY_ACTION, VALUE_ACTION_GOTO);
        item.put(KEY_TITLE, title);
        String page = pageNum + VALUE_PAGE_FIT;
        item.put(KEY_PAGE, page);
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

}