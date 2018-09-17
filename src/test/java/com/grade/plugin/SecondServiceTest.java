package com.grade.plugin;

import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description:
 * @Date: Created in 17:03 2018/9/15
 */
public class SecondServiceTest {

    @Test
    public void firstPart() {

        new SecondService().firstPart("1","2","1");
        /*double[] a = new double[]{1,2,28,5,1,5,21,5,5,51,5,5,15};
        List<Double> aa = new ArrayList<>();
        for (int i = 0; i< 10; i++){
            aa.add((double) (i+2));
        }
        System.out.println(aa.stream().collect(Collectors.averagingDouble(Double::doubleValue)));
*/
    }

    @Test
    public void test(){
        List<Record> list = new Dao().find(SQL.findSecondPart("1","2","1", "7"));
        List<Record> list1 = list.stream().filter(x -> x.getStr("question_code").contains("-1")).collect(Collectors.toList());
        List<Record> list2 = list.stream().filter(x -> x.getStr("question_code").contains("-2")).collect(Collectors.toList());
        double[] replace_a = ArrayUtils.toPrimitive(list.stream().filter(x -> x.getStr("question_code").contains("-1")).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().toArray(Double[]::new));
    System.out.println(list1);
    System.out.println("-------------------------------------");
    System.out.println(list2);
        System.out.println("-------------------------------------");

    System.out.println(list.get(0).getStr("startTime") +"---"+ list.get(0).getStr("submitTime"));
    }
}