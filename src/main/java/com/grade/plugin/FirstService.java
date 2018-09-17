package com.grade.plugin;

import com.jfinal.plugin.activerecord.Record;
import com.sun.deploy.util.ArrayUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description: 成绩综合
 * @Date: Created in 18:41 2018/9/13
 */
public class FirstService {

    Dao dao = new Dao();
    final Subdimension subdimension = new Subdimension();

    /**
     * 计算维度分数
     * @param schoolId
     * @param gradeId
     * @param classId
     * @return
     */
    public void firstPart(String schoolId, String gradeId, String classId){
        List<Record> stuIdList = dao.find(SQL.findStuId(schoolId, gradeId, classId));
        System.out.println(stuIdList);
        for (Record record : stuIdList){
            //System.out.println(dao.find(SQL.findGroupbyPart(schoolId, gradeId, classId, record.getStr("stuId"))));
            if (this.save(dao.find(SQL.findFirstAll(schoolId, gradeId, classId, record.getStr("stuId"))),
                    dao.find(SQL.findGroupbyPart(schoolId, gradeId, classId, record.getStr("stuId"))),
                    dao.find(SQL.findAll(schoolId, gradeId, classId, record.getStr("stuId"))))){
                //this.secondPart(schoolId, gradeId, classId);
            }
        }


    }

