package com.video.poi;

import org.apache.commons.lang3.RandomUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.drawingml.x2006.main.*;

import java.io.FileOutputStream;
import java.io.IOException;

public class CreateWordXDDFChart4 {
 
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

	public void createScatterChart() throws IOException {
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet("散点图");

		Row row;
		Cell cell;
		for (int r = 0; r < 105; r++) {
			row = sheet.createRow(r);
			cell = row.createCell(0);
			cell.setCellValue("S" + r);
			cell = row.createCell(1);
			cell.setCellValue(RandomUtils.nextInt(1,100));
		}

		XSSFDrawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 0, 0, 21, 40);

		XSSFChart chart = drawing.createChart(anchor);

		chart.setTitleText("预选赛项目得分分布图");
		chart.setAutoTitleDeleted(false);

		CTChart ctChart = chart.getCTChart();
		ctChart.addNewPlotVisOnly().setVal(true);
		ctChart.addNewDispBlanksAs().setVal(STDispBlanksAs.Enum.forInt(2));
		ctChart.addNewShowDLblsOverMax().setVal(false);


		CTPlotArea ctPlotArea = ctChart.getPlotArea();

		CTScatterChart scatterChart = ctPlotArea.addNewScatterChart();
		scatterChart.addNewScatterStyle().setVal(STScatterStyle.LINE_MARKER);
		scatterChart.addNewVaryColors().setVal(false);
		scatterChart.addNewAxId().setVal(123456);
		scatterChart.addNewAxId().setVal(123457);

		CTCatAx ctCatAx = ctPlotArea.addNewCatAx();
		ctCatAx.addNewAxId().setVal(123456);
		CTScaling ctScaling = ctCatAx.addNewScaling();
		ctScaling.addNewOrientation().setVal(STOrientation.MIN_MAX);
		ctCatAx.addNewDelete().setVal(false);
		ctCatAx.addNewAxPos().setVal(STAxPos.B);
		ctCatAx.addNewCrossAx().setVal(123457);
		ctCatAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);


		CTValAx ctValAx = ctPlotArea.addNewValAx();
		ctValAx.addNewAxId().setVal(123457);
		CTScaling ctScaling1 = ctValAx.addNewScaling();
		ctScaling1.addNewOrientation().setVal(STOrientation.MIN_MAX);
		ctValAx.addNewDelete().setVal(false);
		ctValAx.addNewAxPos().setVal(STAxPos.B);
		ctValAx.addNewCrossAx().setVal(123456);

		CTShapeProperties ctShapeProperties = ctValAx.addNewMajorGridlines().addNewSpPr();
		CTLineProperties ctLineProperties = ctShapeProperties.addNewLn();
		ctLineProperties.setW(9525);
		ctLineProperties.setCap(STLineCap.Enum.forInt(3));
		ctLineProperties.setCmpd(STCompoundLine.Enum.forInt(1));
		ctLineProperties.setAlgn(STPenAlignment.Enum.forInt(1));
		// 不显示Y轴上的坐标刻度线
		ctValAx.addNewMajorTickMark().setVal(STTickMark.NONE);
		ctValAx.addNewMinorTickMark().setVal(STTickMark.NONE);
		ctValAx.addNewTickLblPos().setVal(STTickLblPos.NEXT_TO);

		// 设置散点图内的信息
		CTScatterSer ctScatterSer = scatterChart.addNewSer();
		ctScatterSer.addNewIdx().setVal(0);
		ctScatterSer.addNewOrder().setVal(0);
		// 去掉连接线
		ctPlotArea.getScatterChartArray(0).getSerArray(0).addNewSpPr().addNewLn().addNewNoFill();

		// 设置散点图各图例的显示
		CTDLbls ctdLbls = scatterChart.addNewDLbls();
		ctdLbls.addNewShowVal().setVal(true);
		ctdLbls.addNewShowLegendKey().setVal(false);
		ctdLbls.addNewShowSerName().setVal(false);
		ctdLbls.addNewShowCatName().setVal(false);
		ctdLbls.addNewShowPercent().setVal(false);
		ctdLbls.addNewShowBubbleSize().setVal(false);
		// 设置标记的样式
		CTMarker ctMarker = ctScatterSer.addNewMarker();
		ctMarker.addNewSymbol().setVal(STMarkerStyle.Enum.forInt(3));
		ctMarker.addNewSize().setVal((short) 5);
		CTShapeProperties ctShapeProperties1 = ctMarker.addNewSpPr();
		ctShapeProperties1.addNewSolidFill().addNewSchemeClr().setVal(STSchemeColorVal.Enum.forInt(5));
		CTLineProperties ctLineProperties1 = ctShapeProperties1.addNewLn();
		ctLineProperties1.setW(9525);
		ctLineProperties1.addNewSolidFill().addNewSchemeClr().setVal(STSchemeColorVal.Enum.forInt(5));

		CTAxDataSource ctAxDataSource = ctScatterSer.addNewXVal();
		CTStrRef ctStrRef = ctAxDataSource.addNewStrRef();
		ctStrRef.setF("散点图!$A$1:$A$100");
		CTNumDataSource ctNumDataSource = ctScatterSer.addNewYVal();
		CTNumRef ctNumRef = ctNumDataSource.addNewNumRef();
		ctNumRef.setF("散点图!$B$1:$B$100");

		System.out.println(ctChart);

		FileOutputStream fileOut = new FileOutputStream("C:\\out.xlsx");
		wb.write(fileOut);
		fileOut.close();
	}

	public static void main(String[] args) throws IOException {
		CreateWordXDDFChart4 createWordXDDFChart4 = new CreateWordXDDFChart4();
		createWordXDDFChart4.createScatterChart();
	}
    /**
     * 绘制堆散点图
	 * @param args
     * @throws Exception
	 */
