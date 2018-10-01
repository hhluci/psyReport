package com.grade.plugin;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description: 查询语句
 * @Date: Created in 19:12 2018/9/13
 */
public class Constant {

    /*当标准差为零时返回结果*/
    public static String INVALID_STANDARD_DEVIATION = "0";

    public static String findAll(String schoolId, String gradeId, String classId, String stuId){
        return "select distinct\n" +
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
        return "select distinct\n" +
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
        return "select distinct\n" +
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
        return "select distinct\n" +
                "    *\n" +
                "from tbproblembehavrep t\n" +
                "where t.schoolId = '" + schoolId + "' and t.gradeId = '" + gradeId + "' and t.classId = '" + classId + "'";
    }

    public static String findId(String schoolId, String gradeId, String classId){
        return "select distinct\n" +
                "    id\n" +
                "from tbproblembehavrep t\n" +
                "where t.schoolId = '" + schoolId + "' and t.gradeId = '" + gradeId + "' and t.classId = '" + classId + "'" +
                "group by t.stuId";
    }

    /**
     * 某个同学第二部分所有成绩
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param stuId
     * @return
     */
    public static String findSecondPart(String schoolId, String gradeId, String classId, String stuId){
        return "select\n" +
                "      a.paperCode,a.schoolId, a.gradeId,a.classId,a.stuId,a.queCode,a.queAns,a.queSeq,a.startTime,a.submitTime,\n" +
                "       b.option_number,\n" +
                "       q.mode_code, q.second_scale_type_code, q.third_scale_type_code,q.reverse_scoring,q.part\n" +
                "from answer a\n" +
                "    inner join test_table b on a.queCode = b.question_code and a.paperCode = b.EC_code\n" +
                "    inner join question_bank q on a.queCode = q.question_code\n" +
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "'\n" +
                "          and a.classId = '" + classId + "' and a.stuId = '" + stuId + "'\n" +
                "          and b.option_number = 7";
    }

    public static String findStuId(String schoolId, String gradeId, String classId){
        return "select distinct\n" +
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
                "      q.part\n" +
                "from answer a\n" +
                "    inner join test_table b on a.queCode = b.question_code and a.paperCode = b.EC_code\n" +
                "    inner join question_bank q on a.queCode = q.question_code\n" +
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "'\n" +
                "          and a.classId = '" + classId + "' and a.stuId = '" + stuId + "'\n" +
                "          and b.option_number = 7\n"+
                "group by q.part";
    }

    /**通过分组查询时间*/
    public static String findTestTime(String schoolId, String gradeId, String classId, String stuId){
        return "select\n" +
                "      a.startTime,a.submitTime,q.part\n" +
                "from answer a\n" +
                "    inner join test_table b on a.queCode = b.question_code and a.paperCode = b.EC_code\n" +
                "    inner join question_bank q on a.queCode = q.question_code\n" +
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "'\n" +
                "          and a.classId = '" + classId + "' and a.stuId = '" + stuId + "'\n" +
                "          and b.option_number = 7";
    }

    /**
     * 查询某个学校某个班级中摸个同学的所有成绩
     * @param schoolId
     * @param gradeId
     * @param classId
     * @return
     */
    public static String findStuAns(String schoolId, String gradeId, String classId, String stuId){
        return "select distinct\n" +
                "       a.paperCode,a.schoolId, a.gradeId,a.classId,a.stuId,a.queCode,a.queAns,a.queSeq,a.startTime,a.submitTime,\n" +
                "       b.option_number,\n" +
                "       q.mode_code, q.second_scale_type_code, q.third_scale_type_code,q.reverse_scoring\n" +
                "from answer a\n" +
                "    join test_table b on a.queCode = b.question_code\n" +
                "                       and a.paperCode = b.EC_code\n" +
                "    join question_bank q on a.queCode = q.question_code\n" +
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "'\n" +
                "          and a.classId = '" + classId + "' and a.stuId = '" + stuId + "'\n" +
                "          and b.option_number = 2 or b.option_number = 4";
    }

    /**
     * 查询每个班中学生id
     * @param schoolId
     * @param gradeId
     * @param classId
     * @return
     */
    public static String findStuIdList(String schoolId, String gradeId, String classId){
        return "select\n" +
                "       a.stuId\n" +
                "from answer a\n" +
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "'\n" +
                "    and a.classId = '" + classId + "'\n" +
                "group by a.stuId";
    }

    /**
     * 查询第一部分开始和结束时间
     *
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param stuId
     * @return
     */
    public static String findFirstTime(String schoolId, String gradeId, String classId, String stuId){
        return "select\n" +
                "       a.startTime,a.submitTime\n" +
                "from answer a\n" +
                "    inner join test_table b on a.queCode = b.question_code and a.paperCode = b.EC_code\n" +
                "    inner join question_bank q on a.queCode = q.question_code\n" +
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "'\n" +
                "          and a.classId = '" + classId + "' and a.stuId = '" + stuId + "'\n" +
                "          and b.option_number = 2 or b.option_number = 4\n" +
                "group by b.option_number";
    }

}
