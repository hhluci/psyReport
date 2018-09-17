package com.grade.plugin;

import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.descriptive.summary.SumOfSquares;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description:
 * @Date: Created in 14:16 2018/9/5
 */

public class SubdimensionTest {

    @Test
    public void reverseScoringd(){

        Dao dao = new Dao();
        /*List<Record> ans = dao.find("SELECT\n" +
                "\ta.schoolId, a.gradeId, a.classId, a.stuId, a.queAns, a.queSeq, a.startTime, a.submitTime,\n" +
                "  b.mode_code, b.third_scale_type_code, b.option_number, b.reverse_scoring\n" +
                "FROM\n" +
                "\tanswer a\n" +
                "JOIN question_bank b ON a.queCode = b.question_code\n" +
                "WHERE\n" +
                "\tb.third_scale_type_code LIKE '10101';");*/

        /*List<Record> ansL = dao.find("SELECT\n" +
                "\ta.schoolId, a.gradeId, a.classId, a.stuId, a.queAns, a.queSeq, a.startTime, a.submitTime,\n" +
                "  b.mode_code, b.third_scale_type_code, b.question_code, b.option_number, b.reverse_scoring\n" +
                "FROM\n" +
                "\tanswer a\n" +
                "JOIN question_bank b ON a.queCode = b.question_code\n" +
                "WHERE\n" +
                "\tb.third_scale_type_code LIKE '30101'");*/
        List<Record> ans1 = dao.find("SELECT\n" +
                "\ta.schoolId, a.gradeId, a.classId, a.stuId, a.queAns, a.queSeq, a.startTime, a.submitTime,\n" +
                "  b.mode_code, b.third_scale_type_code, b.question_code, b.option_number, b.reverse_scoring\n" +
                "FROM\n" +
                "\tanswer a\n" +
                "JOIN question_bank b ON a.queCode = b.question_code \n" +
                "WHERE\n" +
                "\tb.question_code LIKE '%-1'");
        List<Record> ans2 = dao.find("SELECT\n" +
                "\ta.schoolId, a.gradeId, a.classId, a.stuId, a.queAns, a.queSeq, a.startTime, a.submitTime,\n" +
                "  b.mode_code, b.third_scale_type_code, b.question_code, b.option_number, b.reverse_scoring\n" +
                "FROM\n" +
                "\tanswer a\n" +
                "JOIN question_bank b ON a.queCode = b.question_code \n" +
                "WHERE\n" +
                "\tb.question_code LIKE '%-2' AND b.third_scale_type_code = '30102'");

        Subdimension subdimension = new Subdimension();
        //System.out.println(subdimension.reverseScoring(2, "7"));
        //System.out.println(subdimension.primitiveScore(ans));
        //System.out.println(subdimension.getL(ansL));
        System.out.println(subdimension.getC(ans1, ans2));
    }

    @Test
    public void Z(){
        Dao dao = new Dao();
        Subdimension subdimension = new Subdimension();
        //List<Record> ans = dao.findTemplate("b.third_scale_type_code = '10301'");
        List<Record> ans = dao.find("SELECT\n" +
                "\ta.stuId\n" +
                "FROM\n" +
                "\tanswer a\n" +
                "JOIN question_bank b ON a.queCode = b.question_code\n" +
                "WHERE\n" +
                "\ta.schoolId = '1'\n" +
                "AND a.classId = 1\n" +
                "AND a.gradeId = 2\n" +
                "AND b.third_scale_type_code = '10301'\n" +
                "GROUP BY\n" +
                "\ta.stuId");

        //System.out.println(subdimension.getAverage(ans));
        System.out.println(ans);



    }

    @Test
    public void getAvarage(){
        double[] a = new double[]{1,2,3};
        double[] b = new double[]{45,4,7,8,2,1,6,9,2,7};
        List<Integer> c = new ArrayList<>();
        c.add(1);
        c.add(1);
        c.add(15);
        c.add(12);
        c.add(16);
        c.add(11);
        c.add(16);
        c.add(2);
        Subdimension subdimension = new Subdimension();
        //System.out.println(subdimension.getAverage(a));
        //System.out.println(subdimension.getStandardDeviation(a));
        //System.out.println(new Variance(false).evaluate(a));
        System.out.println(subdimension.getZ(a)[0]);
        //System.out.println(subdimension.getT(a, a));
        //NormalDistribution normalDistribution = new NormalDistribution(0,1);
        //System.out.println(normalDistribution.cumulativeProbability(1.96));
        //System.out.println(new DecimalFormat("0.00").format(new NormalDistribution().cumulativeProbability(-1.96)));
        //System.out.println(subdimension.getPercentLevel(a,a));
        //System.out.println(subdimension.dimensionOriginalScore(a));

        //System.out.println(new SumOfSquares(5).evaluate());

        /*for (int i = 0; i < b.length; i++){
            System.out.println(subdimension.getSortScore(b)[i]);
        }
*/
        //System.out.println(subdimension.isRepeat(c));

    }

    @Test
    public void streamTest(){
        List<String> list = Arrays.asList("123", "1234", "12345", "123456", "1234567", "122222223", "123", "1234", "2422");
        Map<String, Long> collect = list.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(collect);
        System.out.println(list.indexOf("123"));


    }

    @Test
    public void tesst(){
        Record record = new Record();
        Record record1 = new Record();
        Record record2 = new Record();
        Record record3 = new Record();
        Record record4 = new Record();
        record.set("username", "123");
        record1.set("username", "123");
        record2.set("username", "145");
        record3.set("username", "1566");
        record4.set("username", "145");
        List<Record> list = new ArrayList<>();
        list.add(record);
        list.add(record1);
        list.add(record2);
        list.add(record3);
        list.add(record4);
        Map<String, List<Record>> map = list.stream().collect(Collectors.groupingBy(x -> x.get("username")));
        Map<String, List<Record>> map1 = map.entrySet().stream().filter(x -> x.getValue().size() >= 2)
                 .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));



        System.out.println(map1);
    }
    @Test
    public void getR(){
        Record record = new Record();
        Record record1 = new Record();
        Record record2 = new Record();
        Record record3 = new Record();
        Record record4 = new Record();
        record.set("queAns", 5)
        .set("question_code", "123");
        record1.set("queAns", 5)
                .set("question_code", "1234");
        record2.set("queAns", 6)
                .set("question_code", "1");
        record3.set("queAns", 6)
                .set("question_code", "12");
        record4.set("queAns", 6)
                .set("question_code", "124");
        List<Record> list = new ArrayList<>();
        list.add(record);
        list.add(record1);
        list.add(record2);
        list.add(record3);
        list.add(record4);
        System.out.println(new Subdimension().rectifyScore(list));
        /*List<Record> list1 = new ArrayList<>();
        Map<Object, List<Record>> map = new Subdimension().getRepeateGroup(list);
        Set<Object> set = map.keySet();
        //System.out.println(new Subdimension().getRectify(list.size(), list));
        System.out.println(new Subdimension().getRepeateGroup(list));
        System.out.println(new Subdimension().getRepeateGroup(list).size());
        System.out.println(new Subdimension().getRepeateGroup(list).keySet().iterator());
        map.keySet().forEach(x-> map.get(x));
        for (Object object : set){

        }*/
    }




}