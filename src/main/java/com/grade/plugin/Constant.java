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

    public static String findStuAns1(String schoolId, String gradeId, String classId, String stuId){
        return "select distinct\n" +
                "      a.paperCode,a.schoolId, a.gradeId,a.classId,a.stuId,a.queCode,a.queAns,a.queSeq,a.startTime,a.submitTime,\n" +
                "       b.option_number,\n" +
                "       q.mode_code, q.second_scale_type_code, q.third_scale_type_code,q.reverse_scoring,q.part\n" +
                "from answer a\n" +
                "    inner join test_table b on a.queCode = b.question_code and a.paperCode = b.EC_code\n" +
                "    inner join question_bank q on a.queCode = q.question_code and a.stuId = '" + stuId + "'\n" +
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "'\n" +
                "          and a.classId = '" + classId + "'\n" +
                "          and b.option_number = 2 or b.option_number = 4\n" +
                ";";
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

    /**
     * 查询学生完整作答时间
     * @param schoolId
     * @param gradeId
     * @param classId
     * @param stuId
     * @return
     */
    public static String findStuElapsedTime(String schoolId, String gradeId, String classId, String stuId){
        return "select\n" +
                "      MIN(a.startTime) AS 'startTime' ,MAX(a.submitTime) AS 'submitTime'\n" +
                "from answer a\n" +
                "join test_table b on a.queCode = b.question_code and a.paperCode = b.EC_code\n" +
                "                            and b.option_number != '7'\n"+
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "'\n" +
                "          and a.classId = '" + classId + "'" + "and a.stuId = '" + stuId + "'";
    }

    /**
     * 查询当天测试学校编码
     * @return
     */
    public static String findThisDayTestSchool(){
        return "SELECT\n" +
                "\ta.schoolId\n" +
                "FROM\n" +
                "\tanswer a\n" +
                "WHERE\n" +
                "  TO_DAYS(submitTime) = TO_DAYS(NOW())\n" +
                "GROUP BY schoolId";
    }

    /**
     * 查询当天测试的年纪代码
     * @param schoolId
     * @return
     */
    public static String findThisDayTestGrade(String schoolId){
        return "SELECT\n" +
                "\ta.gradeId\n" +
                "FROM\n" +
                "\tanswer a\n" +
                "WHERE\n" +
                "  TO_DAYS(submitTime) = TO_DAYS(NOW()) AND a.schoolId = '" + schoolId + "'\n" +
                "GROUP BY gradeId";
    }

    /**
     * 查询当天测试的班级代码
     * @param schoolId
     * @param gradeId
     * @return
     */
    public static String findThisDayTestClassId(String schoolId, String gradeId){
        return "SELECT\n" +
                "\ta.classId\n" +
                "FROM\n" +
                "\tanswer a\n" +
                "WHERE\n" +
                "  TO_DAYS(submitTime) = TO_DAYS(NOW()) \n" +
                "  AND a.schoolId = '" + schoolId + "' AND a.gradeId = '" + gradeId + "'\n" +
                "GROUP BY classId";
    }

    /**
     * 查询某个班级学生的问题行为的所有成绩
     * @param schoolId
     * @param gradeId
     * @param classId
     * @return
     */
    public static String findClassActionProblem(String schoolId, String gradeId, String classId){
        return "select\n" +
                "    *\n" +
                "from tbproblembehavrep\n" +
                "where schoolId = '" + schoolId + "' and gradeId = '" + gradeId + "'\n" +
                "      and classId = '" + classId + "'";
    }

    /**
     * 查询某个班级中学生信息
     * @param schoolId
     * @param gradeId
     * @param classId
     * @return
     */
    public static String findClassStudentInfomation(String schoolId, String gradeId, String classId){
        return "select\n" +
                "    studentNO, name, gender\n" +
                "from student\n" +
                "where schoolID = '" + schoolId + "' and gradeID = '" + gradeId + "'\n" +
                "       and classID = '" + classId + "'";
    }

    /**
     * 查询某个班级中学生生日的最大值和最小值
     * @param schoolId
     * @param gradeId
     * @param classId
     * @return
     */
    public static String findClassStudentBirthdayRange(String schoolId, String gradeId, String classId){
        return "select\n" +
                "    min(birthday) as min ,\n" +
                "    max(birthday) as max\n" +
                "from student\n" +
                "where schoolID = '" + schoolId + "' and gradeID = '" + gradeId + "'\n" +
                "       and classID = '" + classId + "'";
    }

    /**
     * 查询某个班所有学生积极心理品质成绩
     * @param schoolId
     * @param gradeId
     * @param classId
     * @return
     */
    public static String findClassPsychology(String schoolId, String gradeId, String classId){
        return "select\n" +
                "  *\n" +
                "from tbpsyqualityrep\n" +
                "where schoolId = '" + schoolId + "' and gradeId = '" + gradeId + "'\n" +
                "  and classId = '" + classId + "'";
    }

    /**
     * 查询某个班级参加考试中男生人数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @return
     */
    public static String findTestStuSexManNum(String schoolId, String gradeId, String classId){
        return "select\n" +
                "    count(gender) as man\n" +
                "from tbproblembehavrep a\n" +
                "    inner join student b on a.stuId = b.studentNO\n" +
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "' and a.classId = '" + classId + "'\n" +
                "       and b.gender = '男'";
    }

    /**
     * 查询某个班级参加考试中女生人数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @return
     */
    public static String findTestStuSexWomanNum(String schoolId, String gradeId, String classId){
        return "select\n" +
                "    count(gender) as woman\n" +
                "from tbproblembehavrep a\n" +
                "    inner join student b on a.stuId = b.studentNO\n" +
                "where a.schoolId = '" + schoolId + "' and a.gradeId = '" + gradeId + "' and a.classId = '" + classId + "'\n" +
                "       and b.gender = '女'";
    }

}
