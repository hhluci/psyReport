package com.grade.plugin;

import com.grade.empty.JsonToObject;
import com.jfinal.plugin.activerecord.Record;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description: 班级积极心理品质报告
 * @Date: Created in 10:14 2018/10/29
 */
public class ClassPositivePsychology {

    private Dao dao;


    /**
     * 计算班级积极心理品质报告
     * @param schoolId
     * @param gradeId
     * @param classId
     */
    public void getClassPsychology(String schoolId, String gradeId, String classId, Dao settingDao){
        this.dao = settingDao;
        /*该班级中所有学生积极心理品质成绩*/
        List<Record> allStuPsychology = dao.find(Constant.findClassPsychology(schoolId, gradeId, classId));
        /*该班所有学生信息*/
        List<Record> allStuInfo = dao.find(Constant.findClassStudentInfomation(schoolId, gradeId, classId));
        /*该班所有学生出生日期范围*/
        List<Record> allStuBirthdayRange = dao.find(Constant.findClassStudentBirthdayRange(schoolId, gradeId, classId));
        /*该班级中所有学生人数*/
        int sumStu = allStuInfo.size();
        /*该班级中测试人数*/
        int testStu = allStuPsychology.size();
        /*答题人数中男生人数*/
        long numSexMan = dao.find(Constant.findTestStuSexManNum(schoolId, gradeId, classId)).get(0).getLong("man");
        /*答题人数中女生人数*/
        long numSexWoman = dao.find(Constant.findTestStuSexWomanNum(schoolId, gradeId, classId)).get(0).getLong("woman");
        /*测试时间*/
        String testTime = allStuPsychology.get(0).getStr("testDate").substring(0, allStuPsychology.get(0)
                .getStr("testDate").lastIndexOf(" "));

        /**作答风格分析*/
        /*评分准确性*/
        /*有嫌疑人数*/
        long accuracyNum = allStuPsychology.stream().filter(x -> compareForAccuracy(x.getStr("accuracy"))).count();
        /*有嫌疑学生列表*/
        List<String> accuracyList = allStuPsychology.stream().filter(x -> compareForAccuracy(x.getStr("accuracy")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*可靠程度*/
        String accuracyPercent = new DecimalFormat("0.0").format((double) (testStu - accuracyNum) / testStu * 100) + "%";

        /*评分倾向性*/
        /*有嫌疑人数*/
        long tendentiousnessNum = allStuPsychology.stream().filter(x -> compareForTendentiousness(x.getStr("tendentiousness"))).count();
        /*有嫌疑学生列表*/
        List<String> tendentiousnessList = allStuPsychology.stream().filter(x -> compareForTendentiousness(x.getStr("tendentiousness")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*可靠程度*/
        String tendentiousnessPercent = new DecimalFormat("0.0").format((double) (testStu - tendentiousnessNum) / sumStu * 100) + "%";

        /*评分一致性*/
        /*有嫌疑人数*/
        long consistencyNum = allStuPsychology.stream().filter(x -> compareForConsistency(x.getStr("consistency"))).count();
        /*有嫌疑学生id*/
        List<String> consistencyList = allStuPsychology.stream().filter(x -> compareForConsistency(x.getStr("consistency")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*可靠程度*/
        String consistencyPercent = new DecimalFormat("0.0").format((double) (testStu - consistencyNum) / testStu * 100) + "%";

        /*评分稳定性*/
        /*有嫌疑人数*/
        long stabilityNum = allStuPsychology.stream().filter(x -> compareForStability(x.getStr("stability"))).count();
        /*有嫌疑学生id*/
        List<String> stabilityList = allStuPsychology.stream().filter(x -> compareForStability(x.getStr("stability")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*可靠程度*/
        String stabilityPercent = new DecimalFormat("0.0").format((double)(testStu - stabilityNum) / testStu * 100) + "%";

        /**指数*/
        /*同伴关系*/
        /*高分人数*/
        long companionHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("companionT"))).count();
        /*中等分数人数*/
        long companionMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("companionT"))).count();
        /*低分人数*/
        long companionLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("companionT"))).count();
        /*高分学生id*/
        List<String> companionHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("companionT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> companionLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("companionT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /*亲子关系*/
        /*高分人数*/
        long parenthoodHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("parenthoodT"))).count();
        /*中等分数人数*/
        long parenthoodMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("parenthoodT"))).count();
        /*低分人数*/
        long parenthoodLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("parenthoodT"))).count();
        /*高分学生id*/
        List<String> parenthoodHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("parenthoodT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> parenthoodLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("parenthoodT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /*师生关系*/
        /*高分人数*/
        long tsrelationHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("tsRelationT"))).count();
        /*中等分数人数*/
        long tsrelationMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("tsRelationT"))).count();
        /*低分人数*/
        long tsrelationLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("tsRelationT"))).count();
        /*高分学生id*/
        List<String> tsrelationHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("tsRelationT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> tsrelationLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("tsRelationT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /*宜人性指数*/
        /*高分人数*/
        long agreeablenessHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("agreeablenessT"))).count();
        /*中等分数人数*/
        long agreeablenessMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("agreeablenessT"))).count();
        /*低分人数*/
        long agreeablenessLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("agreeablenessT"))).count();
        /*高分学生id*/
        List<String> agreeablenessHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("agreeablenessT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> agreeablenessLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("agreeablenessT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /*心理弹性*/
        /*高分人数*/
        long resilienceHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("resilienceT"))).count();
        /*中等分数人数*/
        long resilienceMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("resilienceT"))).count();
        /*低分人数*/
        long resilienceLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("resilienceT"))).count();
        /*高分学生id*/
        List<String> resilienceHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("resilienceT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> resilienceLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("resilienceT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /*自尊指数*/
        /*高分人数*/
        long selfesteemHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("selfEsteemT"))).count();
        /*中等分数人数*/
        long selfesteemMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("selfEsteemT"))).count();
        /*低分人数*/
        long selfesteemLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("selfEsteemT"))).count();
        /*高分学生id*/
        List<String> selfesteemHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("selfEsteemT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> selfesteemLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("selfEsteemT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /*自控*/
        /*高分人数*/
        long selfcontrollHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("SelfControllT"))).count();
        /*中等分数人数*/
        long selfcontrollMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("SelfControllT"))).count();
        /*低分人数*/
        long selfcontrollLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("SelfControllT"))).count();
        /*高分学生id*/
        List<String> selfcontrollHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("SelfControllT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> selfcontrollLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("SelfControllT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /*社会主动性*/
        /*高分人数*/
        long socialactiveHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("SocialActiveT"))).count();
        /*中等分数人数*/
        long socialactiveMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("SocialActiveT"))).count();
        /*低分人数*/
        long socialactiveLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("SocialActiveT"))).count();
        /*高分学生id*/
        List<String> socialactiveHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("SocialActiveT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> socialactiveLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("SocialActiveT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /*学业责任心*/
        /*高分人数*/
        long learningresponsibilityHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("learningResponsibilityT"))).count();
        /*中等分数人数*/
        long learningresponsibilityMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("learningResponsibilityT"))).count();
        /*低分人数*/
        long learningresponsibilityLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("learningResponsibilityT"))).count();
        /*高分学生id*/
        List<String> learningresponsibilityHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("learningResponsibilityT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> learningresponsibilityLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("learningResponsibilityT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /*持续的兴趣*/
        /*高分人数*/
        long continueinterestHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("continueTnterestT"))).count();
        /*中等分数人数*/
        long continueinterestMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("continueTnterestT"))).count();
        /*低分人数*/
        long continueinterestLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("continueTnterestT"))).count();
        /*高分学生id*/
        List<String> continueinterestHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("continueTnterestT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> continueinterestLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("continueTnterestT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /*持续努力*/
        /*高分人数*/
        long continueeffortHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("continueEffortT"))).count();
        /*中等分数人数*/
        long continueeffortMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("continueEffortT"))).count();
        /*低分人数*/
        long continueeffortLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("continueEffortT"))).count();
        /*高分学生id*/
        List<String> continueeffortHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("continueEffortT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> continueeffortLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("continueEffortT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /*情景选择*/
        /*高分人数*/
        long situationselectHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("situationSelT"))).count();
        /*中等分数人数*/
        long situationselectMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("situationSelT"))).count();
        /*低分人数*/
        long situationselectLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("situationSelT"))).count();
        /*高分学生id*/
        List<String> situationselectHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("situationSelT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> situationselectLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("situationSelT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /*情景修正*/
        /*高分人数*/
        long situationcorrectHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("situationAdpT"))).count();
        /*中等分数人数*/
        long situationcorrectMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("situationAdpT"))).count();
        /*低分人数*/
        long situationcorrectLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("situationAdpT"))).count();
        /*高分学生id*/
        List<String> situationcorrectHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("situationAdpT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> situationcorrectLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("situationAdpT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /*注意分配*/
        /*高分人数*/
        long attentiondistributionHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("distriOfAttenT"))).count();
        /*中等分数人数*/
        long attentiondistributionMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("distriOfAttenT"))).count();
        /*低分人数*/
        long attentiondistributionLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("distriOfAttenT"))).count();
        /*高分学生id*/
        List<String> attentiondistributionHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("distriOfAttenT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> attentiondistributionLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("distriOfAttenT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /*认知改变*/
        /*高分人数*/
        long cognitivechangeHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("cognitiveChangeT"))).count();
        /*中等分数人数*/
        long cognitivechangeMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("cognitiveChangeT"))).count();
        /*低分人数*/
        long cognitivechangeLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("cognitiveChangeT"))).count();
        /*高分学生id*/
        List<String> cognitivechangeHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("cognitiveChangeT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> cognitivechangeLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("cognitiveChangeT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        /*反应调节*/
        /*高分人数*/
        long responsemodHighNum = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("responseModT"))).count();
        /*中等分数人数*/
        long responsemodMiddleNum = allStuPsychology.stream().filter(x -> compareForMiddleScore(x.getStr("responseModT"))).count();
        /*低分人数*/
        long responsemodLowNum = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("responseModT"))).count();
        /*高分学生id*/
        List<String> responsemodHighList = allStuPsychology.stream().filter(x -> compareForHighScore(x.getStr("responseModT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());
        /*低分学生id*/
        List<String> responsemodLowList = allStuPsychology.stream().filter(x -> compareForLowScore(x.getStr("responseModT")))
                .map(x -> x.getStr("stuId")).collect(Collectors.toList());

        Record result = new Record();
        result.set("school_id", schoolId)
                .set("grade_id", gradeId)
                .set("class_id", classId)
                .set("test_stu_num", testStu)
                .set("test_stu_percent", testStu + ":" + sumStu )
                .set("stu_sex_percent", numSexMan + ":" + numSexWoman)
                .set("stu_birthday_rang", allStuBirthdayRange.get(0).getStr("min").substring(0,4) + "-" + allStuBirthdayRange.get(0).getStr("max").substring(0,4))
                .set("test_date", testTime)

                .set("accuracy_percent", accuracyPercent)
                .set("accuracy_num", accuracyNum)
                .set("accuracy_list", accuracyList.toString())
                .set("tendentiousness_percent", tendentiousnessPercent)
                .set("tendentiousness_num", tendentiousnessNum)
                .set("tendentiousness_list", tendentiousnessList.toString())
                .set("consistency_percent", consistencyPercent)
                .set("consistency_num", consistencyNum)
                .set("consistency_list", consistencyList.toString())
                .set("stability_percent", stabilityPercent)
                .set("stability_num", stabilityNum)
                .set("stability_list", stabilityList.toString())

                .set("companion_high_num", companionHighNum)
                .set("companion_middle_num", companionMiddleNum)
                .set("companion_low_num", companionLowNum)
                .set("companion_high_list", companionHighList.toString())
                .set("companion_low_list", companionLowList.toString())

                .set("parenthood_high_num", parenthoodHighNum)
                .set("parenthood_middle_num", parenthoodMiddleNum)
                .set("parenthood_low_num", parenthoodLowNum)
                .set("parenthood_high_list", parenthoodHighList.toString())
                .set("parenthood_low_list", parenthoodLowList.toString())

                .set("tsrelation_high_num", tsrelationHighNum)
                .set("tsrelation_middle_num", tsrelationMiddleNum)
                .set("tsrelation_low_num", tsrelationLowNum)
                .set("tsrelation_high_list",tsrelationHighList.toString() )
                .set("tsrelation_low_list", tsrelationLowList.toString())

                .set("agreeableness_high_num", agreeablenessHighNum)
                .set("agreeableness_middle_num", agreeablenessMiddleNum)
                .set("agreeableness_low_num", agreeablenessLowNum)
                .set("agreeableness_high_list", agreeablenessHighList.toString())
                .set("agreeableness_low_list", agreeablenessLowList.toString())

                .set("resilience_high_num", resilienceHighNum)
                .set("resilience_middle_num", resilienceMiddleNum)
                .set("resilience_low_num", resilienceLowNum)
                .set("resilience_high_list", resilienceHighList.toString())
                .set("resilience_low_list", resilienceLowList.toString())

                .set("selfesteem_high_num", selfesteemHighNum)
                .set("selfesteem_middle_num", selfesteemMiddleNum)
                .set("selfesteem_low_num", selfesteemLowNum)
                .set("selfesteem_high_list", selfesteemHighList.toString())
                .set("selfesteem_low_list", selfesteemLowList.toString())

                .set("selfcontroll_high_num", selfcontrollHighNum)
                .set("selfcontroll_middle_num", selfcontrollMiddleNum)
                .set("selfcontroll_low_num", selfcontrollLowNum)
                .set("selfcontroll_high_list", selfcontrollHighList.toString())
                .set("selfcontroll_low_list", selfcontrollLowList.toString())

                .set("socialactive_high_num", socialactiveHighNum)
                .set("socialactive_middle_num", socialactiveMiddleNum)
                .set("socialactive_low_num", socialactiveLowNum)
                .set("socialactive_high_list", socialactiveHighList.toString())
                .set("socialactive_low_list", socialactiveLowList.toString())

                .set("learningresponsibility_high_num", learningresponsibilityHighNum)
                .set("learningresponsibility_middle_num", learningresponsibilityMiddleNum)
                .set("learningresponsibility_low_num", learningresponsibilityLowNum)
                .set("learningresponsibility_high_list", learningresponsibilityHighList.toString())
                .set("learningresponsibility_low_list", learningresponsibilityLowList.toString())

                .set("continueinterest_high_num", continueinterestHighNum)
                .set("continueinterest_middle_num", continueinterestMiddleNum)
                .set("continueinterest_low_num", continueinterestLowNum)
                .set("continueinterest_high_list", continueinterestHighList.toString())
                .set("continueinterest_low_list", continueinterestLowList.toString())

                .set("continueeffort_high_num", continueeffortHighNum)
                .set("continueeffort_middle_num", continueeffortMiddleNum)
                .set("continueeffort_low_num", continueeffortLowNum)
                .set("continueeffort_high_list", continueeffortHighList.toString())
                .set("continueeffort_low_list", continueeffortLowList.toString())

                .set("situationcorrect_high_num", situationcorrectHighNum)
                .set("situationcorrect_middle_num", situationcorrectMiddleNum)
                .set("situationcorrect_low_num", situationcorrectLowNum)
                .set("situationcorrect_high_list", situationcorrectHighList.toString())
                .set("situationcorrect_low_list", situationcorrectLowList.toString())

                .set("attentiondistribution_high_num", attentiondistributionHighNum)
                .set("attentiondistribution_middle_num", attentiondistributionMiddleNum)
                .set("attentiondistribution_low_num", attentiondistributionLowNum)
                .set("attentiondistribution_high_list", attentiondistributionHighList.toString())
                .set("attentiondistribution_low_list", attentiondistributionLowList.toString())

                .set("cognitivechange_high_num", cognitivechangeHighNum)
                .set("cognitivechange_middle_num", cognitivechangeMiddleNum)
                .set("cognitivechange_low_num", cognitivechangeLowNum)
                .set("cognitivechange_high_list", cognitivechangeHighList.toString())
                .set("cognitivechange_low_list", cognitivechangeLowList.toString())

                .set("responsemod_high_num", responsemodHighNum)
                .set("responsemod_middle_num", responsemodMiddleNum)
                .set("responsemod_low_num", responsemodLowNum)
                .set("responsemod_high_list", responsemodHighList.toString())
                .set("responsemod_low_list", responsemodLowList.toString());
        dao.saveClassPsychology(result);

    }

    /**
     * 评分准确性分数在 (0, 30)中
     * @param score
     * @return
     */
    private boolean compareForAccuracy(String score){
        return Double.parseDouble(score) < 30.00;
    }

    /**
     * 评分倾向性分数在(0, 70)
     * @param score
     * @return
     */
    private boolean compareForTendentiousness(String score){
        return Double.parseDouble(score) < 70;
    }

    /**
     * 评分一致性分数在(0, 30)
     * @param score
     * @return
     */
    private boolean compareForConsistency(String score){
        return Double.parseDouble(score) < 30.00;
    }

    /**
     * 评分稳定性分数在(0, 70)
     * @param score
     * @return
     */
    private boolean compareForStability(String score){
        return Double.parseDouble(score) < 70.00;
    }

    /**
     * 指数在[60, ∞)
     * @param score
     * @return
     */
    private boolean compareForHighScore(String score){
        return Double.parseDouble(score) >= 60.00;
    }

    /**
     * 指数分数在[40, 60)
     * @param score
     * @return
     */
    private boolean compareForMiddleScore(String score){
        return Double.parseDouble(score) < 60.00 && Double.parseDouble(score) >= 40.00;
    }

    /**
     * 指数分数在(∞，40)
     * @param score
     * @return
     */
    private boolean compareForLowScore(String score){
        return Double.parseDouble(score) < 40.00;
    }

}