//	public static void main(String[] args) throws Exception {
//		try (XWPFDocument document = new XWPFDocument()) {
//
//			// 创建源数据
//			// 底部数据栏
//			String[] categories = new String[] { "Lang 1", "Lang 2", "Lang 3" };
//			// 柱状图数据来源
//			Double[] valuesA = new Double[] { 10d, 20d, 30d };
//			Double[] valuesB = new Double[] { 15d, 25d, 35d };
//			Double[] valuesC = new Double[] { 10d, 8d, 20d };
//
//			// 创建一个柱状图
//			XWPFChart chart = document.createChart(15 * Units.EMU_PER_CENTIMETER, 10 * Units.EMU_PER_CENTIMETER);
//
//			// 处理对应的数据
//			int numOfPoints = categories.length;
//			String categoryDataRange = chart.formatRange(new CellRangeAddress(1, numOfPoints, 0, 0));
//			String valuesDataRangeA = chart.formatRange(new CellRangeAddress(1, numOfPoints, 1, 1));
//			String valuesDataRangeB = chart.formatRange(new CellRangeAddress(1, numOfPoints, 2, 2));
//			String valuesDataRangeC = chart.formatRange(new CellRangeAddress(1, numOfPoints, 2, 2));
//			XDDFDataSource<String> categoriesData = XDDFDataSourcesFactory.fromArray(categories, categoryDataRange, 0);
//
//			XDDFNumericalDataSource<Double> valuesDataA = XDDFDataSourcesFactory.fromArray(valuesA, valuesDataRangeA, 1);
//			XDDFNumericalDataSource<Double> valuesDataB = XDDFDataSourcesFactory.fromArray(valuesB, valuesDataRangeB, 2);
//			XDDFNumericalDataSource<Double> valuesDataC = XDDFDataSourcesFactory.fromArray(valuesC, valuesDataRangeC, 2);
//
//			// 创建一些轴
//			XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
//			XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
//			leftAxis.setCrosses(AxisCrosses.AUTO_ZERO);
//			// Set AxisCrossBetween, so the left axis crosses the category axis between the categories.
//			// Else first and last category is exactly on cross points and the bars are only half visible.
//			leftAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
//
//			// 创建柱状图的类型
//			XDDFChartData data = chart.createData(ChartTypes.SCATTER, bottomAxis, leftAxis);
//			// 指定为堆叠柱状图
////			((XDDFBarChartData) data).setBarGrouping(BarGrouping.STACKED);
////			((XDDFBarChartData) data).setBarDirection(BarDirection.COL);
//
//			// 创建每一个柱子上的类容 若有多个 循环添加
//			// if only one series do not vary colors for each bar
////			((XDDFBarChartData) data).setVaryColors(true);
//			XDDFChartData.Series series = data.addSeries(categoriesData, valuesDataA);
////			XDDFChartData.Series series2 = data.addSeries(categoriesData, valuesDataB);
////			XDDFChartData.Series series3 = data.addSeries(categoriesData, valuesDataC);
//			series.setTitle("a", setTitleInDataSheet(chart, "a", 1));
////			series2.setTitle("b", setTitleInDataSheet(chart, "b", 2));
////			series3.setTitle("c", setTitleInDataSheet(chart, "c", 3));
//
//
//			// plot chart data
//			// 设置同一个柱子上的图形偏移量为0
//			chart.getCTChart().getPlotArea().getScatterChartArray(0).getSerArray(0).addNewSpPr().addNewLn().addNewNoFill();
//			// 绘制图形数据
//			chart.plot(data);
//
//			// create legend
//			XDDFChartLegend legend = chart.getOrAddLegend();
//			legend.setPosition(LegendPosition.LEFT);
//			legend.setOverlay(false);
//
//			// Write the output to a file
//			try (FileOutputStream fileOut = new FileOutputStream("CreateWordXDDFChart.docx")) {
//				document.write(fileOut);
//			}
//		}
//	}
}