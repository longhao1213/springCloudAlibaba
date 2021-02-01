package com.video.poi;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (C), 2006-2010, ChengDu ybya info. Co., Ltd.
 * FileName: Test.java
 *
 * @author lh
 * @version 1.0.0
 * @Date 2021/02/01 10:32
 */
public class Test {

    /**
     * 测试生成表格和图表
     * @param args
     */
    public static void main(String[] args){
        try (XWPFDocument document = new XWPFDocument(new FileInputStream("D:\\FreeMarker.docx"))) {
            // 模拟点数据
            Map<String, String> paragraphMap = new HashMap<>();
            Map<String, String> tableMap = new HashMap<>();
            List<String[]> familyList = new ArrayList<>();
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
            ExportWordUtil.changeParagraphText(document,paragraphMap);
            ExportWordUtil.changeTableText(document, tableMap);
            ExportWordUtil.copyHeaderInsertText(document, familyList , 7);

            // 创建源数据
            // 底部数据栏
            String[] categories = new String[] { "Lang 1", "Lang 2", "Lang 3" };
            // 柱状图数据来源
            Double[] valuesA = new Double[] { 10d, 20d, 30d };
            Double[] valuesB = new Double[] { 15d, 25d, 35d };
            Double[] valuesC = new Double[] { 10d, 8d, 20d };

            // 创建一个柱状图
            XWPFChart chart = document.createChart(15 * Units.EMU_PER_CENTIMETER, 10 * Units.EMU_PER_CENTIMETER);

            // 处理对应的数据
            int numOfPoints = categories.length;
            String categoryDataRange = chart.formatRange(new CellRangeAddress(1, numOfPoints, 0, 0));
            String valuesDataRangeA = chart.formatRange(new CellRangeAddress(1, numOfPoints, 1, 1));
            String valuesDataRangeB = chart.formatRange(new CellRangeAddress(1, numOfPoints, 2, 2));
            String valuesDataRangeC = chart.formatRange(new CellRangeAddress(1, numOfPoints, 2, 2));
            XDDFDataSource<String> categoriesData = XDDFDataSourcesFactory.fromArray(categories, categoryDataRange, 0);

            XDDFNumericalDataSource<Double> valuesDataA = XDDFDataSourcesFactory.fromArray(valuesA, valuesDataRangeA, 1);
            XDDFNumericalDataSource<Double> valuesDataB = XDDFDataSourcesFactory.fromArray(valuesB, valuesDataRangeB, 2);
            XDDFNumericalDataSource<Double> valuesDataC = XDDFDataSourcesFactory.fromArray(valuesC, valuesDataRangeC, 2);

            // 创建一些轴
            XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
            XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
            leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
            // Set AxisCrossBetween, so the left axis crosses the category axis between the categories.
            // Else first and last category is exactly on cross points and the bars are only half visible.
            leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);

            // 创建柱状图的类型
            XDDFChartData data = chart.createData(ChartTypes.BAR, bottomAxis, leftAxis);
            // 指定为堆叠柱状图
            ((XDDFBarChartData) data).setBarGrouping(BarGrouping.STACKED);
            ((XDDFBarChartData) data).setBarDirection(BarDirection.COL);

            // 创建每一个柱子上的类容 若有多个 循环添加
            // if only one series do not vary colors for each bar
            ((XDDFBarChartData) data).setVaryColors(true);
            XDDFChartData.Series series = data.addSeries(categoriesData, valuesDataA);
            XDDFChartData.Series series2 = data.addSeries(categoriesData, valuesDataB);
            XDDFChartData.Series series3 = data.addSeries(categoriesData, valuesDataC);
            series.setTitle("a", setTitleInDataSheet(chart, "a", 1));
            series2.setTitle("b", setTitleInDataSheet(chart, "b", 2));
            series3.setTitle("c", setTitleInDataSheet(chart, "c", 3));


            // plot chart data
            // 设置同一个柱子上的图形偏移量为0
            chart.getCTChart().getPlotArea().getBarChartArray(0).addNewOverlap().setVal((byte) 100);
            // 绘制图形数据
            chart.plot(data);

            // create legend
            XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(LegendPosition.LEFT);
            legend.setOverlay(false);

            // Write the output to a file
            try (FileOutputStream fileOut = new FileOutputStream("CreateWordXDDFChart.docx")) {
                document.write(fileOut);
            }
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static CellReference setTitleInDataSheet(XWPFChart chart, String title, int column) throws Exception {
        XSSFWorkbook workbook = chart.getWorkbook();
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row = sheet.getRow(0);
        if (row == null)
            row = sheet.createRow(0);
        XSSFCell cell = row.getCell(column);
        if (cell == null)
            cell = row.createCell(column);
        cell.setCellValue(title);
        return new CellReference(sheet.getSheetName(), 0, column, true, true);
    }
}