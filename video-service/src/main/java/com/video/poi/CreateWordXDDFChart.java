package com.video.poi;
 
import java.io.FileOutputStream;
 
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
 
public class CreateWordXDDFChart {
 
	// Methode to set title in the data sheet without creating a Table but using the sheet data only.
	// Creating a Table is not really necessary.
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

	/**
	 * 绘制堆叠柱状图
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try (XWPFDocument document = new XWPFDocument()) {
 
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
		}
	}
}