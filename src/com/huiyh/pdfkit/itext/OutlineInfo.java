package com.huiyh.pdfkit.itext;

import java.util.HashMap;
import java.util.List;

/**
 * Created by huiyh on 2016/2/24.
 */
public class OutlineInfo {
    private List<HashMap<String, Object>> outlines;
    private HashMap<String, Object> preOutline;
    private int preLevel;

    public void setOutlines(List<HashMap<String, Object>> outlines) {
        this.outlines = outlines;
    }

    public void setPreOutline(HashMap<String, Object> preOutline) {
        this.preOutline = preOutline;
    }

    public void setPreLevel(int preLevel) {
        this.preLevel = preLevel;
    }

    public OutlineInfo(HashMap<String, Object> preOutline, List<HashMap<String, Object>> outlines, int preLevel) {

    }

    public List<HashMap<String, Object>> getOutlines() {
        return outlines;
    }

    public HashMap<String, Object> getPreOutline() {
        return preOutline;
    }

    public int getPreLevel() {
        return preLevel;
    }
}
