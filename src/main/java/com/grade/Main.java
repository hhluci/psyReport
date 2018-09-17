package com.grade;

import com.grade.plugin.FirstService;
import com.grade.plugin.SecondService;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade
 * @Description:
 * @Date: Created in 21:46 2018/9/17
 */
public class Main {

    public static void main(String schoolId, String gradeId, String classId) {
        new FirstService().firstPart(schoolId, gradeId, classId);
        new SecondService().firstPart(schoolId, gradeId, classId);
    }

}
