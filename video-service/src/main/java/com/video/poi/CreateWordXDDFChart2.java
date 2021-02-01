package com.video.poi;
 
import java.io.FileOutputStream;
 
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.chart.AxisPosition;
import org.apache.poi.xddf.usermodel.chart.ChartTypes;
import org.apache.poi.xddf.usermodel.chart.LegendPosition;
import org.apache.poi.xddf.usermodel.chart.MarkerStyle;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryAxis;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFChartLegend;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFLineChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFValueAxis;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
 
public class CreateWordXDDFChart2 {
 
 
	public static void main(String[] args) throws Exception {
		try (XWPFDocument document = new XWPFDocument()) {
 
			// create the chart
			XWPFChart chart = document.createChart(15 * Units.EMU_PER_CENTIMETER, 10 * Units.EMU_PER_CENTIMETER);
 
			//标题
			chart.setTitleText("地区排名前七的国家");
			//标题覆盖
			chart.setTitleOverlay(false);
			
			//图例位置
			XDDFChartLegend legend = chart.getOrAddLegend();
			legend.setPosition(LegendPosition.TOP);
			
			//分类轴标(X轴),标题位置
			XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
			bottomAxis.setTitle("国家");
			//值(Y轴)轴,标题位置
			XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
			leftAxis.setTitle("面积和人口");
 
			//CellRangeAddress(起始行号，终止行号， 起始列号，终止列号）
			//分类轴标(X轴)数据，单元格范围位置[0, 0]到[0, 6]
//			XDDFDataSource<String> countries = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(0, 0, 0, 6));
			XDDFCategoryDataSource countries = XDDFDataSourcesFactory.fromArray(new String[] {"俄罗斯","加拿大","美国","中国","巴西","澳大利亚","印度"});
			//数据1，单元格范围位置[1, 0]到[1, 6]
//			XDDFNumericalDataSource<Double> area = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 1, 0, 6));
			XDDFNumericalDataSource<Integer> area = XDDFDataSourcesFactory.fromArray(new Integer[] {17098242,9984670,9826675,9596961,8514877,7741220,3287263});
 
			//数据1，单元格范围位置[2, 0]到[2, 6]
//			XDDFNumericalDataSource<Double> population = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(2, 2, 0, 6));


			//LINE：折线图，
			XDDFLineChartData data = (XDDFLineChartData) chart.createData(ChartTypes.LINE, bottomAxis, leftAxis);
 
			//图表加载数据，折线1
			XDDFLineChartData.Series series1 = (XDDFLineChartData.Series) data.addSeries(countries, area);
			//折线图例标题
			series1.setTitle("面积", null);
			//是否弯曲
			series1.setSmooth(false);
			//设置标记大小
			series1.setMarkerSize((short) 6);
			//设置标记样式，星星
			series1.setMarkerStyle(MarkerStyle.STAR);
			//绘制
			chart.plot(data);
 
			// Write the output to a file
			try (FileOutputStream fileOut = new FileOutputStream("CreateWordXDDFChart.docx")) {
				document.write(fileOut);
			}
		}
	}
}