package com.video.utils;

import cn.afterturn.easypoi.entity.ImageEntity;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.util.Assert;

import javax.validation.groups.Default;
import java.awt.*;
import java.io.*;
import java.util.Map;

/**
 * @author 何昌杰
 *
 * 参考API博客 https://blog.csdn.net/dagecao/article/details/86536680
 */
@Slf4j
public class JfreeUtil {

    private static String tempImgPath="D:\\tempJfree.jpeg";

    /**
     * 将图片转化为字节数组
     * @return 字节数组
     */
    private static byte[] imgToByte(){
        File file = new File(tempImgPath);
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        //删除临时文件
        file.delete();
        return buffer;
    }

    /**
     * 生成饼状图
     * @param title
     * @param datas
     * @param width
     * @param height
     * @return
     */
    public static ImageEntity pieChart(String title, Map<String, Integer> datas, int width, int height) {

        //创建主题样式
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("宋体", Font.BOLD, 20));
        //设置图例的字体
        standardChartTheme.setRegularFont(new Font("宋体", Font.PLAIN, 15));
        //设置轴向的字体
        standardChartTheme.setLargeFont(new Font("宋体", Font.PLAIN, 15));
        //设置主题样式
        ChartFactory.setChartTheme(standardChartTheme);

        //根据jfree生成一个本地饼状图
        DefaultPieDataset pds = new DefaultPieDataset();
        datas.forEach(pds::setValue);
        //图标标题、数据集合、是否显示图例标识、是否显示tooltips、是否支持超链接
        JFreeChart chart = ChartFactory.createPieChart(title, pds, true, false, false);
        //设置抗锯齿
        chart.setTextAntiAlias(false);
        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setNoDataMessage("暂无数据");
        //忽略无值的分类
        plot.setIgnoreNullValues(true);
        plot.setBackgroundAlpha(0f);
        //设置标签阴影颜色
        plot.setShadowPaint(new Color(255,255,255));
        //设置标签生成器(默认{0})
        plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}({1})/{2}"));
        try {
            ChartUtils.saveChartAsJPEG(new File(tempImgPath), chart, width, height);
        } catch (IOException e1) {
            log.error("生成饼状图失败！");
        }
        ImageEntity imageEntity = new ImageEntity(imgToByte(), width, height);
        Assert.notNull(imageEntity.getData(),"生成饼状图对象失败！");
        return imageEntity;
    }

    /**
     * 生成柱状图
     * @param title
     * @param datas
     * @param width
     * @param height
     * @return
     */
    public static ImageEntity barChart(String title, Map<Integer, Integer> datas, int width, int height) {
        //创建主题样式
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("宋体", Font.BOLD, 20));
        //设置图例的字体
        standardChartTheme.setRegularFont(new Font("宋体", Font.PLAIN, 15));
        //设置轴向的字体
        standardChartTheme.setLargeFont(new Font("宋体", Font.PLAIN, 15));
        //设置主题样式
        ChartFactory.setChartTheme(standardChartTheme);

        // 根据jfree生成一个本地柱状图
        DefaultCategoryDataset defaultCategoryDataset = new DefaultCategoryDataset();
        for (Map.Entry<Integer, Integer> stringIntegerEntry : datas.entrySet()) {
            defaultCategoryDataset.addValue(stringIntegerEntry.getKey(), "", stringIntegerEntry.getValue());
        }
        JFreeChart barChart = ChartFactory.createBarChart(title, "横轴", "纵轴", defaultCategoryDataset);
//设置抗锯齿
        barChart.setTextAntiAlias(false);
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setNoDataMessage("暂无数据");
        //忽略无值的分类
//        plot.setIgnoreNullValues(true);
        plot.setBackgroundAlpha(0f);
        // 设置图的背景颜色
        plot.setBackgroundPaint(ChartColor.WHITE);
        //设置图的边框
        plot.setOutlinePaint(ChartColor.white);
        // 获取渲染器
        BarRenderer customBarRenderer = (BarRenderer) plot.getRenderer();
        //取消柱子上的渐变色
        customBarRenderer.setBarPainter( new StandardBarPainter() );
        customBarRenderer.setItemMargin(-0.01);
        //设置柱子的颜色
        Color c=new Color(156, 73, 160);
        customBarRenderer.setSeriesPaint(0, c);
        //设置柱子宽度 单位为占用图片的百分比
        customBarRenderer.setMaximumBarWidth(0.015);
        customBarRenderer.setMinimumBarLength(0.005);
        //设置柱子间距
        customBarRenderer.setItemMargin(1);
        //设置阴影,false代表没有阴影
        customBarRenderer.setShadowVisible(true);
        // 设置柱状图的顶端显示数字
//        customBarRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());//显示每个柱的数值
//        customBarRenderer.setBaseItemLabelsVisible(true);
        customBarRenderer.setItemLabelAnchorOffset(0);

        NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
        CategoryAxis axis = plot.getDomainAxis(); //x轴
//	        /设置最高的一个柱与图片顶端的距离(最高柱的10%)
        numberaxis.setUpperMargin(0.3);
        // axis.setTickLabelPaint(ChartColor.red);
//	        axis.setMaximumCategoryLabelLines(10);  //标题行数，每个字显示一行
//	        axis.setMaximumCategoryLabelWidthRatio(0.5f);  //每个标题宽度，控制为1个字的宽度
//	        axis.setLabelInsets();
//	        axis.setLabelPaint(ChartColor.red);
        /*------设置X轴坐标上的文字-----------*/
        axis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 20));
        /*------设置X轴的标题文字------------*/
        axis.setLabelFont(new Font("宋体", Font.PLAIN, 20));

        /*------设置Y轴坐标上的文字-----------*/
        numberaxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 20));

        // 设置显示位置
