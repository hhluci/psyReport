package com.grade.plugin;

import com.jfinal.plugin.activerecord.Record;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.RecursiveAction;

import static org.junit.Assert.*;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description:
 * @Date: Created in 21:39 2018/9/30
 */

public class SubdimensionTest {

    Dao dao = new Dao("subdimension");
    Subdimension subdimension = new Subdimension();

    String sql = "select\n" +
            "      a.paperCode,a.schoolId, a.gradeId,a.classId,a.stuId,a.queCode,a.queAns,a.queSeq,a.startTime,a.submitTime,\n" +
            "       b.option_number,\n" +
            "       q.mode_code, q.second_scale_type_code, q.third_scale_type_code,q.reverse_scoring,q.part\n" +
            "from answer a\n" +
            "    inner join test_table b on a.queCode = b.question_code and a.paperCode = b.EC_code\n" +
            "    inner join question_bank q on a.queCode = q.question_code\n" +
            "where a.schoolId = '6' and a.gradeId = '7'\n" +
            "          and a.classId = '7' and a.stuId = '220174'\n" +
            "          and b.option_number = 7 and q.part = 5";

    @Test
    public void rectifyScoreFinal(){

        //List<Record> temp = dao.find(sql);
        //System.out.println("--------------------------");
        //System.out.println(temp);
        //System.out.println(subdimension.rectifyScoreFinal(temp).size());
        double[] temp = new double[]{4.78, 4.06};
        System.out.println(subdimension.getStandardDeviation(temp));
    }

}