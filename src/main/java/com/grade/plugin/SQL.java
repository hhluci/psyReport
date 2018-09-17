package com.grade.plugin;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description: 查询语句
 * @Date: Created in 19:12 2018/9/13
 */
public class SQL {

    public static String findAll(String schoolId, String gradeId, String classId, String stuId){
        return "select\n" +
                "    a.schoolId, a.gradeId, a.classId, a.stuId, a.queAns, a.queSeq, a.startTime, a.submitTime,\n" +
                "    b.mode_code, b.second_scale_type_code, b.third_scale_type_code, b.question_code, b.option_number, b.reverse_scoring,\n" +
                "    e.part\n" +
                "from answer a\n" +
                "    inner join question_bank b on a.queCode = b.question_code\n" +
                "    inner join eva_com_code_table e on a.queCode = e.question_code\n" +
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "'" +
                "  and a.classId = '" + classId+ "' and a.stuId = '" + stuId + "'";
    }

    public static String findFirstAll(String schoolId, String gradeId, String classId, String stuId){
        return "select\n" +
                "    a.schoolId, a.gradeId, a.classId, a.stuId, a.queAns, a.queSeq, a.startTime, a.submitTime,a.paperCode,\n" +
                "    b.mode_code, b.second_scale_type_code, b.third_scale_type_code, b.question_code, b.option_number, b.reverse_scoring,\n" +
                "    e.part\n" +
                "from answer a\n" +
                "   inner join question_bank b on a.queCode = b.question_code\n" +
                "   inner join eva_com_code_table e on a.queCode = e.question_code\n" +
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "'\n" +
                "  and a.classId = '" + classId + "' and a.stuId = '" + stuId + "'\n" +
                "  and e.part = 1 or e.part = 2";
    }

    /**分组查询 通过试卷部分和试卷编号*/
    public static String findGroupbyPart(String schoolId, String gradeId, String classId, String stuId){
        return "select\n" +
                "    a.schoolId, a.gradeId, a.classId, a.stuId, a.queAns, a.queSeq, a.startTime, a.submitTime,a.paperCode,\n" +
                "    b.mode_code, b.second_scale_type_code, b.third_scale_type_code, b.question_code, b.option_number, b.reverse_scoring,\n" +
                "    e.part\n" +
                "from answer a\n" +
                "   inner join question_bank b on a.queCode = b.question_code\n" +
                "   inner join eva_com_code_table e on a.queCode = e.question_code\n" +
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "'\n" +
                "  and a.classId = '" + classId + "' and a.stuId = '" + stuId + "'\n" +
                "group by e.part";
    }

    public static String findGenerate(String schoolId, String gradeId, String classId){
        return "select\n" +
                "    *\n" +
                "from tbproblembehavrep t\n" +
                "where t.schoolId = '" + schoolId + "' and t.gradeId = '" + gradeId + "' and t.classId = '" + classId + "'";
    }

    public static String findId(String schoolId, String gradeId, String classId){
        return "select\n" +
                "    id\n" +
                "from tbproblembehavrep t\n" +
                "where t.schoolId = '" + schoolId + "' and t.gradeId = '" + gradeId + "' and t.classId = '" + classId + "'" +
                "group by t.stuId";
    }

    public static String findSecondPart(String schoolId, String gradeId, String classId, String stuId){
        return "select\n" +
                "    a.schoolId, a.gradeId, a.classId, a.stuId, a.queAns, a.queSeq, a.startTime, a.submitTime,a.paperCode,\n" +
                "    b.mode_code, b.third_scale_type_code, b.second_scale_type_code, b.question_code, b.option_number, b.reverse_scoring,\n" +
                "    e.part\n" +
                "from answer a\n" +
                "   inner join question_bank b on a.queCode = b.question_code\n" +
                "   inner join eva_com_code_table e on a.queCode = e.question_code\n" +
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "'\n" +
                "  and a.classId = '" + classId + "' and a.stuId = '" + stuId + "'\n" +
                "and e.part > 2";
    }

    public static String findStuId(String schoolId, String gradeId, String classId){
        return "select\n" +
                "    a.stuId\n" +
                "from answer a\n" +
                "   inner join question_bank b on a.queCode = b.question_code\n" +
                "   inner join eva_com_code_table e on a.queCode = e.question_code\n" +
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "'\n" +
                "  and a.classId = '" + classId + "'\n" +
                "group by a.stuId";
    }

    public static String findSecondPartGroupPart(String schoolId, String gradeId, String classId, String stuId){
        return "select\n" +
                "    e.part\n" +
                "from answer a\n" +
                "   inner join question_bank b on a.queCode = b.question_code\n" +
                "   inner join eva_com_code_table e on a.queCode = e.question_code\n" +
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "'\n" +
                "  and a.classId = '" + classId + "' and a.stuId = '" + stuId + "'\n" +
                "  and e.part > 2\n" +
                "group by e.part";
    }

}
