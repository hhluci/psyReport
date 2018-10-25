package com.grade.plugin;

import com.jfinal.plugin.activerecord.Record;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description:
 * @Date: Created in 17:37 2018/10/23
 */
public class ClassActionServiceTest {

    Dao dao = new Dao("ClassTest");

    @Test
    public void getClassAction() {
        new ClassActionService().getClassAction("14", "19", "67");
    }

    @Test
    public void testDao(){
        List<Record> promble = dao.find(Constant.findClassActionProblem("14", "19", "67"));
        //System.out.println(dao.find(Constant.findClassActionProblem("14", "19", "67")).size());
        //System.out.println(dao.find(Constant.findClassStudentInfomation("14", "19", "67")).size());
        //System.out.println(dao.find(Constant.findClassStudentBirthdayRange("14", "19", "67")).size());
        List<Record> allStuBirthdayRange = dao.find(Constant.findClassStudentBirthdayRange("14", "19", "67"));

        long count = dao.find(Constant.findClassStudentInfomation("14", "19", "67")).stream()
                .filter(x -> "男".equals(x.getStr("gender"))).count();

        String testDate = promble.get(0).getStr("testDate").substring(0, promble.get(0).getStr("testDate").lastIndexOf(" "));

        long deletionCount = promble.stream().filter(x -> compareTo(x.getStr("missRate"))).count();

        List<String> missRateList = promble.stream().filter(x -> !compareTo(x.getStr("missRate"))).map(x -> x.getStr("stuId")).collect(Collectors.toList());

        long testTime = promble.stream().filter(x -> compareForTestTime(x.getStr("elapsedTime"))).count();

        long L = promble.stream().filter(x -> compareForL(x.getStr("idxL"))).count();

        System.out.println(allStuBirthdayRange.get(0).getStr("min").substring(0,4) + "-" + allStuBirthdayRange.get(0).getStr("max").substring(0,4));



    }

    private boolean compareTo(String percent){
        double compareBy = Double.parseDouble(percent.substring(0, percent.lastIndexOf("%")));
        return compareBy <= 33.3;
    }

    /**
     * 比较学生作答时间不足450s
     * @param time
     * @return
     */
    private boolean compareForTestTime(String time){
        return Integer.parseInt(time) * 60 < 450;
    }

    /**
     * 比较学生L成绩在 [0, 12]范围中
     * @param LScore
     * @return
     */
    private boolean compareForL(String LScore){
        return 0.00 <= Double.parseDouble(LScore) && Double.parseDouble(LScore) <= 12.00;
    }

    @Test
    public void test1(){
        System.out.println(12 + ":" + 4);
    }
}