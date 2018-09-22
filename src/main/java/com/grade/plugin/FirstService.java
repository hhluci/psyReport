package com.grade.plugin;

import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description:
 * @Date: Created in 9:00 2018/9/20
 */
public class FirstService {

    private Dao dao = new Dao("first");
    private Subdimension subdimension = new Subdimension();

    public void firstPart(String schoolId, String gradeId, String classId){
        /*获取该班级下所有学生id*/
        List<Record> stuId = dao.find(Constant.findStuId(schoolId, gradeId, classId));
        /*中间结果集*/
        Map<String, Record> temp = new HashMap<>();
        /*最终结果*/
        Record score ;
        for (Record id : stuId){
            Record result = new Record();
            List<Record> allList = dao.find(Constant.findFirstAll(schoolId, gradeId, classId, id.getStr("stuId")));
            List<Record> testTime = dao.find(Constant.findTestTime(schoolId, gradeId, classId, id.getStr("stuId")));

            result.set("schoolId", schoolId)
                    .set("gradeId", gradeId)
                    .set("classId", classId)
                    .set("stuId", id.getStr("stuId"))
                    //问题行为交卷时间
                    .set("testDate", testTime.get(1).getStr("submitTime"))
                    //作答时间
                    .set("elapsedTime", this.getTestTime(testTime.get(0).getStr("startTime"), testTime.get(testTime.size()-1).getStr("submitTime")))
                    //作答缺失
                    .set("missRate", "0%")
                    //效度分数
                    .set("idxL", new DecimalFormat("0.00").format(subdimension.getL(this.getScoreList(allList, "30101"))))
                    .set("idxF", new DecimalFormat("0.00").format(subdimension.getF(this.getScoreList(allList, "30103"))))
                    .set("idxC", new DecimalFormat("0.00").format(subdimension.getC(this.getScoreList(allList, "30102"), this.getCScoreList(allList))))
                    //情绪子维度分数
                    .set("anxiety", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10101")))))
                    .set("depression", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10102")))))
                    .set("uncontroll", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10104")))))
                    .set("fail", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10105")))))
                    .set("somatization", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10103")))))
                    .set("oddBehav", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10106")))))
                    /*情绪维度*/
                    .set("emotion", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.getAverage(this.getListToArray(allList, "10100")))))
                    //人际问题子维度
                    .set("socialPress", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10201")))))
                    .set("degenerate", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10202")))))
                    .set("bully", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10203")))))
                    /*人际问题维度*/
                    .set("Interpersonal", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.getAverage(this.getListToArray(allList, "10200")))))
                    //注意力问题子维度
                    .set("inattention", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10301")))))
                    .set("mania", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10302")))))
                    /*注意力维度*/
                    .set("attention", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.getAverage(this.getListToArray(allList, "10300")))))
                    //成瘾问题子维度
                    .set("netAddiction", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10401")))))
                    .set("phoneAddiction", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10402")))))
                    /*成瘾问题维度*/
                    .set("addiction", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.getAverage(this.getListToArray(allList, "10400")))))
                    //厌学状况子维度
                    .set("hateSchool", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10501")))))
                    .set("hateTeacher", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10502")))))
                    .set("hateStudy", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10503")))))
                    .set("anxExam", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.primitiveScore(this.getScoreList(allList, "10504")))))
                    /*厌学问题维度*/
                    .set("Weariness", Double.parseDouble(new DecimalFormat("0.00").format(subdimension.getAverage(this.getListToArray(allList, "10500")))))
                    /*自伤指数*/
                    .set("autolesionIdx", (Double.parseDouble(result.getStr("anxiety")) + Double.parseDouble(result.getStr("depression")) + Double.parseDouble(result.getStr("somatization"))) / 3)
                    /*无助感指数*/
                    .set("HelplessnessIdx", (Double.parseDouble(result.getStr("uncontroll")) + Double.parseDouble(result.getStr("fail"))) / 2)
                    /*人际障碍指数*/
                    .set("interpersonalIdx", (Double.parseDouble(result.getStr("socialPress")) + Double.parseDouble(result.getStr("degenerate"))) / 2)
                    /*成瘾指数*/
                    .set("addictionIdx", (Double.parseDouble(result.getStr("netAddiction")) + Double.parseDouble(result.getStr("phoneAddiction"))) / 2)
                    /*受欺负指数*/
                    .set("bullyIdx", Double.parseDouble(result.getStr("bully")))
                    /*行为指数*/
                    .set("behavIdx", Double.parseDouble(result.getStr("oddBehav")))
                    /*狂躁指数*/
                    .set("maniaIdx", Double.parseDouble(result.getStr("mania")))
                    /*身体不适指数*/
                    .set("poorHealthIdx", Double.parseDouble(result.getStr("somatization")))
                    /*厌学指数*/
                    .set("WearinessIdx", (Double.parseDouble(result.getStr("hateSchool")) + Double.parseDouble(result.getStr("hateStudy"))) / 2)
                    /*分心指数*/
                    .set("DistractionIdx", Double.parseDouble(result.getStr("inattention")))
                    /*考试焦虑指数*/
                    .set("anxExamIdx", Double.parseDouble(result.getStr("anxExam")))
                    /*师生冲突指数*/
                    .set("conflictIdx", Double.parseDouble(result.getStr("hateTeacher")));
                    temp.put(id.getStr("stuId"), result);
        }
        //System.out.println(temp);
        /*情绪问题维度*/
        double ave_emo = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("emotion")));
        double s_emo = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("emotion")).collect(Collectors.toList()).stream().toArray(Double[]::new)));
        /*人际问题维度*/
        double ave_Inter = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("Interpersonal")));
        double s_Inter = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("Interpersonal")).collect(Collectors.toList()).stream().toArray(Double[]::new)));
        /*注意力问题维度*/
        double ave_atten = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("attention")));
        double s_atten = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("attention")).collect(Collectors.toList()).stream().toArray(Double[]::new)));
        /*成瘾问题维度*/
        double ave_addict = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("addiction")));
        double s_addict = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("addiction")).collect(Collectors.toList()).stream().toArray(Double[]::new)));
        /*厌学状况维度*/
        double ave_Wear = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("Weariness")));
        double s_Wear = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("Weariness")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        /*子维度*/
        double ave_anxiety = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("anxiety")));
        double s_anxiety = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("anxiety")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_depression = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("depression")));
        double s_depression =subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("depression")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_uncontroll = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("uncontroll")));
        double s_uncontroll =subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("uncontroll")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_fail = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("fail")));
        double s_fail = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("fail")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_somatization = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("somatization")));
        double s_somatization = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("somatization")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_oddBehav = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("oddBehav")));
        double s_oddBehav = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("oddBehav")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_socialPress = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("socialPress")));
        double s_socialPress = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("socialPress")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_degenerate = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("degenerate")));
        double s_degenerate = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("degenerate")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_bully = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("bully")));
        double s_bully = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("bully")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_inattention =temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("inattention")));
        double s_inattention = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("inattention")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_mania = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("mania")));
        double s_mania = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("mania")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_netAddiction = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("netAddiction")));
        double s_netAddiction = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("netAddiction")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_phoneAddiction = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("phoneAddiction")));
        double s_phoneAddiction = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("phoneAddiction")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_hateSchool = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("hateSchool")));
        double s_hateSchool = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("hateSchool")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_hateTeacher = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("hateTeacher")));
        double s_hateTeacher = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("hateTeacher")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_hateStudy = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("hateStudy")));
        double s_hateStudy = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("hateStudy")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_anxExam = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("anxExam")));
        double s_anxExam = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("anxExam")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        /*指数*/
        double ave_autolesionIdx = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("autolesionIdx")));
        double s_autolesionIdx = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("autolesionIdx")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_HelplessnessIdx = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("HelplessnessIdx")));
        double s_HelplessnessIdx = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("HelplessnessIdx")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_interpersonalIdx = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("interpersonalIdx")));
        double s_interpersonalIdx = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("interpersonalIdx")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_bullyIdx = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("bullyIdx")));
        double s_bullyIdx = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("bullyIdx")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_addictionIdx = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("addictionIdx")));
        double s_addictionIdx = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("addictionIdx")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_behavIdx = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("behavIdx")));
        double s_behavIdx = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("behavIdx")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_maniaIdx = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("maniaIdx")));
        double s_maniaIdx = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("maniaIdx")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_poorHealthIdx = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("poorHealthIdx")));
        double s_poorHealthIdx = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("poorHealthIdx")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_WearinessIdx = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("WearinessIdx")));
        double s_WearinessIdx = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("WearinessIdx")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_DistractionIdx = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("DistractionIdx")));
        double s_DistractionIdx = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("DistractionIdx")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_anxExamIdx = temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("anxExamIdx")));
        double s_anxExamIdx = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("anxExamIdx")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_conflictIdx =temp.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("conflictIdx")));
        double s_conflictIdx =subdimension.getStandardDeviation(ArrayUtils.toPrimitive(temp.values().stream().map(x -> x.getDouble("conflictIdx")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        for (String key : temp.keySet()){
            score = new Record()
                    .set("schoolId", schoolId)
                    .set("gradeId", gradeId)
                    .set("classId", classId)
                    .set("stuId", key)
                    .set("testDate", temp.get(key).getStr("testDate"))
                    .set("elapsedTime", temp.get(key).getStr("elapsedTime"))
                    .set("missRate", temp.get(key).getStr("missRate"))
                    .set("idxL", temp.get(key).getStr("idxL"))
                    .set("idxF", temp.get(key).getStr("idxF"))
                    .set("idxC", temp.get(key).getStr("idxC"))
                    /*维度*/
                    .set("emotion", s_emo != 0 ? new DecimalFormat("0.00").format((temp.get(key).getDouble("emotion") - ave_emo) / s_emo * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("Interpersonal", s_Inter != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("Interpersonal") - ave_Inter) / s_Inter * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("attention", s_atten != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("attention") - ave_atten) / s_atten * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("addiction", s_addict != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("addiction") - ave_addict) / s_addict * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("Weariness", s_Wear != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("Weariness") - ave_Wear) / s_Wear * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    /*子维度*/
                    .set("anxiety", s_anxiety != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("anxiety") - ave_anxiety) / s_anxiety * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("depression", s_depression != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("depression") - ave_depression) / s_depression * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("uncontroll", s_uncontroll != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("uncontroll") - ave_uncontroll) / s_uncontroll * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("fail", s_fail != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("fail") - ave_fail) / s_fail * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("somatization", s_somatization != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("somatization") - ave_somatization) / s_somatization * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("oddBehav", s_oddBehav != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("oddBehav") - ave_oddBehav) / s_oddBehav * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("socialPress", s_socialPress != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("socialPress") - ave_socialPress) / s_socialPress * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("degenerate", s_degenerate != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("degenerate") - ave_degenerate) / s_degenerate * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("bully", s_bully != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("bully") - ave_bully) / s_bully * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("inattention", s_inattention != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("inattention") - ave_inattention) / s_inattention * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("mania", s_mania != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("mania") - ave_mania) / s_mania * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("netAddiction", s_netAddiction != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("netAddiction") - ave_netAddiction) / s_netAddiction * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("phoneAddiction", s_phoneAddiction != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("phoneAddiction") - ave_phoneAddiction) / s_phoneAddiction * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("hateSchool", s_hateSchool != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("hateSchool") - ave_hateSchool) / s_hateSchool * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("hateTeacher", s_hateTeacher != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("hateTeacher") - ave_hateTeacher) / s_hateTeacher * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("hateStudy", s_hateStudy != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("hateStudy") - ave_hateStudy) / s_hateStudy * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("anxExam", s_anxExam != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("anxExam") - ave_anxExam) / s_anxExam * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    /*指数*/
                    .set("autolesionIdx", s_autolesionIdx != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("autolesionIdx") - ave_autolesionIdx) / s_autolesionIdx * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("HelplessnessIdx", s_HelplessnessIdx != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("HelplessnessIdx") - ave_HelplessnessIdx) / s_HelplessnessIdx * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("interpersonalIdx", s_interpersonalIdx != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("interpersonalIdx") - ave_interpersonalIdx) / s_interpersonalIdx * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("addictionIdx", s_addictionIdx != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("addictionIdx") - ave_addictionIdx) / s_addictionIdx * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("bullyIdx", s_bullyIdx != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("bullyIdx") - ave_bullyIdx) / s_bullyIdx * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("behavIdx", s_behavIdx != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("behavIdx") - ave_behavIdx) / s_behavIdx * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("maniaIdx", s_maniaIdx != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("maniaIdx") - ave_maniaIdx) / s_maniaIdx * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("poorHealthIdx", s_poorHealthIdx != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("poorHealthIdx") - ave_poorHealthIdx) / s_poorHealthIdx * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("WearinessIdx", s_WearinessIdx != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("WearinessIdx") - ave_WearinessIdx) / s_WearinessIdx * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("DistractionIdx", s_DistractionIdx != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("DistractionIdx") - ave_DistractionIdx) / s_DistractionIdx * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("anxExamIdx", s_anxExamIdx != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("anxExamIdx") - ave_anxExamIdx) / s_anxExamIdx * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION)
                    .set("conflictIdx", s_conflictIdx != 0 ?new DecimalFormat("0.00").format((temp.get(key).getDouble("conflictIdx") - ave_conflictIdx) / s_conflictIdx * 10 + 50) : Constant.INVALID_STANDARD_DEVIATION);
            dao.save(score);
            //System.out.println(score);
        }

    }


    /**
     * 获取第一部分中某子维度集合
     * @param findAll 第一部分中第一小部分所有成绩
     * @param scaleTypeCode  third_scale_type_code子维度编号
     * @return
     */
    private List<Record> getScoreList(List<Record> findAll, String scaleTypeCode){
        return findAll.stream()
                .filter(x -> scaleTypeCode.equals(x.getStr("third_scale_type_code")))
                .collect(Collectors.toList());
    }

    /**
     * 获取重复题中第一次出现的列表
     * @param findAll
     * @return
     */
    private List<Record> getCScoreList(List<Record> findAll){
        return findAll.stream()
                .filter(x -> x.getStr("question_code").contains("-1"))
                .filter(x -> x.getInt("part") == 1)
                .filter(x -> x.getInt("part") == 2)
                .collect(Collectors.toList());
    }

    /**
     * 将list转为double数组
     * @return
     */
    private double[] getListToArray(List<Record> findAll, String scaleTypeCode){
        Integer[] result = findAll.stream()
                .filter(x -> scaleTypeCode.equals(x.getStr("second_scale_type_code")))
                .map(x -> x.get("queAns"))
                .collect(Collectors.toList())
                .stream()
                .toArray(Integer[]::new);
        double[] array = new double[result.length];
        for (int i = 0; i < result.length; i++){
            array[i] = result[i];
        }
        return array;
    }

    /**
     * 计算答题时间
     * @param startTime
     * @param endTime
     * @return
     */
    private String getTestTime(String startTime, String endTime){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
        DateTime start = dateTimeFormatter.parseDateTime(startTime);
        DateTime end = dateTimeFormatter.parseDateTime(endTime);
        return String.valueOf(Minutes.minutesBetween(start, end).getMinutes());
    }

}
