package com.grade.plugin;

import com.jfinal.plugin.activerecord.Record;
import org.junit.Test;

import java.util.List;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description:
 * @Date: Created in 9:13 2018/9/20
 */
public class FirstServiceTest {


    @Test
    public void firstPart() {
        //new FirstService().firstPart("15", "21", "110");
        //new FirstService().firstPart("15", "25", "168");
    }

    @Test
    public void getScore(){
        Dao dao = new Dao("test");
        //FirstService firstService = new FirstService();
       // List<Record> all = dao.find(Constant.findStuAns("6", "7", "7", "220174"));
        //System.out.println(firstService.getScoreList(all, "10602", "10604").size());
        //System.out.println(all);
        //System.out.println(firstService.getCScoreList(all).size());


    }
}