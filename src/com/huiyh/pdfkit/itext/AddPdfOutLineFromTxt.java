package com.huiyh.pdfkit.itext;

/**
 * Created by huiyh on 2016/2/24.
 */

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.IntHashtable;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.SimpleBookmark;

public class AddPdfOutLineFromTxt {
    private Stack<OutlineInfo> parentOutlineStack = new Stack<OutlineInfo>();

    public void createPdf(String destPdf, String sourcePdf, BufferedReader bufRead, int pattern) throws IOException, DocumentException {

        if (pattern != AddBookmarkConstants.RESERVED_OLD_OUTLINE
                && pattern != AddBookmarkConstants.RESERVED_NONE
                && pattern != AddBookmarkConstants.RESERVED_FIRST_OUTLINE)
            return;
        // 读入pdf文件
        PdfReader reader = new PdfReader(sourcePdf);

        List<HashMap<String, Object>> outlines = new ArrayList<HashMap<String, Object>>();
        if (pattern == AddBookmarkConstants.RESERVED_OLD_OUTLINE) {
            outlines.addAll(SimpleBookmark.getBookmark(reader));
        } else if (pattern == AddBookmarkConstants.RESERVED_FIRST_OUTLINE) {
            addFirstOutlineReservedPdf(outlines, reader);
        }

        addBookmarks(bufRead, outlines, null, 0);
        // 新建stamper
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                destPdf));

        stamper.setOutlines(outlines);
        stamper.close();
    }

    private void addBookmarks(BufferedReader bufRead,
                              List<HashMap<String, Object>> outlines,
                              HashMap<String, Object> preOutline, int preLevel) throws IOException {
        String contentFormatLine = null;
        bufRead.mark(1);
        if ((contentFormatLine = bufRead.readLine()) != null) {
            FormattedBookmark bookmark = parseFormmattedText(contentFormatLine);

            HashMap<String, Object> map = parseBookmarkToHashMap(bookmark);

            int level = bookmark.getLevel();
            // 如果n==m, 那么是同一层的，这个时候，就加到ArrayList中,继续往下面读取
            if (level == preLevel) {
                outlines.add(map);
                addBookmarks(bufRead, outlines, map, level);
            }
            // 如果n>m,那么可以肯定，该行是上一行的孩子，, new 一个kids的arraylist,并且加入到这个arraylist中
            else if (level > preLevel) {
                List<HashMap<String, Object>> kids = new ArrayList<HashMap<String, Object>>();
                kids.add(map);
                preOutline.put("Kids", kids);
                // 记录有孩子的outline信息
                parentOutlineStack.push(new OutlineInfo(preOutline, outlines,
                        preLevel));
                addBookmarks(bufRead, kids, map, level);
            }
            // 如果n<m , 那么就是说孩子增加完了，退回到上层，bufRead倒退一行
            else if (level < preLevel) {
                bufRead.reset();
                OutlineInfo obj = parentOutlineStack.pop();
                addBookmarks(bufRead, obj.getOutlines(), obj.getPreOutline(),
                        obj.getPreLevel());
            }

        }
    }

    private HashMap<String, Object> parseBookmarkToHashMap(
            FormattedBookmark bookmark) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("Title", bookmark.getTitle());
        map.put("Action", "GoTo");
        map.put("Page", bookmark.getPage() + " Fit");
        return map;
    }

    private FormattedBookmark parseFormmattedText(String contentFormatLine) {
        FormattedBookmark bookmark = new FormattedBookmark();
        String title = "";
        String destPage = "";

        // 当没有页码在字符串结尾的时候，一般就是书的名字，如果格式正确的话。
        int lastSpaceIndex = contentFormatLine.lastIndexOf(" ");
        if (lastSpaceIndex == -1) {
            title = contentFormatLine;
            destPage = "1";
        } else {
            title = contentFormatLine.substring(0, lastSpaceIndex);
            destPage = contentFormatLine.substring(lastSpaceIndex + 1);
        }

        String[] titleSplit = title.split(" ");
        int dotCount = titleSplit[0].split("\\.").length - 1;

        bookmark.setLevel(dotCount);
        bookmark.setPage(destPage);
        bookmark.setTitle(title);
        return bookmark;
    }

    private void addFirstOutlineReservedPdf(
            List<HashMap<String, Object>> outlines, PdfReader reader) {
        PdfDictionary catalog = reader.getCatalog();
        PdfObject obj = PdfReader.getPdfObjectRelease(catalog.get(PdfName.OUTLINES));
        // 没有书签
        if (obj == null || !obj.isDictionary())
            return;

        PdfDictionary outlinesDictionary = (PdfDictionary) obj;
        // 得到第一个书签
        PdfDictionary firstOutline = (PdfDictionary) PdfReader.getPdfObjectRelease(outlinesDictionary.get(PdfName.FIRST));

        PdfString titleObj = firstOutline.getAsString((PdfName.TITLE));
        String title = titleObj.toUnicodeString();

        PdfArray dest = firstOutline.getAsArray(PdfName.DEST);

        if (dest == null) {
            PdfDictionary action = (PdfDictionary) PdfReader
                    .getPdfObjectRelease(firstOutline.get(PdfName.A));
            if (action != null) {
                if (PdfName.GOTO.equals(PdfReader.getPdfObjectRelease(action
                        .get(PdfName.S)))) {
                    dest = (PdfArray) PdfReader.getPdfObjectRelease(action
                            .get(PdfName.D));
                }
            }
        }
        String destStr = parseDestString(dest, reader);

        String[] decodeStr = destStr.split(" ");
        int num = Integer.valueOf(decodeStr[0]);
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("Title", title);
        map.put("Action", "GoTo");
        map.put("Page", num + " Fit");

        outlines.add(map);
    }

    private String parseDestString(PdfArray dest, PdfReader reader) {
        String destStr = "";
        if (dest.isString()) {
            destStr = dest.toString();
        } else if (dest.isName()) {
            destStr = PdfName.decodeName(dest.toString());
        } else if (dest.isArray()) {
            IntHashtable pages = new IntHashtable();
            int numPages = reader.getNumberOfPages();
            for (int k = 1; k <= numPages; ++k) {
                pages.put(reader.getPageOrigRef(k).getNumber(), k);
                reader.releasePage(k);
            }

            destStr = makeBookmarkParam((PdfArray) dest, pages);
        }
        return destStr;
    }

    private String makeBookmarkParam(PdfArray dest, IntHashtable pages) {
        StringBuffer s = new StringBuffer();
        PdfObject obj = dest.getPdfObject(0);
        if (obj.isNumber()) {
            s.append(((PdfNumber) obj).intValue() + 1);
        } else {
            s.append(pages.get(getNumber((PdfIndirectReference) obj)));
        }
        s.append(' ').append(dest.getPdfObject(1).toString().substring(1));
        for (int k = 2; k < dest.size(); ++k) {
            s.append(' ').append(dest.getPdfObject(k).toString());
        }
        return s.toString();
    }

    private int getNumber(PdfIndirectReference indirect) {
        PdfDictionary pdfObj = (PdfDictionary) PdfReader
                .getPdfObjectRelease(indirect);
        if (pdfObj.contains(PdfName.TYPE)
                && pdfObj.get(PdfName.TYPE).equals(PdfName.PAGES)
                && pdfObj.contains(PdfName.KIDS)) {
            PdfArray kids = (PdfArray) pdfObj.get(PdfName.KIDS);
            indirect = (PdfIndirectReference) kids.getPdfObject(0);
        }
        return indirect.getNumber();
    }
}
