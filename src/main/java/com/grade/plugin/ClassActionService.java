package com.grade.plugin;

import com.jfinal.plugin.activerecord.Record;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description: 班级最终成绩
 * @Date: Created in 9:03 2018/10/16
 */
public class ClassActionService {

    final Dao dao = new Dao("ClassActionService");

    /**
     * 计算班级问题行为报告
     * @param schoolId
     * @param gradeId
     * @param classId
     */
    public void getClassAction(String schoolId, String gradeId, String classId){

        /*该班级中所有学生问题行为成绩*/
        List<Record> allStuActionProblem = dao.find(Constant.findClassActionProblem(schoolId, gradeId, classId));
        /*该班级所有学生信息*/
        List<Record> allStuInfo = dao.find(Constant.findClassStudentInfomation(schoolId, gradeId, classId));
        /*该班级学生出生日期范围*/
        List<Record> allStuBirthdayRange = dao.find(Constant.findClassStudentBirthdayRange(schoolId, gradeId, classId));

        /*答题人数*/
        int testStu = allStuActionProblem.size();
        /*班级总人数*/
        int sumStu = allStuInfo.size();
        /*答题人数中男生人数*/
        long numSexMan = allStuInfo.stream().filter(x -> "男".equals(x.getStr("gender"))).count();
        /*答题人数中女生人数*/
        long numSexWoman = allStuInfo.stream().filter(x -> "女".equals(x.getStr("gender"))).count();
        /*测试时间*/
        String testTime = allStuActionProblem.get(0).getStr("testDate").substring(0, allStuActionProblem.get(0)
                .getStr("testDate").lastIndexOf(" "));
        /**作答缺失*/
        /*作答缺失人数*/
        long deletionNum = allStuActionProblem.stream().filter(x -> compareTo(x.getStr("missRate"))).count();
        /*有嫌疑学生集合*/
        List<String> deletionList = allStuActionProblem.stream().filter(x -> compareTo(x.getStr("missRate")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*作答缺失率*/
        String deletionPrecent = new DecimalFormat("0.0").format((double) (testStu - deletionNum)/testStu *100) + "%";
        /**作答时间*/
        /*作答时间不足450s的学生人数*/
        long lessTimeStuNum = allStuActionProblem.stream().filter(x -> compareForTestTime(x.getStr("elapsedTime"))).count();
        /*作答时间为满足450s的学生列表*/
        List<String> lessTimeList = allStuActionProblem.stream().filter(x -> compareForTestTime(x.getStr("elapsedTime")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*作答时间大于等于450s学生人数比例*/
        String lessTimePrecent = new DecimalFormat("0.0").format((double)(testStu - lessTimeStuNum)/testStu * 100) + "%";
        /**L指数*/
        /*有嫌疑人数*/
        long Lmunber = allStuActionProblem.stream().filter(x -> compareForL(x.getStr("idxL"))).count();
        /*L指数有嫌疑学生id列表*/
        List<String> LMissList = allStuActionProblem.stream().filter(x -> compareForL(x.getStr("idxL")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*L指数无嫌疑比例*/
        String LMissPrecent = new DecimalFormat("0.0").format((double)(testStu - Lmunber) / testStu *100) + "%";
        /**F指数*/
        /*有嫌疑人数*/
        long Fmunber = allStuActionProblem.stream().filter(x -> compareForF(x.getStr("idxF"))).count();
        /*F指数有嫌疑学生id列表*/
        List<String> FMissList = allStuActionProblem.stream().filter(x -> compareForF(x.getStr("idxF")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*F指数无嫌疑比例*/
        String FMissPrecent = new DecimalFormat("0.0").format((double)(testStu - Fmunber) / testStu *100) + "%";
        /**C指数*/
        /*有嫌疑人数*/
        long Cmunber = allStuActionProblem.stream().filter(x -> compareForC(x.getStr("idxC"))).count();
        /*C指数有嫌疑学生id列表*/
        List<String> CMissList = allStuActionProblem.stream().filter(x -> compareForC(x.getStr("idxC")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*C指数无嫌疑比例*/
        String CMissPrecent = new DecimalFormat("0.0").format((double)(testStu - Cmunber) / testStu *100) + "%";

        /**子维度*/

        /**焦虑*/
        /*不存在风险人数*/
        long anxiousNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("anxiety"))).count();
        /*潜在风险人数*/
        long anxiousPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("anxiety"))).count();
        /*存在风险人数*/
        long anxiousRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("anxiety"))).count();
        /*潜在风险学生id*/
        List<String> anxiousPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("anxiety")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> anxiousRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("anxiety")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**抑郁*/
        /*不存在风险人数*/
        long depressedNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("depression"))).count();
        /*潜在风险人数*/
        long depressedPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("depression"))).count();
        /*存在风险人数*/
        long depressedRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("depression"))).count();
        /*潜在风险学生id*/
        List<String> depressedPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("depression")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> depressedRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("depression")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**躯体化*/
        /*不存在风险人数*/
        long somatizationNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("somatization"))).count();
        /*潜在风险人数*/
        long somatizationPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("somatization"))).count();
        /*存在风险人数*/
        long somatizationRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("somatization"))).count();
        /*潜在风险学生id*/
        List<String> somatizationPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("somatization")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> somatizationRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("somatization")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**无控制感*/
        /*不存在风险人数*/
        long uncontrolNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("uncontroll"))).count();
        /*潜在风险人数*/
        long uncontrolPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("uncontroll"))).count();
        /*存在风险人数*/
        long uncontrolRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("uncontroll"))).count();
        /*潜在风险学生id*/
        List<String> uncontrolPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("uncontroll")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> uncontrolRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("uncontroll")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**失败感*/
        /*不存在风险人数*/
        long failNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("fail"))).count();
        /*潜在风险人数*/
        long failPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("fail"))).count();
        /*存在风险人数*/
        long failRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("fail"))).count();
        /*潜在风险学生id*/
        List<String> failPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("fail")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> failRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("fail")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**不寻常行为*/
        /*不存在风险人数*/
        long oddbehaviorNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("oddBehav"))).count();
        /*潜在风险人数*/
        long oddbehaviorPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("oddBehav"))).count();
        /*存在风险人数*/
        long oddbehaviorRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("oddBehav"))).count();
        /*潜在风险学生id*/
        List<String> oddbehaviorPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("oddBehav")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> oddbehaviorRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("oddBehav")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**社会压力*/
        /*不存在风险人数*/
        long pressureNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("socialPress"))).count();
        /*潜在风险人数*/
        long pressurePotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("socialPress"))).count();
        /*存在风险人数*/
        long pressureRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("socialPress"))).count();
        /*潜在风险学生id*/
        List<String> pressurePotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("socialPress")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> pressureRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("socialPress")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**交往退缩*/
        /*不存在风险人数*/
        long degenerateNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("degenerate"))).count();
        /*潜在风险人数*/
        long degeneratePotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("degenerate"))).count();
        /*存在风险人数*/
        long degenerateRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("degenerate"))).count();
        /*潜在风险学生id*/
        List<String> degeneratePotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("degenerate")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> degenerateRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("degenerate")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**欺负*/
        /*不存在风险人数*/
        long bullyNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("bully"))).count();
        /*潜在风险人数*/
        long bullyPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("bully"))).count();
        /*存在风险人数*/
        long bullyRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("bully"))).count();
        /*潜在风险学生id*/
        List<String> bullyPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("bully")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> bullyRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("bully")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**注意力不集中*/
        /*不存在风险人数*/
        long inattentionNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("inattention"))).count();
        /*潜在风险人数*/
        long inattentionPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("inattention"))).count();
        /*存在风险人数*/
        long inattentionRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("inattention"))).count();
        /*潜在风险学生id*/
        List<String> inattentionPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("inattention")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> inattentionRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("inattention")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**狂躁*/
        /*不存在风险人数*/
        long maniaNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("mania"))).count();
        /*潜在风险人数*/
        long maniaPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("mania"))).count();
        /*存在风险人数*/
        long maniaRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("mania"))).count();
        /*潜在风险学生id*/
        List<String> maniaPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("mania")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> maniaRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("mania")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**网络成瘾*/
        /*不存在风险人数*/
        long netaddictionNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("netAddiction"))).count();
        /*潜在风险人数*/
        long netaddictionPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("netAddiction"))).count();
        /*存在风险人数*/
        long netaddictionRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("netAddiction"))).count();
        /*潜在风险学生id*/
        List<String> netaddictionPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("netAddiction")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> netaddictionRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("netAddiction")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**手机成瘾*/
        /*不存在风险人数*/
        long phoneaddictionNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("phoneAddiction"))).count();
        /*潜在风险人数*/
        long phoneaddictionPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("phoneAddiction"))).count();
        /*存在风险人数*/
        long phoneaddictionRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("phoneAddiction"))).count();
        /*潜在风险学生id*/
        List<String> phoneaddictionPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("phoneAddiction")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> phoneaddictionRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("phoneAddiction")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**讨厌学校*/
        /*不存在风险人数*/
        long hateschoolNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("hateSchool"))).count();
        /*潜在风险人数*/
        long hateschoolPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("hateSchool"))).count();
        /*存在风险人数*/
        long hateschoolRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("hateSchool"))).count();
        /*潜在风险学生id*/
        List<String> hateschoolPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("hateSchool")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> hateschoolRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("hateSchool")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**讨厌老师*/
        /*不存在风险人数*/
        long hateteacherNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("hateTeacher"))).count();
        /*潜在风险人数*/
        long hateteacherPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("hateTeacher"))).count();
        /*存在风险人数*/
        long hateteacherRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("hateTeacher"))).count();
        /*潜在风险学生id*/
        List<String> hateteacherPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("hateTeacher")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> hateteacherRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("hateTeacher")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**讨厌学习*/
        /*不存在风险人数*/
        long hatestudyNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("hateStudy"))).count();
        /*潜在风险人数*/
        long hatestudyPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("hateStudy"))).count();
        /*存在风险人数*/
        long hatestudyRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("hateStudy"))).count();
        /*潜在风险学生id*/
        List<String> hatestudyPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("hateStudy")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> hatestudyRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("hateStudy")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**考试焦虑*/
        /*不存在风险人数*/
        long anxexamNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskSubDimension(x.getStr("anxExam"))).count();
        /*潜在风险人数*/
        long anxexamPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("anxExam"))).count();
        /*存在风险人数*/
        long anxexamRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("anxExam"))).count();
        /*潜在风险学生id*/
        List<String> anxexamPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskSubDimension(x.getStr("anxExam")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> anxexamRiskList = allStuActionProblem.stream().filter(x -> compareForRiskSubDimension(x.getStr("anxExam")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**指数*/
        /**自伤指数*/
        /*不存在风险人数*/
        long autolesionidxNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskIndex(x.getStr("autolesionIdx"))).count();
        /*潜在风险人数*/
        long autolesionidxPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("autolesionIdx"))).count();
        /*存在风险人数*/
        long autolesionidxRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("autolesionIdx"))).count();
        /*潜在风险学生id*/
        List<String> autolesionidxPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("autolesionIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> autolesionidxRiskList = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("autolesionIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**无助感指数*/
        /*不存在风险人数*/
        long helplessnessidxNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskIndex(x.getStr("HelplessnessIdx"))).count();
        /*潜在风险人数*/
        long helplessnessidxPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("HelplessnessIdx"))).count();
        /*存在风险人数*/
        long helplessnessidxRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("HelplessnessIdx"))).count();
        /*潜在风险学生id*/
        List<String> helplessnessidxPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("HelplessnessIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> helplessnessidxRiskList = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("HelplessnessIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**人际障碍指数*/
        /*不存在风险人数*/
        long interpersonalidxNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskIndex(x.getStr("interpersonalIdx"))).count();
        /*潜在风险人数*/
        long interpersonalidxPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("interpersonalIdx"))).count();
        /*存在风险人数*/
        long interpersonalidxRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("interpersonalIdx"))).count();
        /*潜在风险学生id*/
        List<String> interpersonalidxPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("interpersonalIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> interpersonalidxRiskList = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("interpersonalIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**成瘾指数*/
        /*不存在风险人数*/
        long addictionidxNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskIndex(x.getStr("addictionIdx"))).count();
        /*潜在风险人数*/
        long addictionidxPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("addictionIdx"))).count();
        /*存在风险人数*/
        long addictionidxRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("addictionIdx"))).count();
        /*潜在风险学生id*/
        List<String> addictionidxPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("addictionIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> addictionidxRiskList = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("addictionIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**受欺负指数*/
        /*不存在风险人数*/
        long bullyidxNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskIndex(x.getStr("bullyIdx"))).count();
        /*潜在风险人数*/
        long bullyidxPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("bullyIdx"))).count();
        /*存在风险人数*/
        long bullyidxRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("bullyIdx"))).count();
        /*潜在风险学生id*/
        List<String> bullyidxPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("bullyIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> bullyidxRiskList = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("bullyIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**行为失常指数*/
        /*不存在风险人数*/
        long behavidxNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskIndex(x.getStr("behavIdx"))).count();
        /*潜在风险人数*/
        long behavidxPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("behavIdx"))).count();
        /*存在风险人数*/
        long behavidxRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("behavIdx"))).count();
        /*潜在风险学生id*/
        List<String> behavidxPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("behavIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> behavidxRiskList = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("behavIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**狂躁指数*/
        /*不存在风险人数*/
        long maniaidxNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskIndex(x.getStr("maniaIdx"))).count();
        /*潜在风险人数*/
        long maniaidxPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("maniaIdx"))).count();
        /*存在风险人数*/
        long maniaidxRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("maniaIdx"))).count();
        /*潜在风险学生id*/
        List<String> maniaidxPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("maniaIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> maniaidxRiskList = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("maniaIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**身体不适指数*/
        /*不存在风险人数*/
        long poorhealthidxNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskIndex(x.getStr("poorHealthIdx"))).count();
        /*潜在风险人数*/
        long poorhealthidxPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("poorHealthIdx"))).count();
        /*存在风险人数*/
        long poorhealthidxRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("poorHealthIdx"))).count();
        /*潜在风险学生id*/
        List<String> poorhealthidxPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("poorHealthIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> poorhealthidxRiskList = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("poorHealthIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**厌学指数*/
        /*不存在风险人数*/
        long wearinessidxNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskIndex(x.getStr("WearinessIdx"))).count();
        /*潜在风险人数*/
        long wearinessidxPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("WearinessIdx"))).count();
        /*存在风险人数*/
        long wearinessidxRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("WearinessIdx"))).count();
        /*潜在风险学生id*/
        List<String> wearinessidxPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("WearinessIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> wearinessidxRiskList = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("WearinessIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**分心指数*/
        /*不存在风险人数*/
        long distractionidxNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskIndex(x.getStr("DistractionIdx"))).count();
        /*潜在风险人数*/
        long distractionidxPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("DistractionIdx"))).count();
        /*存在风险人数*/
        long distractionidxRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("DistractionIdx"))).count();
        /*潜在风险学生id*/
        List<String> distractionidxPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("DistractionIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> distractionidxRiskList = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("DistractionIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**考试焦虑指数*/
        /*不存在风险人数*/
        long anxexamidxNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskIndex(x.getStr("anxExamIdx"))).count();
        /*潜在风险人数*/
        long anxexamidxPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("anxExamIdx"))).count();
        /*存在风险人数*/
        long anxexamidxRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("anxExamIdx"))).count();
        /*潜在风险学生id*/
        List<String> anxexamidxPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("anxExamIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> anxexamidxRiskList = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("anxExamIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /**师生冲突指数*/
        /*不存在风险人数*/
        long conflictidxNoriskNum = allStuActionProblem.stream().filter(x -> compareForNoRiskIndex(x.getStr("conflictIdx"))).count();
        /*潜在风险人数*/
        long conflictidxPotentialRiskNum = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("conflictIdx"))).count();
        /*存在风险人数*/
        long conflictidxRiskNum = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("conflictIdx"))).count();
        /*潜在风险学生id*/
        List<String> conflictidxPotentialRiskList = allStuActionProblem.stream().filter(x -> compareForPotentialRiskIndex(x.getStr("conflictIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*存在风险学生id*/
        List<String> conflictidxRiskList = allStuActionProblem.stream().filter(x -> compareForRiskIndex(x.getStr("conflictIdx")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        Record result = new Record();
        result.set("school_id", schoolId)
                .set("grade_id", gradeId)
                .set("class_id", classId)
                .set("test_stu_percent", testStu + ":" + sumStu)
                .set("stu_sex_percent", numSexMan + ":" + numSexWoman)
                .set("stu_birthday_rang", allStuBirthdayRange.get(0).getStr("min").substring(0,4) + "-" + allStuBirthdayRange.get(0).getStr("max").substring(0,4))
                .set("test_date", testTime)

                .set("test_deficiency_percent", deletionPrecent)
                .set("test_deficiency_num", deletionNum)
                .set("test_deficiency_list", deletionList.toString())
                .set("test_time_percent", lessTimePrecent)
                .set("test_time_num", lessTimeStuNum)
                .set("test_time_list", lessTimeList.toString())

                .set("available_l_percent", LMissPrecent)
                .set("available_l_num", Lmunber)
                .set("available_l_list", LMissList.toString())

                .set("available_f_percent", FMissPrecent)
                .set("available_f_num", Fmunber)
                .set("available_f_list", FMissList.toString())

                .set("available_c_percent", CMissPrecent)
                .set("available_c_num", Cmunber)
                .set("available_c_list", CMissList.toString())

                .set("anxious_norisk_num", anxiousNoriskNum)
                .set("anxious_potentialrisk_num", anxiousPotentialRiskNum)
                .set("anxious_risk_num", anxiousRiskNum)
                .set("anxious_potentialrisk_list", anxiousPotentialRiskList.toString())
                .set("anxious_risk_list", anxiousRiskList.toString())

                .set("depressed_norisk_num", depressedNoriskNum)
                .set("depressed_potentialrisk_num", depressedPotentialRiskNum)
                .set("depressed_risk_num", depressedRiskNum)
                .set("depressed_potentialrisk_list", depressedPotentialRiskList.toString())
                .set("depressed_risk_list", depressedRiskList.toString())

                .set("somatization_norisk_num", somatizationNoriskNum)
                .set("somatization_potentialrisk_num", somatizationPotentialRiskNum)
                .set("somatization_risk_num", somatizationRiskNum)
                .set("somatization_potentialrisk_list", somatizationPotentialRiskList.toString())
                .set("somatization_risk_list", somatizationRiskList.toString())

                .set("uncontrol_norisk_num", uncontrolNoriskNum)
                .set("uncontrol_potentialrisk_num", uncontrolPotentialRiskNum)
                .set("uncontrol_risk_num", uncontrolRiskNum)
                .set("uncontrol_potentialrisk_list", uncontrolPotentialRiskList.toString())
                .set("uncontrol_risk_list", uncontrolRiskList.toString())

                .set("fail_norisk_num", failNoriskNum)
                .set("fail_potentialrisk_num", failPotentialRiskNum)
                .set("fail_risk_num", failRiskNum)
                .set("fail_potentialrisk_list", failPotentialRiskList.toString())
                .set("fail_risk_list", failRiskList.toString())

                .set("oddbehavior_norisk_num", oddbehaviorNoriskNum)
                .set("oddbehavior_potentialrisk_num", oddbehaviorPotentialRiskNum)
                .set("oddbehavior_risk_num", oddbehaviorRiskNum)
                .set("oddbehavior_potentialrisk_list", oddbehaviorPotentialRiskList.toString())
                .set("oddbehavior_risk_list", oddbehaviorRiskList.toString())

                .set("pressure_norisk_num", pressureNoriskNum)
                .set("pressure_potentialrisk_num", pressurePotentialRiskNum)
                .set("pressure_risk_num", pressureRiskNum)
                .set("pressure_potentialrisk_list", pressurePotentialRiskList.toString())
                .set("pressure_risk_list", pressureRiskList.toString())

                .set("degenerate_norisk_num", degenerateNoriskNum)
                .set("degenerate_potentialrisk_num", degeneratePotentialRiskNum)
                .set("degenerate_risk_num", degenerateRiskNum)
                .set("degenerate_potentialrisk_list", degeneratePotentialRiskList.toString())
                .set("degenerate_risk_list", degenerateRiskList.toString())

                .set("bully_norisk_num", bullyNoriskNum)
                .set("bully_potentialrisk_num", bullyPotentialRiskNum)
                .set("bully_risk_num", bullyRiskNum)
                .set("bully_potentialrisk_list", bullyPotentialRiskList.toString())
                .set("bully_risk_list", bullyRiskList.toString())

                .set("inattention_norisk_num", inattentionNoriskNum)
                .set("inattention_potentialrisk_num", inattentionPotentialRiskNum)
                .set("inattention_risk_num", inattentionRiskNum)
                .set("inattention_potentialrisk_list", inattentionPotentialRiskList.toString())
                .set("inattention_risk_list", inattentionRiskList.toString())

                .set("mania_norisk_num", maniaNoriskNum)
                .set("mania_potentialrisk_num", maniaPotentialRiskNum)
                .set("mania_risk_num", maniaRiskNum)
                .set("mania_potentialrisk_list", maniaPotentialRiskList.toString())
                .set("mania_risk_list", maniaRiskList.toString())

                .set("netaddiction_norisk_num", netaddictionNoriskNum)
                .set("netaddiction_potentialrisk_num", netaddictionPotentialRiskNum)
                .set("netaddiction_risk_num", netaddictionRiskNum)
                .set("netaddiction_potentialrisk_list", netaddictionPotentialRiskList.toString())
                .set("netaddiction_risk_list", netaddictionRiskList.toString())

                .set("phoneaddiction_norisk_num", phoneaddictionNoriskNum)
                .set("phoneaddiction_potentialrisk_num", phoneaddictionPotentialRiskNum)
                .set("phoneaddiction_risk_num", phoneaddictionRiskNum)
                .set("phoneaddiction_potentialrisk_list", phoneaddictionPotentialRiskList.toString())
                .set("phoneaddiction_risk_list", phoneaddictionRiskList.toString())

                .set("hateschool_norisk_num", hateschoolNoriskNum)
                .set("hateschool_potentialrisk_num", hateschoolPotentialRiskNum)
                .set("hateschool_risk_num", hateschoolRiskNum)
                .set("hateschool_potentialrisk_list", hateschoolPotentialRiskList.toString())
                .set("hateschool_risk_list", hateschoolRiskList.toString())

                .set("hateteacher_norisk_num", hateteacherNoriskNum)
                .set("hateteacher_potentialrisk_num", hateteacherPotentialRiskNum)
                .set("hateteacher_risk_num", hateteacherRiskNum)
                .set("hateteacher_potentialrisk_list", hateteacherPotentialRiskList.toString())
                .set("hateteacher_risk_list", hateteacherRiskList.toString())

                .set("hatestudy_norisk_num", hatestudyNoriskNum)
                .set("hatestudy_potentialrisk_num", hatestudyPotentialRiskNum)
                .set("hatestudy_risk_num", hatestudyRiskNum)
                .set("hatestudy_potentialrisk_list", hatestudyPotentialRiskList.toString())
                .set("hatestudy_risk_list", hatestudyRiskList.toString())

                .set("anxexam_norisk_num", anxexamNoriskNum)
                .set("anxexam_potentialrisk_num", anxexamPotentialRiskNum)
                .set("anxexam_risk_num", anxexamRiskNum)
                .set("anxexam_potentialrisk_list", anxexamPotentialRiskList.toString())
                .set("anxexam_risk_list", anxexamRiskList.toString())

                .set("autolesionidx_norisk_num", autolesionidxNoriskNum)
                .set("autolesionidx_potentialrisk_num", autolesionidxPotentialRiskNum)
                .set("autolesionidx_risk_num", autolesionidxRiskNum)
                .set("autolesionidx_potentialrisk_list", autolesionidxPotentialRiskList.toString())
                .set("autolesionidx_risk_list", autolesionidxRiskList.toString())

                .set("helplessnessidx_norisk_num", helplessnessidxNoriskNum)
                .set("helplessnessidx_potentialrisk_num", helplessnessidxPotentialRiskNum)
                .set("helplessnessidx_risk_num", helplessnessidxRiskNum)
                .set("helplessnessidx_potentialrisk_list", helplessnessidxPotentialRiskList.toString())
                .set("helplessnessidx_risk_list", helplessnessidxRiskList.toString())

                .set("interpersonalidx_norisk_num", interpersonalidxNoriskNum)
                .set("interpersonalidx_potentialrisk_num", interpersonalidxPotentialRiskNum)
                .set("interpersonalidx_risk_num", interpersonalidxRiskNum)
                .set("interpersonalidx_potentialrisk_list", interpersonalidxPotentialRiskList.toString())
                .set("interpersonalidx_risk_list", interpersonalidxRiskList.toString())

                .set("addictionidx_norisk_num", addictionidxNoriskNum)
                .set("addictionidx_potentialrisk_num", addictionidxPotentialRiskNum)
                .set("addictionidx_risk_num", addictionidxRiskNum)
                .set("addictionidx_potentialrisk_list", addictionidxPotentialRiskList.toString())
                .set("addictionidx_risk_list", addictionidxRiskList.toString())

                .set("bullyidx_norisk_num", bullyidxNoriskNum)
                .set("bullyidx_potentialrisk_num", bullyidxPotentialRiskNum)
                .set("bullyidx_risk_num", bullyidxRiskNum)
                .set("bullyidx_potentialrisk_list", bullyidxPotentialRiskList.toString())
                .set("bullyidx_risk_list", bullyidxRiskList.toString())

                .set("behavidx_norisk_num", behavidxNoriskNum)
                .set("behavidx_potentialrisk_num", behavidxPotentialRiskNum)
                .set("behavidx_risk_num", behavidxRiskNum)
                .set("behavidx_potentialrisk_list", behavidxPotentialRiskList.toString())
                .set("behavidx_risk_list", behavidxRiskList.toString())

                .set("maniaidx_norisk_num", maniaidxNoriskNum)
                .set("maniaidx_potentialrisk_num", maniaidxPotentialRiskNum)
                .set("maniaidx_risk_num", maniaidxRiskNum)
                .set("maniaidx_potentialrisk_list", maniaidxPotentialRiskList.toString())
                .set("maniaidx_risk_list", maniaidxRiskList.toString())

                .set("poorhealthidx_norisk_num", poorhealthidxNoriskNum)
                .set("poorhealthidx_potentialrisk_num", poorhealthidxPotentialRiskNum)
                .set("poorhealthidx_risk_num", poorhealthidxRiskNum)
                .set("poorhealthidx_potentialrisk_list", poorhealthidxPotentialRiskList.toString())
                .set("poorhealthidx_risk_list", poorhealthidxRiskList.toString())

                .set("wearinessidx_norisk_num", wearinessidxNoriskNum)
                .set("wearinessidx_potentialrisk_num", wearinessidxPotentialRiskNum)
                .set("wearinessidx_risk_num", wearinessidxRiskNum)
                .set("wearinessidx_potentialrisk_list", wearinessidxPotentialRiskList.toString())
                .set("wearinessidx_risk_list", wearinessidxRiskList.toString())

                .set("distractionidx_norisk_num", distractionidxNoriskNum)
                .set("distractionidx_potentialrisk_num", distractionidxPotentialRiskNum)
                .set("distractionidx_risk_num", distractionidxRiskNum)
                .set("distractionidx_potentialrisk_list", distractionidxPotentialRiskList.toString())
                .set("distractionidx_risk_list", distractionidxRiskList.toString())

                .set("anxexamidx_norisk_num", anxexamidxNoriskNum)
                .set("anxexamidx_potentialrisk_num", anxexamidxPotentialRiskNum)
                .set("anxexamidx_risk_num", anxexamidxRiskNum)
                .set("anxexamidx_potentialrisk_list", anxexamidxPotentialRiskList.toString())
                .set("anxexamidx_risk_list", anxexamidxRiskList.toString())

                .set("conflictidx_norisk_num", conflictidxNoriskNum)
                .set("conflictidx_potentialrisk_num", conflictidxPotentialRiskNum)
                .set("conflictidx_risk_num", conflictidxRiskNum)
                .set("conflictidx_potentialrisk_list", conflictidxPotentialRiskList.toString())
                .set("conflictidx_risk_list", conflictidxRiskList.toString());

        dao.saveClassAction(result);


    }

    /**
     * 比较学生作答缺失率
     * @param percent
     * @return
     */
    private boolean compareTo(String percent){
        double compareBy = Double.parseDouble(percent.substring(0, percent.lastIndexOf("%")));
        return compareBy > 33.3;
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
     * 比较学生L成绩在 [13, 18]范围中
     * @param LScore
     * @return
     */
    private boolean compareForL(String LScore){
        return 13.00 <= Double.parseDouble(LScore) && Double.parseDouble(LScore) <= 18.00;
    }

    /**
     * 比较学生F成绩在 [10, 15]范围中
     * @param FScore
     * @return
     */
    private boolean compareForF(String FScore){
        return 10.00 <= Double.parseDouble(FScore) && Double.parseDouble(FScore) <= 15.00;
    }

    /**
     * 比较学生C成绩在 [6, 10]范围中
     * @param CScore
     * @return
     */
    private boolean compareForC(String CScore){
        return 6.00 <= Double.parseDouble(CScore) && Double.parseDouble(CScore) <= 10.00;
    }

    /**
     * 子维度无风险比较
     * @param score
     * @return
     */
    private boolean compareForNoRiskSubDimension(String score){
        return 0.00 <= Double.parseDouble(score) && Double.parseDouble(score) < 60.00;
    }

    /**
     * 子维度潜在风险比较
     * @param score
     * @return
     */
    private boolean compareForPotentialRiskSubDimension(String score){
        return 60.00 <= Double.parseDouble(score) && Double.parseDouble(score) < 70.00;
    }

    /**
     * 子维度存在风险比较
     * @param score
     * @return
     */
    private boolean compareForRiskSubDimension(String score){
        return Double.parseDouble(score) >= 70.00;
    }

    /**
     * 指数不存在风险比较
     * @param score
     * @return
     */
    private boolean compareForNoRiskIndex(String score){
        return Double.parseDouble(score) < 6.0;
    }

    /**
     * 指数潜在风险比较
     * @param score
     * @return
     */
    private boolean compareForPotentialRiskIndex(String score){
        return 6.0 <= Double.parseDouble(score) && Double.parseDouble(score) < 7.0;
    }

    /**
     * 指数存在风险比较
     * @param score
     * @return
     */
    private boolean compareForRiskIndex(String score){
        return Double.parseDouble(score) >= 7.0;
    }
}
