package com.huiyh.pdfkit.lowagie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.huiyh.pdfkit.lowagie.BookmarkConst.*;

public class BookmarkCreater {
    private static int line = -1;

    public void setOutlines(String oldFile, String newFile, String outlinesFile) {
        //read the outlines structure file
        List<Map> data = BookmarkHelper.readOutlinesFile(outlinesFile);
        //set the outlines
        List outlines = createOutlines(data);

        BookmarkHelper.setOutlines(oldFile, newFile, outlines);
    }

    public List createOutlines(List<Map> data) {
        List list = new ArrayList();

        HashMap map = null;
        int lastLine = line + 1;

        do {
            line++;

            map = new HashMap();
            map.put(KEY_TITLE, (data.get(line)).get(KEY_TITLE));
            map.put(KEY_ACTION, VALUE_ACTION_GOTO);
            map.put(KEY_PAGE, (data.get(line)).get(KEY_PAGE));

            if (hasChildren(data, line)) {
                map.put(KEY_KIDS, createOutlines(data));
            }

            list.add(map);
        } while (hasSubling(data, lastLine));

        return list;
    }


    private boolean hasSubling(List<Map> data, int lastLine) {
        if (line >= data.size() - 1) {
            return false;
        }

        String lastIndexStr = (String) data.get(lastLine).get(KEY_INDEX);
        String currIndexStr = (String) data.get(line + 1).get(KEY_INDEX);

        return (lastIndexStr.length() == currIndexStr.length());
    }

    private boolean hasChildren(List<Map> data, int line) {
        if (line >= data.size() - 1) {
            return false;
        }

        String currIndexStr = (String) data.get(line).get(KEY_INDEX);
        String nextIndexStr = (String) data.get(line + 1).get(KEY_INDEX);

        if (line + 1 < data.size() && currIndexStr.length() < nextIndexStr.length()) {
            return true;
        }
        return false;
    }
}