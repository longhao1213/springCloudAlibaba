package com.video.poi;

import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.XDDFColor;
import org.apache.poi.xddf.usermodel.XDDFShapeProperties;
import org.apache.poi.xddf.usermodel.XDDFSolidFillProperties;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTScatterSer;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;

import java.io.FileOutputStream;

public class CreateWordXDDFChart5 {


	/**
	 * 绘制散点图
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		try (XWPFDocument document = new XWPFDocument()) {
 
			// create the chart
			XWPFChart chart = document.createChart(15 * Units.EMU_PER_CENTIMETER, 10 * Units.EMU_PER_CENTIMETER);
 
			//标题
			chart.setTitleText("散点图");
			//标题覆盖
			chart.setTitleOverlay(false);
			
			//图例位置
			XDDFChartLegend legend = chart.getOrAddLegend();
			legend.setPosition(LegendPosition.TOP);
			
			//分类轴标(X轴),标题位置
			XDDFCategoryAxis bottomAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
			bottomAxis.setTitle("X轴");
			//值(Y轴)轴,标题位置
			XDDFValueAxis leftAxis = chart.createValueAxis(AxisPosition.LEFT);
			leftAxis.setTitle("Y轴");
 
			// 设置X轴数据
			XDDFNumericalDataSource countries = XDDFDataSourcesFactory.fromArray(new Integer[] {1,2,3,4,5,6,7});
			// 设置Y轴数据
			XDDFNumericalDataSource<Integer> area = XDDFDataSourcesFactory.fromArray(new Integer[] {5,5,5,4,5,6,7});

			//LINE：散点图，
			XDDFScatterChartData data = (XDDFScatterChartData) chart.createData(ChartTypes.SCATTER, bottomAxis, leftAxis);
			// 不自动生成颜色
			data.setVaryColors(false);
			//图表加载数据
			XDDFScatterChartData.Series series1 = (XDDFScatterChartData.Series) data.addSeries(countries, area);
			//设置标记大小
			series1.setMarkerSize((short) 6);
			//设置标记样式，星星
			series1.setMarkerStyle(MarkerStyle.CIRCLE);
			//绘制
			byte[] color = new byte[] {(byte) 195, (byte) 224, (byte) 176};
			chart.getCTChart().getPlotArea().getScatterChartArray(0).getSerArray(0).addNewSpPr().addNewLn().addNewNoFill();
			XDDFSolidFillProperties fillMarker = new XDDFSolidFillProperties(XDDFColor.from(new byte[]{(byte)0xFF, (byte)0xFF, (byte)0x00}));
//			XDDFSolidFillProperties fillMarker = new XDDFSolidFillProperties(XDDFColor.from(228, 225, 3));

			XDDFShapeProperties propertiesMarker = new XDDFShapeProperties();
			propertiesMarker.setFillProperties(fillMarker);
			chart.getCTChart().getPlotArea().getScatterChartArray(0).getSerArray(0).getMarker()
					.addNewSpPr().set(propertiesMarker.getXmlObject());
			chart.plot(data);
 
			try (FileOutputStream fileOut = new FileOutputStream("CreateWordXDDFChart.docx")) {
				document.write(fileOut);
			}
		}
	}
}