    public boolean save(List<Record> allList, List<Record> findGroupByPart, List<Record> findAllScore){
        /*添加学生唯一标识信息*/
        Record recordFirstPart = new Record();
        /* --------------------------------- 添加写入数据库信息 start --------------------------------- */
        recordFirstPart.set("schoolId", findGroupByPart.get(0).getStr("schoolId"))
                .set("gradeId", findGroupByPart.get(0).getStr("gradeId"))
                .set("classId", findGroupByPart.get(0).getStr("classId"))
                .set("stuId", findGroupByPart.get(0).getStr("stuId"))
                //问题行为的交卷时间
                .set("testDate", this.getTestTime(findGroupByPart.get(0).getStr("startTime"), findGroupByPart.get(1).getStr("submitTime")))
                //作答时间
                .set("elapsedTime", "0")
                //作答缺失
                .set("missRate", "0%")
                //效度分数
                .set("idxL", String.valueOf(subdimension.getL(this.getScoreList(findAllScore, "30101"))))
                .set("idxF", String.valueOf(subdimension.getF(this.getScoreList(findAllScore, "30103"))))
                .set("idxC", String.valueOf(subdimension.getC(this.getScoreList(findAllScore, "30102"), this.getCScoreList(findAllScore))))
                //情绪子维度分数
                .set("anxiety", String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10101"))))
                .set("depression", String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10102"))))
                .set("uncontroll", String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10104"))))
                .set("fail", String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10105"))))
                .set("somatization", String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10103"))))
                .set("oddBehav", String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10106"))))
                /*情绪维度*/
                .set("emotion",String.valueOf(subdimension.getAverage(this.getListToArray(allList, "10100"))))
                //人际问题子维度
                .set("socialPress",String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10201"))))
                .set("degenerate",String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10202"))))
                .set("bully",String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10203"))))
                /*人际问题维度*/
                .set("Interpersonal",String.valueOf(subdimension.getAverage(this.getListToArray(allList, "10200"))))
                //注意力问题子维度
                .set("inattention",String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10301"))))
                .set("mania",String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10302"))))
                /*注意力维度*/
                .set("attention",String.valueOf(subdimension.getAverage(this.getListToArray(allList, "10300"))))
                //成瘾问题子维度
                .set("netAddiction",String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10401"))))
                .set("phoneAddiction",String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10402"))))
                /*成瘾问题维度*/
                .set("addiction",String.valueOf(subdimension.getAverage(this.getListToArray(allList, "10400"))))
                //厌学状况子维度
                .set("hateSchool",String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10501"))))
                .set("hateTeacher",String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10502"))))
                .set("hateStudy",String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10503"))))
                .set("anxExam",String.valueOf(subdimension.primitiveScore(this.getScoreList(allList, "10504"))))
                /*厌学问题维度*/
                .set("Weariness",String.valueOf(subdimension.getAverage(this.getListToArray(allList, "10500"))));

        /* --------------------------------- 添加写入数据库信息 end --------------------------------- */
        return dao.save(recordFirstPart);

    }

    public void secondPart(String schoolId, String gradeId, String classId){
        /**第一部分报表中 某个班的所有成绩*/
        List<Record> listAll = dao.find(SQL.findGenerate(schoolId, gradeId, classId));
        /**通过学号分组 获取id值*/
        List<Record> listId = dao.find(SQL.findId(schoolId, gradeId, classId));

        Record updateRecord = new Record();

        double[] anxiety = subdimension.getT(this.getListToDouble(listAll, "anxiety"));
        double[] depression = subdimension.getT(this.getListToDouble(listAll, "depression"));
        double[] uncontroll = subdimension.getT(this.getListToDouble(listAll, "uncontroll"));
        double[] fail = subdimension.getT(this.getListToDouble(listAll, "fail"));
        double[] somatization = subdimension.getT(this.getListToDouble(listAll, "somatization"));
        double[] oddBehav = subdimension.getT(this.getListToDouble(listAll, "oddBehav"));
        double[] socialPress = subdimension.getT(this.getListToDouble(listAll, "socialPress"));
        double[] degenerate = subdimension.getT(this.getListToDouble(listAll, "degenerate"));
        double[] bully = subdimension.getT(this.getListToDouble(listAll, "bully"));
        double[] inattention = subdimension.getT(this.getListToDouble(listAll, "inattention"));
        double[] mania = subdimension.getT(this.getListToDouble(listAll, "mania"));
        double[] netAddiction = subdimension.getT(this.getListToDouble(listAll, "netAddiction"));
        double[] phoneAddiction = subdimension.getT(this.getListToDouble(listAll, "phoneAddiction"));
        double[] hateSchool = subdimension.getT(this.getListToDouble(listAll, "hateSchool"));
        double[] hateTeacher = subdimension.getT(this.getListToDouble(listAll, "hateTeacher"));
        double[] hateStudy = subdimension.getT(this.getListToDouble(listAll, "hateStudy"));
        double[] anxExam = subdimension.getT(this.getListToDouble(listAll, "anxExam"));
        double[] emotion = this.getEmotionT(anxiety, depression, uncontroll, fail, somatization, oddBehav);
        double[] Interpersonal = this.getInterpersonalT(socialPress, degenerate, bully);
        double[] attention = this.getAttentionT(inattention, mania);
        double[] addiction = this.getAddictionT(netAddiction, phoneAddiction);
        double[] Weariness = this.getWearinessT(hateSchool, hateTeacher, hateStudy, anxExam);
        double[] autolesionIdx = subdimension.getT(this.getautolesionIdx(anxiety, depression, somatization));
        double[] HelplessnessIdx = subdimension.getT(this.getHelplessnessIdx(uncontroll, fail));
        double[] interpersonalIdx = subdimension.getT(this.getinterpersonalIdx(socialPress, degenerate));
        double[] addictionIdx = subdimension.getT(this.getaddictionIdx(netAddiction, phoneAddiction));
        double[] bullyIdx = subdimension.getT(this.getbullyIdx(bully));
        double[] behavIdx = subdimension.getT(this.getbullyIdx(bully));
        double[] maniaIdx = subdimension.getT(this.getmaniaIdx(mania));
        double[] poorHealthIdx = subdimension.getT(this.getpoorHealthIdx(somatization));
        double[] WearinessIdx = subdimension.getT(this.getWearinessIdx(hateSchool, hateStudy));
        double[] DistractionIdx = subdimension.getT(this.getDistractionIdx(inattention));
        double[] anxExamIdx = subdimension.getT(this.getDistractionIdx(inattention));
        double[] conflictIdx = subdimension.getT(this.getconflictIdx(hateTeacher));
        for (int i = 0; i < listId.size(); i++){
            updateRecord.set("id", listId.get(i).getInt("id"))
                    .set("anxiety", anxiety[i])
                    .set("depression", depression[i])
                    .set("uncontroll", uncontroll[i])
                    .set("fail", fail[i])
                    .set("somatization", somatization[i])
                    .set("oddBehav", oddBehav[i])
                    .set("socialPress", socialPress[i])
                    .set("degenerate", degenerate[i])
                    .set("bully", bully[i])
                    .set("inattention", inattention[i])
                    .set("mania", mania[i])
                    .set("netAddiction", netAddiction[i])
                    .set("phoneAddiction", phoneAddiction[i])
                    .set("hateSchool", hateSchool[i])
                    .set("hateTeacher", hateTeacher[i])
                    .set("hateStudy", hateStudy[i])
                    .set("anxExam", anxExam[i])
                    .set("emotion", emotion[i])
                    .set("Interpersonal", Interpersonal[i])
                    .set("attention", attention[i])
                    .set("addiction", addiction[i])
                    .set("Weariness", Weariness[i])
                    .set("autolesionIdx", autolesionIdx[i])
                    .set("HelplessnessIdx", HelplessnessIdx[i])
                    .set("interpersonalIdx", interpersonalIdx[i])
                    .set("addictionIdx", addictionIdx[i])
                    .set("bullyIdx", bullyIdx[i])
                    .set("behavIdx", behavIdx[i])
                    .set("maniaIdx", maniaIdx[i])
                    .set("poorHealthIdx", poorHealthIdx[i])
                    .set("WearinessIdx", WearinessIdx[i])
                    .set("DistractionIdx", DistractionIdx[i])
                    .set("anxExamIdx", anxExamIdx[i])
                    .set("conflictIdx", conflictIdx[i]);
            dao.update("tbproblembehavrep", "id", updateRecord);
        }
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
     * 将list转为double数组
     * @param findAll
     * @param subdimension
     * @return
     */
    private double[] getListToDouble(List<Record> findAll, String subdimension){
        String[] result = findAll.stream()
                .map(x -> x.getStr(subdimension))
                .collect(Collectors.toList())
                .stream()
                .toArray(String[]::new);
        double[] array = new double[result.length];
        for (int i = 0; i < result.length; i++){
            array[i] = Double.parseDouble(result[i]);
        }
        return array;
    }

    /**
     * 情绪维度 T 分数
     * @param anxiety
     * @param depression
     * @param uncontroll
     * @param fail
     * @param somatization
     * @param oddBehav
     * @return
     */
    private double[] getEmotionT(double[] anxiety, double[] depression, double[] uncontroll, double[] fail,
                                 double[] somatization, double[] oddBehav){
        double[] result = new double[anxiety.length];
        for (int i = 0; i < anxiety.length; i++){
            result[i] = (anxiety[i] + depression[i] + uncontroll[i] + fail[i] + somatization[i] + oddBehav[i]) / 6;
        }
        return result;
    }

    /**
     * 人际问题 T分数
     * @param socialPress
     * @param degenerate
     * @param bully
     * @return
     */
    private double[] getInterpersonalT(double[] socialPress, double[] degenerate, double[] bully){
        double[] result = new double[socialPress.length];
        for (int i = 0; i < socialPress.length; i++){
            result[i] = (socialPress[i] + degenerate[i] + bully[i]) / 3;
        }
        return result;
    }

    /**
     * 注意力问题T分数
     * @param inattention
     * @param mania
     * @return
     */
    private double[] getAttentionT(double[] inattention, double[] mania){
        double[] result = new double[inattention.length];
        for (int i = 0; i < inattention.length; i++){
            result[i] = (inattention[i] + mania[i] ) / 2;
        }
        return result;
    }

    /**
     * 成瘾问题 T分数
     * @param netAddiction
     * @param phoneAddiction
     * @return
     */
    private double[] getAddictionT(double[] netAddiction, double[] phoneAddiction){
        double[] result = new double[netAddiction.length];
        for (int i = 0; i < netAddiction.length; i++){
            result[i] = (netAddiction[i] + phoneAddiction[i] ) / 2;
        }
        return result;
    }

    /**
     * 厌学问题T分数
     * @param hateSchool
     * @param hateTeacher
     * @param hateStudy
     * @param anxExam
     * @return
     */
    private double[] getWearinessT(double[] hateSchool, double[] hateTeacher, double[] hateStudy, double[] anxExam){
        double[] result = new double[hateSchool.length];
        for (int i = 0; i < hateSchool.length; i++){
            result[i] = (hateSchool[i] + hateTeacher[i] + hateStudy[i] + anxExam[i]) / 4;
        }
        return result;
    }

    /**
     * 自伤指数原始分
     * @param anxiety
     * @param depression
     * @param somatization
     * @return
     */
    private double[] getautolesionIdx(double[] anxiety, double[] depression, double[] somatization){
        double[] result = new double[anxiety.length];
        for (int i = 1; i < anxiety.length; i ++){
            result[i] = (anxiety[i] + depression[i] + somatization[i]) / 3;
        }
        return result;
    }

    /**
     * 无助感原始分
     * @param uncontroll
     * @param fail
     * @return
     */
    private double[] getHelplessnessIdx(double[] uncontroll, double[] fail){
        double[] result = new double[uncontroll.length];
        for (int i = 1; i < uncontroll.length; i ++){
            result[i] = (uncontroll[i] + fail[i]) / 2;
        }
        return result;
    }

    /**
     * 人际障碍指数
     * @param socialPress
     * @param degenerate
     * @return
     */
    private double[] getinterpersonalIdx(double[] socialPress, double[] degenerate){
        double[] result = new double[socialPress.length];
        for (int i = 1; i < socialPress.length; i ++){
            result[i] = (socialPress[i] + degenerate[i]) / 2;
        }
        return result;
    }

    /**
     * 成瘾指数
     * @param netAddiction
     * @param phoneAddiction
     * @return
     */
    private double[] getaddictionIdx(double[] netAddiction, double[] phoneAddiction){
        double[] result = new double[netAddiction.length];
        for (int i = 1; i < netAddiction.length; i ++){
            result[i] = (netAddiction[i] + phoneAddiction[i]) / 2;
        }
        return result;
    }

    /**
     * 受欺负指数
     * @param bully
     * @return
     */
    private double[] getbullyIdx(double[] bully){
        return bully;
    }

    /**
     * 不寻常行为指数
     * @param oddBehav
     * @return
     */
    private double[] getbehavIdx(double[] oddBehav){
        return oddBehav;
    }

    /**
     * 狂躁指数
     * @param mania
     * @return
     */
    private double[] getmaniaIdx(double[] mania){
        return mania;
    }

    /**
     * 身体不适指数
     * @param somatization
     * @return
     */
    private double[] getpoorHealthIdx(double[] somatization){
        return somatization;
    }

    /**
     * 厌学指数
     * @param hateSchool
     * @param hateStudy
     * @return
     */
    private double[] getWearinessIdx(double[] hateSchool, double[] hateStudy){
        double[] result = new double[hateSchool.length];
        for (int i = 1; i < hateSchool.length; i ++){
            result[i] = (hateSchool[i] + hateStudy[i]) / 2;
        }
        return result;
    }

    /**
     * 分心指数
     * @param inattention
     * @return
     */
    private double[] getDistractionIdx(double[] inattention){
        return inattention;
    }

    /**
     * 考试焦虑指数
     * @param anxExam
     * @return
     */
    private double[] getanxExamIdx(double[] anxExam){
        return anxExam;
    }

    /**
     * 师生冲突指数
     * @param heatTeacher
     * @return
     */
    private double[] getconflictIdx(double[] heatTeacher){
        return heatTeacher;
    }


}
