package com.huiyh.pdfkit.lowagie;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.huiyh.pdfkit.lowagie.BookmarkConst.*;
import static com.huiyh.pdfkit.lowagie.BookmarkConst.KEY_PAGE;

public class BookmarkHelper2 {
    private static int line = -1;
    private List<HashMap<String,Object>> data = null;

    public static void setOutlines(String oldFile, String newFile, List<HashMap<String ,Object>> data) {
        try {
            //create a reader for a certain document
            PdfReader reader = new PdfReader(oldFile);

            // we create a stamper that will copy the document to a new file
            PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(newFile));
            
            //read the outlines structure file
//            readOutlinesFile(outlinesFile);
//            this.data = data;

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

    public List createOutlines() {
        List list = new ArrayList();

        HashMap map = null;
        int lastLine = line + 1;

        do {
            line++;

            map = new HashMap();
            map.put("Title", ((Map) data.get(line)).get("Title"));
            map.put("Action", "GoTo");
            map.put("Page", ((Map) data.get(line)).get("Page"));

            if (hasChildren(line)) {
                map.put("Kids", createOutlines());
            }

            list.add(map);
        } while (hasSubling(lastLine));

        return list;
    }

    /*
*List item:
*String[] = {INDEX, PAGE, TITLE}
*/
    private void readOutlinesFile(String file) {
        HashMap map = null;
        FileReader fileReader = null;
        BufferedReader reader = null;
        List<HashMap<String,Object>> lines = new ArrayList();
        try {
            fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);

            String line = null;
            String subStr = null;
            int tabIndex = 0;
            int spaceIndex = 0;

            while ((line = reader.readLine()) != null) {
                map = new HashMap();

                tabIndex = line.indexOf("/t");
                tabIndex = (tabIndex >= 0) ? tabIndex : Integer.MAX_VALUE;
                spaceIndex = line.indexOf(" ");
                spaceIndex = (spaceIndex >= 0) ? spaceIndex : Integer.MAX_VALUE;
                subStr = line.substring(0, tabIndex < spaceIndex ? tabIndex : spaceIndex);
                map.put("Index", subStr);
                line = line.substring(subStr.length() + 1).trim();

                tabIndex = line.indexOf("/t");
                tabIndex = (tabIndex >= 0) ? tabIndex : Integer.MAX_VALUE;
                spaceIndex = line.indexOf(" ");
                spaceIndex = (spaceIndex >= 0) ? spaceIndex : Integer.MAX_VALUE;
                subStr = line.substring(0, tabIndex < spaceIndex ? tabIndex : spaceIndex);
                map.put("Page", subStr);

                map.put("Title", line.substring(subStr.length() + 1).trim());

                lines.add(map);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileReader != null){
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        this.data = lines;
    }

    private boolean hasSubling(int lastLine) {
        if (line >= data.size() - 1) return false;

        String lastIndexStr = (String)data.get(lastLine).get("Index");
        String currIndexStr = (String)data.get(line + 1).get("Index");

        return (lastIndexStr.length() == currIndexStr.length());
    }

    private boolean hasChildren(int line) {
        if (line >= data.size() - 1) return false;

        String currIndexStr = (String)data.get(line).get("Index");
        String nextIndexStr = (String)data.get(line + 1).get("Index");

        if (line + 1 < data.size() && currIndexStr.length() < nextIndexStr.length())
            return true;

        return false;
    }

    public static HashMap<String, Object> createMarkItem(String title, int pageNum) {
        HashMap<String, Object> item = new HashMap<>();
        item.put(KEY_ACTION, VALUE_ACTION_GOTO);
        item.put(KEY_TITLE, title);
        String page = pageNum + VALUE_PAGE_FIT;
        item.put(KEY_PAGE, page);
        return item;
    }

}