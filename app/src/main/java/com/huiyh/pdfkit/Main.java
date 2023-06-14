package com.huiyh.pdfkit;

import com.google.gson.Gson;
import com.huiyh.pdfkit.lowagie.BookmarkHelper;
import com.huiyh.util.FileUtils;
import com.huiyh.util.NumberUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.huiyh.pdfkit.lowagie.BookmarkConst.*;

public class Main {

    public static final String DATA_STR = "";
    /**
     * 页码偏移量
     */
    public static final int PAGE_INDEX_OFFSET = 19;
    // 读入pdf文件
    public static final String pdfFileName = "/Users/hyh/Desktop/JavaScript完全自学手册(胡添).pdf";
    public static final String bookmarksFileName = "/Users/hyh/Desktop/JavaScript完全自学手册.txt";
    public static final int PAGE_Y = 320;

    public static void main(String[] args) throws IOException {

        setBookmarksFromLinesFile(pdfFileName, bookmarksFileName);
//        outputBookmarks(fileName);
//        setBookmarksFromJsonFile(fileName, "/Users/HYH/Desktop/哈佛大学经济课.json");
//        try {
//            manipulatePdf(new File("/Users/hyh/Downloads/Learn JavaFX 8(Apress,2015).pdf"),null);
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
    }

    private static void outputBookmarks(String fileName) throws IOException {
        PdfReader reader = new PdfReader(fileName);
        List<HashMap<String, Object>> bookmarks = SimpleBookmark.getBookmark(reader);

        Gson gson = new Gson();
        System.out.println(gson.toJson(bookmarks));
    }

//    public static void setBookmarksFromLinesStr(String fileName, String bookmarks){
//        String[] datas = DATA_STR.split("\n");
//        List<String> markLines = Arrays.asList(datas);
//        setBookmarksFromLines(fileName, markLines);
//    }

    public static void setBookmarksFromLinesFile(String fileName, String bookmarksFileName){
        List<String> markLines = FileUtils.readLines(new File(bookmarksFileName));
        setBookmarksFromLines(fileName, markLines);
    }

    private static void setBookmarksFromLines(String fileName, List<String> markLines) {
        List<HashMap<String, Object>> bookmarks = new ArrayList<>() ;
        for(String title : markLines) {
            if (title == null || title.trim().length() == 0){
                continue;
            }
            int pageNum = getPageNumFromTitleEnd(title);
            HashMap<String, Object> markItem = BookmarkHelper.createXYZMarkItem(title, pageNum + PAGE_INDEX_OFFSET, PAGE_Y);
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
        List<HashMap<String, Object>> maps = BookmarkHelper.readOutlinesJsonFile(bookmarksFileName);
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
//    private static void resetPageNum(List<Map<String, Object>> bookmarks) {
//        for (Map<String,Object> bookmark : bookmarks){
//            String title = (String) bookmark.get(KEY_TITLE);
//            if (title.contains(" ")){
//                int pageNumFromTitleEnd = getPageNumFromTitleEnd(title);
//                String page = (String) bookmark.get(KEY_PAGE);
//                if(page != null && page.length() > 0){
//                    bookmark.put(KEY_PAGE,(pageNumFromTitleEnd + PAGE_INDEX_OFFSET) + page.substring(page.indexOf(" ")));
//                    System.out.println(bookmark.get(KEY_TITLE) + " " + bookmark.get(KEY_PAGE));
//                }
//            }
//            List<Map<String, Object>> kids = (List<Map<String, Object>>) bookmark.get(KEY_KIDS);
//            if(kids != null && kids.size() > 0){
//                resetPageNum(kids);
//            }
//        }
//
//        System.out.println("#################################");
//        System.out.println(new Gson().toJson(bookmarks));
//    }

    public static void manipulatePdf(File src, File dest) throws IOException, DocumentException {
        PdfReader reader = new PdfReader(src.getAbsolutePath());
//        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
//        stamper.
        int numberOfPages = reader.getNumberOfPages();
//        for (int i = 0; i < numberOfPages; i++) {
//        }
//        PdfReaderContentParser parser = new PdfReaderContentParser(reader);
//        TextExtractionStrategy textExtractionStrategy = parser.processContent(1, new LocationTextExtractionStrategy());
        String textFromPage = PdfTextExtractor.getTextFromPage(reader, 2);
        System.out.println(textFromPage);
        PdfDictionary pdfDictionary = reader.getPageN(2);

        Set<PdfName> keys = pdfDictionary.getKeys();
        System.out.println(keys);
//        //添加一个遮挡处，可以把原内容遮住，后面在上面写入内容
////        PdfContentByte canvas = stamper.getUnderContent(1);  //不可以遮挡文字
//        PdfContentByte canvas = stamper.getOverContent(1);  //可以遮挡文字
//
//        float height=780;
//        System.out.println(canvas.getHorizontalScaling());
//        float x,y;
//        x= 116;
//        y = height -49.09F;
//        canvas.saveState();
//        canvas.setColorFill(BaseColor.YELLOW);  //遮挡层颜色：黄色
////      canvas.setColorFill(BaseColor.WHITE);  //遮挡层颜色：白色
//        canvas.rectangle(x, y-5, 43, 15);
//
//        canvas.fill();
//        canvas.restoreState();
//        //开始写入文本
//        canvas.beginText();
//        //BaseFont bf = BaseFont.createFont(URLDecoder.decode(CutAndPaste.class.getResource("/AdobeSongStd-Light.otf").getFile()), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
//        Font font = new Font(bf,10,Font.BOLD);
//        //设置字体和大小
//        canvas.setFontAndSize(font.getBaseFont(), 10);
//        //设置字体的输出位置
//        canvas.setTextMatrix(x, y);
//        //要输出的text
//        canvas.showText("我是替换" );
//
//        canvas.endText();
//        stamper.close();
//        reader.close();
//        System.out.println("complete");
    }
}
