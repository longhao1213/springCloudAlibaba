package com.video.poi;

import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright (C), 2006-2010, ChengDu ybya info. Co., Ltd.
 * FileName: TestPoiTable.java
 *
 * @author lh
 * @version 1.0.0
 * @Date 2021/02/01 09:44
 */
public class TestPoiTable {
    private final static Logger logger = LoggerFactory.getLogger(TestPoiTable.class);

    /**
     * 用word 生成表格
     *
     * @param args
     */
    public static void main(String[] args) {
        // 模拟点数据
        Map<String, String> paragraphMap = new HashMap<>();
        Map<String, String> tableMap = new HashMap<>();
        List<Map<Integer, List<String[]>>> list = new ArrayList<>();
        Map<Integer, List<String[]>> familyMap = new ConcurrentHashMap();
        Map<Integer, List<String[]>> familyMap2 = new ConcurrentHashMap();
        List<String[]> familyList = new ArrayList<>();
        List<String[]> familyList2 = new ArrayList<>();
        paragraphMap.put("number", "10000");
        paragraphMap.put("date", "2020-03-25");
        tableMap.put("name", "赵云");
        tableMap.put("sexual", "男");
        tableMap.put("birthday", "2020-01-01");
        tableMap.put("identify", "123456789");
        tableMap.put("phone", "18377776666");
        tableMap.put("address", "王者荣耀");
        tableMap.put("domicile", "中国-腾讯");
        tableMap.put("QQ", "是");
        tableMap.put("chat", "是");
        tableMap.put("blog", "是");
        familyList.add(new String[]{"露娜", "女", "野友", "666", "6660"});
        familyList.add(new String[]{"鲁班", "男", "射友", "222", "2220"});
        familyList.add(new String[]{"程咬金", "男", "肉友", "999", "9990"});
        familyList.add(new String[]{"太乙真人", "男", "辅友", "111", "1110"});
        familyList.add(new String[]{"貂蝉", "女", "法友", "888", "8880"});
        familyMap.put(7, familyList);
        list.add(familyMap);
        familyList2.add(new String[]{"18581588710", "蜘蛛侠", "100"});
        familyList2.add(new String[]{"18581588710", "战神", "200"});
        familyMap2.put(0, familyList2);
        list.add(familyMap2);
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//        String templatePath = this.getClass().getClassLoader().getResource("static/template/person.docx").getPath();
        XWPFDocument document = null;
        try {
            //解析docx模板并获取document对象
            document = new XWPFDocument(new FileInputStream("D:\\FreeMarker.docx"));

            ExportWordUtil.changeParagraphText(document, paragraphMap);
            ExportWordUtil.changeTableText(document, tableMap);
//            ExportWordUtil.copyHeaderInsertText(document, familyList, 7);
            ExportWordUtil.copyHeaderInsertText2(document, list);

//            document.write(byteOut);
            try (FileOutputStream fileOut = new FileOutputStream("CreateWordXDDFChart.docx")) {
                document.write(fileOut);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class ExportWordUtil {
    private ExportWordUtil() {
    }

    /**
     * 替换文档中段落文本
     *
     * @param document docx解析对象
     * @param textMap  需要替换的信息集合
     */
    public static void changeParagraphText(XWPFDocument document, Map<String, String> textMap) {
        //获取段落集合
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            //判断此段落时候需要进行替换
            String text = paragraph.getText();
            if (checkText(text)) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    //替换模板原来位置
                    run.setText(changeValue(run.toString(), textMap), 0);
                }
            }
        }
    }

    /**
     * 复制表头 插入行数据，这里样式和表头一样
     *
     * @param document    docx解析对象
     * @param tableList   需要插入数据集合
     * @param headerIndex 表头的行索引，从0开始
     */
    public static void copyHeaderInsertText(XWPFDocument document, List<String[]> tableList, int headerIndex) {
        if (null == tableList) {
            return;
        }
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (XWPFTable table : tables) {
            XWPFTableRow copyRow = table.getRow(headerIndex);
            List<XWPFTableCell> cellList = copyRow.getTableCells();
            if (null == cellList) {
                break;
            }
            //遍历要添加的数据的list
            for (int i = 0; i < tableList.size(); i++) {
                //插入一行
                XWPFTableRow targetRow = table.insertNewTableRow(headerIndex + 1 + i);
                //复制行属性
                targetRow.getCtRow().setTrPr(copyRow.getCtRow().getTrPr());

                String[] strings = tableList.get(i);
                for (int j = 0; j < strings.length; j++) {
                    XWPFTableCell sourceCell = cellList.get(j);
                    //插入一个单元格
                    XWPFTableCell targetCell = targetRow.addNewTableCell();
                    //复制列属性
                    targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
                    targetCell.setText(strings[j]);
                }
            }
        }
    }

    /**
     * 复制表头 插入行数据，这里样式和表头一样
     *
     * @param document    docx解析对象
     * @param tableList   需要插入数据集合
     * @param headerIndex 表头的行索引，从0开始
     */
    public static void copyHeaderInsertText2(XWPFDocument document, List<Map<Integer, List<String[]>>> list) {


        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (int k = 0; k < tables.size(); k++) {
            XWPFTable table = tables.get(k);
            Map<Integer, List<String[]>> map = list.get(k);
            Integer headerIndex = 0;
            List<String[]> tableList = null;
            Iterator iter = map.keySet().iterator();
            while (iter.hasNext()) {
                headerIndex = (Integer) iter.next();
             tableList = map.get(headerIndex);
            }
//            integerListMap.forEach((key, value) -> {
//                headerIndex = key;
//                tableList = value;
//            });
            if (null == tableList) {
                return;
            }
//        }
//        for (XWPFTable table : tables) {
            XWPFTableRow copyRow = table.getRow(headerIndex);
            List<XWPFTableCell> cellList = copyRow.getTableCells();
            if (null == cellList) {
                break;
            }
            //遍历要添加的数据的list
            for (int i = 0; i < tableList.size(); i++) {
                //插入一行
                XWPFTableRow targetRow = table.insertNewTableRow(headerIndex + 1 + i);
                //复制行属性
                targetRow.getCtRow().setTrPr(copyRow.getCtRow().getTrPr());

                String[] strings = tableList.get(i);
                for (int j = 0; j < strings.length; j++) {
                    XWPFTableCell sourceCell = cellList.get(j);
                    //插入一个单元格
                    XWPFTableCell targetCell = targetRow.addNewTableCell();
                    //复制列属性
                    targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
                    targetCell.setText(strings[j]);
                }
            }
        }
    }

    /**
     * 替换表格对象方法
     *
     * @param document docx解析对象
     * @param textMap  需要替换的信息集合
     */
    public static void changeTableText(XWPFDocument document, Map<String, String> textMap) {
        //获取表格对象集合
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < tables.size(); i++) {
            //只处理行数大于等于2的表格
            XWPFTable table = tables.get(i);
            if (table.getRows().size() > 1) {
                //判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
                if (checkText(table.getText())) {
                    List<XWPFTableRow> rows = table.getRows();
                    //遍历表格,并替换模板
                    eachTable(rows, textMap);
                }
            }
        }
    }

    /**
     * 遍历表格,并替换模板
     *
     * @param rows    表格行对象
     * @param textMap 需要替换的信息集合
     */
    public static void eachTable(List<XWPFTableRow> rows, Map<String, String> textMap) {
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                //判断单元格是否需要替换
                if (checkText(cell.getText())) {
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) {
                            run.setText(changeValue(run.toString(), textMap), 0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 匹配传入信息集合与模板
     *
     * @param value   模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static String changeValue(String value, Map<String, String> textMap) {
        Set<Map.Entry<String, String>> textSets = textMap.entrySet();
        for (Map.Entry<String, String> textSet : textSets) {
            //匹配模板与替换值 格式${key}
            String key = "${" + textSet.getKey() + "}";
            if (value.indexOf(key) != -1) {
                value = textSet.getValue();
            }
        }
        //模板未匹配到区域替换为空
        if (checkText(value)) {
            value = "";
        }
        return value;
    }

    /**
     * 判断文本中时候包含$
     *
     * @param text 文本
     * @return 包含返回true, 不包含返回false
     */
    public static boolean checkText(String text) {
        boolean check = false;
        if (text.indexOf("$") != -1) {
            check = true;
        }
        return check;
    }

}