//	        p.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
//	        p.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

        /*------设置Y轴的标题文字------------*/
        numberaxis.setLabelFont(new Font("黑体", Font.PLAIN,20));
        //设置y轴显示整数数据
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        //设置Y轴的标题文字颜色
//	        numberaxis.setLabelPaint(ChartColor.red);
        //设置y轴文字横方向
//        numberaxis.setLabelAngle(1.5);
        //设置 y轴刻度尺为隐藏
//	        numberaxis.setTickLabelsVisible(false);
        barChart.getTitle().setFont(new Font("宋体", Font.PLAIN, 20));
        try {
            ChartUtils.saveChartAsJPEG(new File(tempImgPath), barChart, width, height);
        } catch (IOException e1) {
            log.error("生成柱状图失败！");
        }
        ImageEntity imageEntity = new ImageEntity(imgToByte(), width, height);
        Assert.notNull(imageEntity.getData(),"生成柱状图对象失败！");
        return imageEntity;
    }

    public static ImageEntity stackedChart(String title, Map<Integer, Integer> datas, int width, int height) {
        //创建主题样式
        StandardChartTheme standardChartTheme = new StandardChartTheme("CN");
        //设置标题字体
        standardChartTheme.setExtraLargeFont(new Font("宋体", Font.BOLD, 20));
        //设置图例的字体
        standardChartTheme.setRegularFont(new Font("宋体", Font.PLAIN, 15));
        //设置轴向的字体
        standardChartTheme.setLargeFont(new Font("宋体", Font.PLAIN, 15));
        //设置主题样式
        ChartFactory.setChartTheme(standardChartTheme);
        DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
        for (Map.Entry<Integer, Integer> stringIntegerEntry : datas.entrySet()) {
            categoryDataset.addValue(stringIntegerEntry.getKey(), "", stringIntegerEntry.getValue());
        }
        JFreeChart barChart = ChartFactory.createStackedBarChart(title, "横轴", "纵轴", categoryDataset);
        //设置抗锯齿
        barChart.setTextAntiAlias(false);
        CategoryPlot plot = barChart.getCategoryPlot();
        plot.setNoDataMessage("暂无数据");
        //忽略无值的分类
//        plot.setIgnoreNullValues(true);
        plot.setBackgroundAlpha(0f);
        // 设置图的背景颜色
        plot.setBackgroundPaint(ChartColor.WHITE);
        //设置图的边框
        plot.setOutlinePaint(ChartColor.white);
        // 获取渲染器
        BarRenderer customBarRenderer = (BarRenderer) plot.getRenderer();
        //取消柱子上的渐变色
        customBarRenderer.setBarPainter( new StandardBarPainter() );
        customBarRenderer.setItemMargin(-0.01);
        //设置柱子的颜色
        Color c=new Color(156, 73, 160);
        customBarRenderer.setSeriesPaint(0, c);
        //设置柱子宽度 单位为占用图片的百分比
        customBarRenderer.setMaximumBarWidth(0.015);
        customBarRenderer.setMinimumBarLength(0.005);
        //设置柱子间距
        customBarRenderer.setItemMargin(1);
        //设置阴影,false代表没有阴影
        customBarRenderer.setShadowVisible(true);
        // 设置柱状图的顶端显示数字
//        customBarRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());//显示每个柱的数值
//        customBarRenderer.setBaseItemLabelsVisible(true);
        customBarRenderer.setItemLabelAnchorOffset(0);

        NumberAxis numberaxis = (NumberAxis) plot.getRangeAxis();
        CategoryAxis axis = plot.getDomainAxis(); //x轴
//	        /设置最高的一个柱与图片顶端的距离(最高柱的10%)
        numberaxis.setUpperMargin(0.3);
        // axis.setTickLabelPaint(ChartColor.red);
//	        axis.setMaximumCategoryLabelLines(10);  //标题行数，每个字显示一行
//	        axis.setMaximumCategoryLabelWidthRatio(0.5f);  //每个标题宽度，控制为1个字的宽度
//	        axis.setLabelInsets();
//	        axis.setLabelPaint(ChartColor.red);
        /*------设置X轴坐标上的文字-----------*/
        axis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 20));
        /*------设置X轴的标题文字------------*/
        axis.setLabelFont(new Font("宋体", Font.PLAIN, 20));

        /*------设置Y轴坐标上的文字-----------*/
        numberaxis.setTickLabelFont(new Font("sans-serif", Font.PLAIN, 20));

        // 设置显示位置
//	        p.setDomainAxisLocation(AxisLocation.TOP_OR_RIGHT);
//	        p.setRangeAxisLocation(AxisLocation.BOTTOM_OR_RIGHT);

        /*------设置Y轴的标题文字------------*/
        numberaxis.setLabelFont(new Font("黑体", Font.PLAIN,20));
        //设置y轴显示整数数据
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        //设置Y轴的标题文字颜色
//	        numberaxis.setLabelPaint(ChartColor.red);
        //设置y轴文字横方向
//        numberaxis.setLabelAngle(1.5);
        //设置 y轴刻度尺为隐藏
//	        numberaxis.setTickLabelsVisible(false);
        barChart.getTitle().setFont(new Font("宋体", Font.PLAIN, 20));
        try {
            ChartUtils.saveChartAsJPEG(new File(tempImgPath), barChart, width, height);
        } catch (IOException e1) {
            log.error("生成柱状图失败！");
        }
        ImageEntity imageEntity = new ImageEntity(imgToByte(), width, height);
        Assert.notNull(imageEntity.getData(),"生成柱状图对象失败！");
        return imageEntity;
    }

}