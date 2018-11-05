package com.grade;

import com.google.gson.Gson;
import com.grade.empty.JsonToObject;
import com.grade.plugin.*;

import com.jfinal.plugin.activerecord.Record;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade
 * @Description:
 * @Date: Created in 9:31 2018/9/21
 */
public class MainTest {
    @Test
    public void test() throws InterruptedException {

    }

    @Test
    public void testSchool(){
        Dao dao = new Dao("school");
        //List<Record> school = dao.find(Constant.findThisDayTestSchool());
        //System.out.println(school);
        //new ClassActionService().getClassAction("15","25","206");
        //new ClassPositivePsychology().getClassPsychology("15","25","206");

        //new FirstService().firstPart("15","25","206",dao);
        //new SecondService().firstPart("15","25","206",dao);
    }

    @Test
    public void testSQL(){
        Dao dao = new Dao("sql");
        Subdimension subdimension = new Subdimension();
        //List<Record> list = dao.find(Constant.findStuAns1("6", "7", "7", "220173"));
        //System.out.println(list.size());
        //System.out.println(list.stream().collect(Collectors.groupingBy(x -> x.getStr("stuId"))).keySet());
        //List<Record> rsult = this.getScoreList(list, "10601", "10603");
        //List<Record> resultF = this.getScoreList(list, "10605");
       // List<Record> list2 = dao.find(Constant.findSecondPart("6", "7", "7", "220173"));
        //List<Record> result = this.getScoreList(list2, "20203");
        //List<Record> resultPart18 = list2.stream().filter(x -> 22 == x.getInt("part")).collect(Collectors.toList());
        //System.out.println(resultPart18);
        //this.rectifyScoreFinal(resultPart18);
       // System.out.println(subdimension.primitive(resultPart25).stream().map(x -> x.get("queAns")).collect(Collectors.toList()));
        //System.out.println(subdimension.primitive(resultPart25).stream().map(x -> x.get("reverse_scoring")).collect(Collectors.toList()));
       // System.out.println(result.stream().map(x -> x.get("queAns")).collect(Collectors.toList()));
        //System.out.println(subdimension.rectifyScoreFinal(resultPart18));
    }

    public List<Record> rectifyScoreFinal(List<Record> score) {

        List<Record> temp = score.stream()
                .filter(x -> x.getInt("queSeq") != 0)
                .collect(Collectors.toList());

        if (!temp.isEmpty()) {
            Map<Integer, List<Record>> value = temp.stream()
                    .collect(Collectors.groupingBy(x -> x.getInt("queAns")))
                    .entrySet()
                    .stream()
                    .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
         System.out.println(value);
        }
        return score;
    }

    /**
     * 获取第一部分中某子维度集合
     * @param findAll 第一部分中第一小部分所有成绩
     * @param scaleTypeCodeFirst  third_scale_type_code子维度编号
     * @param scaleTypeCodeSecond
     * @return
     */
    private List<Record> getScoreList(List<Record> findAll, String scaleTypeCodeFirst, String scaleTypeCodeSecond){

        List<Record> restult = findAll.stream()
                .filter(x -> scaleTypeCodeFirst.equals(x.getStr("third_scale_type_code")))
                //.filter(x -> scaleTypeCodeSecond.equals(x.getStr("third_scale_type_code")))
                .collect(Collectors.toList());

        List<Record> temp = findAll.stream()
                //.filter(x -> scaleTypeCodeFirst.equals(x.getStr("third_scale_type_code")))
                .filter(x -> scaleTypeCodeSecond.equals(x.getStr("third_scale_type_code")))
                .collect(Collectors.toList());
        restult.addAll(temp);
        temp.clear();

        return restult;
    }

    /**
     * 获取第一部分中某子维度集合
     * @param findAll 第一部分中第一小部分所有成绩
     * @param scaleTypeCode  third_scale_type_code子维度编号
     * @return
     */
    private List<Record> getScoreList(List<Record> findAll, String scaleTypeCode){
        return findAll.stream()
                .filter(x -> scaleTypeCode.equals(x.getStr("third_scale_type_code")))
                .collect(Collectors.toList());
    }

    @Test
    public void stringTest(){
        String string = "[G360983200605238432, G360983200612078473, G36098320060908301X]";
        String a = "[G360983200605238432, G360983200612078473, G36098320060908301X]";
        String b = "[G360983200605238432, G360983200612078473, G36098320060908301X]";
        String res = string.substring(1, string.lastIndexOf("]")) + a.substring(1, a.lastIndexOf("]")) + b.substring(1, b.lastIndexOf("]"));
        System.out.println(res);

    }

    @Test
    public void time(){
        String start = "2018-10-26 18:30:00.000";
        String end = "2018-10-26 18:37:30.000";
        System.out.println(getTestTime(start, end));
    }

    private String getTestTime(String startTime, String endTime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
        DateTime start = dateTimeFormatter.parseDateTime(startTime);
        DateTime end = dateTimeFormatter.parseDateTime(endTime);
        return String.valueOf(Minutes.minutesBetween(start, end).getMinutes()+ "分" + new Period(start, end).getSeconds());
    }

    @Test
    public void testEmpty(){
        String encoding = "UTF-8";
        File file = new File("C:\\Users\\17917\\Desktop\\grade\\src\\main\\java\\com\\grade\\db\\example.json");
        String result = null;
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(fileContent);
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            result = new String(fileContent, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JsonToObject json = new JsonToObject();
        Gson gson = new Gson();

        json = gson.fromJson(result, JsonToObject.class);

        System.out.println(json);
        System.out.println(json.getModel());
        System.out.println(json.getDate());
        System.out.println(json.getDate().get(0).getClassId());
        System.out.println(json.getDate().get(0).getSchoolId());
        System.out.println(json.getDate().get(0).getClassId());

    }

}