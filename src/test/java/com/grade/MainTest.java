package com.grade;

import com.grade.plugin.Constant;
import com.grade.plugin.Dao;

import com.grade.plugin.Subdimension;
import com.jfinal.plugin.activerecord.Record;
import org.junit.Test;

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
    public void test(){
        Main.main("6", "7", "7");
    }

    @Test
    public void testSQL(){
        Dao dao = new Dao("sql");
        Subdimension subdimension = new Subdimension();
        List<Record> list = dao.find(Constant.findStuAns1("6", "7", "7", "220173"));
        //System.out.println(list.size());
        //System.out.println(list.stream().collect(Collectors.groupingBy(x -> x.getStr("stuId"))).keySet());
        List<Record> rsult = this.getScoreList(list, "10601", "10603");
        List<Record> resultF = this.getScoreList(list, "10605");
        List<Record> list2 = dao.find(Constant.findSecondPart("6", "7", "7", "220173"));
        List<Record> result = this.getScoreList(list2, "20203");
        List<Record> resultPart18 = list2.stream().filter(x -> 22 == x.getInt("part")).collect(Collectors.toList());
        //System.out.println(resultPart18);
        //this.rectifyScoreFinal(resultPart18);
       // System.out.println(subdimension.primitive(resultPart25).stream().map(x -> x.get("queAns")).collect(Collectors.toList()));
        //System.out.println(subdimension.primitive(resultPart25).stream().map(x -> x.get("reverse_scoring")).collect(Collectors.toList()));
       // System.out.println(result.stream().map(x -> x.get("queAns")).collect(Collectors.toList()));
        System.out.println(subdimension.rectifyScoreFinal(resultPart18));
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

}