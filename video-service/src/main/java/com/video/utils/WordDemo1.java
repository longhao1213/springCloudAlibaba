package com.video.utils;

import java.util.ArrayList;
import java.util.HashMap;
import cn.afterturn.easypoi.entity.ImageEntity;

/**
 * @author 何昌杰
 */
public class WordDemo1 {
 
    public static void main(String[] args) {
        HashMap<String, Object> map = new HashMap<>(4);
 
        //模拟饼状图数据
        HashMap<String, Integer> datas = new HashMap<>(3);
        datas.put("一号",10);
        datas.put("二号",20);
        datas.put("三号",40);
        ImageEntity imageEntity = JfreeUtil.pieChart("饼状图",datas, 500, 300);
         map.put("picture", imageEntity);

        //模拟柱状图数据
        HashMap<Integer, Integer> datas2 = new HashMap<>(3);
        datas2.put(1,10);
        datas2.put(2,20);
        datas2.put(3,40);
        ImageEntity imageEntity2 = JfreeUtil.barChart("柱状图",datas2, 500, 300);
        map.put("picture2", imageEntity2);
 
        //模拟其它普通数据
        map.put("username", "张三");
        map.put("date", "2019-10-10");
        map.put("desc", "测试");
        map.put("boo", true);
 
        //模拟表格数据
        ArrayList<HashMap<String, String>> list = new ArrayList<>(2);
        HashMap<String, String> temp = new HashMap<>(3);
        temp.put("sn","1");
        temp.put("name","第一个人");
        temp.put("age","23");
        list.add(temp);
        temp = new HashMap<>(3);
        temp.put("sn","2");
        temp.put("name","第二个人");
        temp.put("age","24");
        list.add(temp);
        map.put("personlist",list);
        //word模板相对路径、word生成路径、word生成的文件名称、数据源
        WordUtil.exportWord("D:\\FreeMarker.docx", "D:/", "生成文件.docx", map);
    }
}