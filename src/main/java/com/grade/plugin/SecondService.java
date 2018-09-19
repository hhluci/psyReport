package com.grade.plugin;

import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.ArrayUtils;
import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description:
 * @Date: Created in 8:50 2018/9/15
 */
public class SecondService {
    final Dao dao = new Dao();
    final Subdimension subdimension = new Subdimension();

    public void firstPart(String schoolId, String gradeId, String classId){

        List<Record> stuId = dao.find(SQL.findStuId(schoolId, gradeId, classId));
        this.getAll(stuId, schoolId, gradeId, classId);
        /*for (Record list : stuId){
            Record firstRecord = new Record();
            *//**第二部分所有成绩集合*//*
            List<Record> listAll = dao.find(SQL.findSecondPart(schoolId, gradeId, classId, list.getStr("stuId")));
            *//**第二部分part列表*//*
            List<Record> listPart = dao.find(SQL.findSecondPartGroupPart(schoolId, gradeId, classId, list.getStr("stuId")));
            *//**该学生原始分数*//*
            List<Record> originalScore = new ArrayList<>();
            for (Record record : listPart){
                originalScore.addAll(subdimension.rectifyScore(this.getPart(listAll, record.getInt("part"))));
            }
            *//*firstRecord.set("schoolId", schoolId)
                    .set("gradeId", gradeId)
                    .set("classId", classId)
                    .set("stuId", list.getStr("stuId"));*//*

            //this.saveN2(this.getSST(originalScore), this.getSSB(originalScore), firstRecord);
        }*/

    }

    /**
     * 过滤分组
     * @param list
     * @param number
     * @return
     */
    private List<Record> getPart(List<Record> list, int number){
        return list.stream()
                .filter(x -> x.getInt("part") == number)
                .collect(Collectors.toList());
    }

    /**
     * 获取第一部分中某子维度集合
     * @param findAll 矫正后的原始分数
     * @param scaleTypeCode  third_scale_type_code子维度编号
     * @return
     */
    private List<Record> getScoreList(List<Record> findAll, String scaleTypeCode){
        return findAll.stream()
                .filter(x -> scaleTypeCode.equals(x.getStr("third_scale_type_code")))
                .collect(Collectors.toList());
    }

    /**
     * 获取第一部分中某子维度集合
     * @param findAll 矫正后的原始分数
     * @param scaleTypeCode  third_scale_type_code子维度编号
     * @return
     */
    private List<Record> getScoreListSecond(List<Record> findAll, String scaleTypeCode){
        return findAll.stream()
                .filter(x -> scaleTypeCode.equals(x.getStr("second_scale_type_code")))
                .collect(Collectors.toList());
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
     * @return
     */
    private double[] getListToDouble(List<Record> findAll){
        Integer[] result = findAll.stream()
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

    public double getSST(List<Record> list){
        double ave = list.stream()
                .collect(Collectors.averagingDouble(x -> x.getInt("queAns")));
        double result = 0.00;
        for (Record record : list){
            result += Math.pow(record.getInt("queAns") - ave, 2);
        }
        return result;
    }

    private double getSSB(List<Record> list){
        double result = 0.00;
        double ave = list.stream()
                .collect(Collectors.averagingDouble(x -> x.getInt("queAns")));
        String[] model = new String[]{"20100", "20200", "20300", "20400"};
        for (int i = 0; i < 4; i++){
            String mo = model[i];
            double mondel = list.stream()
                    .filter(x -> mo.equals(x.getStr("second_scale_type_code")))
                    .collect(Collectors.averagingDouble(x -> x.getInt("queAns")));
            double num = list.stream()
                    .filter(x -> mo.equals(x.getStr("second_scale_type_code")))
                    .count();
            result += num * Math.pow(mondel - ave, 2);
        }
       return result;
    }

    public void getAll(List<Record> stuId, String schoolId, String gradeId, String classId ){
        Map<String, Record> n2 = new HashMap<>();
        String testTime = "0";
        Record score;

        for (Record stu :stuId){

            Record result = new Record();
            /**第二部分某同学所有成绩集合*/
            List<Record> listAll = dao.find(SQL.findSecondPart(schoolId, gradeId, classId, stu.getStr("stuId")));
            /**第二部分part列表*/
            List<Record> listPart = dao.find(SQL.findSecondPartGroupPart(schoolId, gradeId, classId, stu.getStr("stuId")));

            /*试卷测试时间为做*/

            /**该学生原始分数*/
            List<Record> originalScore = new ArrayList<>();
            for (Record record : listPart){
                int[] temp = subdimension.getSortScore(ArrayUtils.toPrimitive(subdimension.rectifyScore(this.getPart(listAll, record.getInt("part"))).stream().map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().toArray(Double[]::new)));
                List<Record> partList = subdimension.rectifyScore(this.getPart(listAll, record.getInt("part")));
                for (int i = 0; i < temp.length; i++){
                    partList.get(i).set("sort", temp[i]);
                }
                originalScore.addAll(partList);
            }

            if (this.getSST(originalScore) == 0){
                result.set("accuracy", 0.00);
            }else {
                result.set("accuracy", this.getSSB(originalScore) / this.getSST(originalScore));
            }

            /*评分一致性*/
            String[] scale_type = new String[]{"20101","20102","20103","20201","20202","20203","20204","20205","20301","20302","20303","20401","20402","20403","20404","20405"};
            double[] scale_original = new double[16];
            double[] scale_sort = new double[16];
            for (int i = 0; i < scale_type.length; i++){
                String a = scale_type[i];
                System.out.println(a);
                scale_original[i] = originalScore.stream()
                        .filter(x -> a.equals(x.getStr("third_scale_type_code")))
                        .collect(Collectors.toList())
                        .stream()
                        .collect(Collectors.averagingDouble(x -> x.getDouble("queAns")));

                scale_sort[i] = originalScore.stream()
                        .filter(x -> a.equals(x.getStr("third_scale_type_code")))
                        .collect(Collectors.toList())
                        .stream()
                        .collect(Collectors.averagingDouble(x -> x.getDouble("sort")));

            }
            result.set("consistency", subdimension.getR(scale_original, scale_sort, 16));

            /*每个学生测谎题均分*/
            result.set("tendentiousness", originalScore.stream().filter(x -> "30101".equals(x.get("third_scale_type_code")))
                    .collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(x -> x.getDouble("queAns"))));

            /*重复题*/
            double[] replace_a = ArrayUtils.toPrimitive(originalScore.stream().filter(x -> x.getStr("question_code").contains("-1")).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().toArray(Double[]::new));
            double[] replace_b = ArrayUtils.toPrimitive(originalScore.stream().filter(x -> x.getStr("question_code").contains("-2")).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().toArray(Double[]::new));
            double rep = 0;
            for (int i = 0; i < replace_a.length; i++){
                rep += Math.abs(replace_a[i] - replace_b[i]);
            }
            result.set("stability", rep);

            /*子维度*/
            result.set("companionT", originalScore.stream().filter(x -> "20101".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)))
                    .set("parenthoodT", originalScore.stream().filter(x -> "20102".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)))
                    .set("tsRelationT", originalScore.stream().filter(x -> "20103".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)))
                    .set("agreeablenessT", originalScore.stream().filter(x -> "20201".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)))
                    .set("resilienceT", originalScore.stream().filter(x -> "20202".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)))
                    .set("selfEsteemT", originalScore.stream().filter(x -> "20203".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)))
                    .set("SelfControllT", originalScore.stream().filter(x -> "20204".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)))
                    .set("learningResponsibilityT", originalScore.stream().filter(x -> "20301".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)))
                    .set("continueTnterestT", originalScore.stream().filter(x -> "20302".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)))
                    .set("continueEffortT", originalScore.stream().filter(x -> "20303".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)))
                    .set("SocialActiveT", originalScore.stream().filter(x -> "20205".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)))
                    .set("situationSelT", originalScore.stream().filter(x -> "20401".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)))
                    .set("situationAdpT", originalScore.stream().filter(x -> "20402".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)))
                    .set("distriOfAttenT", originalScore.stream().filter(x -> "20403".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)))
                    .set("cognitiveChangeT", originalScore.stream().filter(x -> "20404".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)))
                    .set("responseModT", originalScore.stream().filter(x -> "20405".equals(x.getStr("third_scale_type_code"))).map(x -> x.getDouble("queAns")).collect(Collectors.toList()).stream().collect(Collectors.averagingDouble(Double::doubleValue)));


            n2.put(stu.getStr("stuId") , result);
        }
        /*评分标准准确性*/
        double ave_n2 = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("accuracy")));
        double s = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("accuracy")).collect(Collectors.toList()).stream().toArray(Double[]::new)));
        /*评分倾向性*/
        double ave_tend = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("accuracy")));
        double s_tend = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("tendentiousness")).collect(Collectors.toList()).stream().toArray(Double[]::new)));
        /*评分一致性*/
        double ave_cons = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("consistency")));
        double s_cons = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("consistency")).collect(Collectors.toList()).stream().toArray(Double[]::new)));
        /*评分稳定性*/
        double ave_stab = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("stability")));
        double s_stab = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("stability")).collect(Collectors.toList()).stream().toArray(Double[]::new)));
        /*子维度*/
        double ave_companionT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("companionT")));
        double s_companionT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("companionT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_parenthoodT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("parenthoodT")));
        double s_parenthoodT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("parenthoodT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_tsRelationT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("tsRelationT")));
        double s_tsRelationT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("tsRelationT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_agreeablenessT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("agreeablenessT")));
        double s_agreeablenessT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("agreeablenessT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_resilienceT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("resilienceT")));
        double s_resilienceT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("resilienceT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_selfEsteemT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("selfEsteemT")));
        double s_selfEsteemT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("selfEsteemT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_SelfControllT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("SelfControllT")));
        double s_SelfControllT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("SelfControllT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_learningResponsibilityT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("learningResponsibilityT")));
        double s_learningResponsibilityT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("learningResponsibilityT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_continueTnterestT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("continueTnterestT")));
        double s_continueTnterestT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("continueTnterestT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_continueEffortT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("continueEffortT")));
        double s_continueEffortT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("continueEffortT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_SocialActiveT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("SocialActiveT")));
        double s_SocialActiveT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("SocialActiveT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_situationSelT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("situationSelT")));
        double s_situationSelT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("situationSelT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_situationAdpT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("situationAdpT")));
        double s_situationAdpT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("situationAdpT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_distriOfAttenT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("distriOfAttenT")));
        double s_distriOfAttenT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("distriOfAttenT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_cognitiveChangeT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("cognitiveChangeT")));
        double s_cognitiveChangeT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("cognitiveChangeT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        double ave_responseModT = n2.values().stream().collect(Collectors.averagingDouble(x -> x.getDouble("responseModT")));
        double s_responseModT = subdimension.getStandardDeviation(ArrayUtils.toPrimitive(n2.values().stream().map(x -> x.getDouble("responseModT")).collect(Collectors.toList()).stream().toArray(Double[]::new)));

        for (String key : n2.keySet()){
            score = new Record()
                    .set("schoolId", schoolId)
                    .set("gradeId", gradeId)
                    .set("classId", classId)
                    .set("stuId", key)
                    //.set("testDate", testTime)
                    .set("accuracy", new DecimalFormat("0.00").format((n2.get(key).getDouble("accuracy") - ave_n2) / s * 10 + 50))
                    .set("tendentiousness", new DecimalFormat("0.00").format((n2.get(key).getDouble("tendentiousness") - ave_tend) / s_tend * 10 + 50))
                    .set("stability", new DecimalFormat("0.00").format((n2.get(key).getDouble("stability") - ave_stab) / s_stab * 10 + 50))
                    .set("consistency", new DecimalFormat("0.00").format((n2.get(key).getDouble("consistency") - ave_cons) / s_cons * 10 + 50))
                    .set("companionT", new DecimalFormat("0.00").format((n2.get(key).getDouble("companionT") - ave_cons) / s_cons * 10 + 50))
                    .set("parenthoodT", new DecimalFormat("0.00").format((n2.get(key).getDouble("parenthoodT") - ave_cons) / s_cons * 10 + 50))
                    .set("tsRelationT", new DecimalFormat("0.00").format((n2.get(key).getDouble("tsRelationT") - ave_cons) / s_cons * 10 + 50))
                    .set("agreeablenessT", new DecimalFormat("0.00").format((n2.get(key).getDouble("agreeablenessT") - ave_cons) / s_cons * 10 + 50))
                    .set("resilienceT", new DecimalFormat("0.00").format((n2.get(key).getDouble("resilienceT") - ave_cons) / s_cons * 10 + 50))
                    .set("selfEsteemT", new DecimalFormat("0.00").format((n2.get(key).getDouble("selfEsteemT") - ave_cons) / s_cons * 10 + 50))
                    .set("SelfControllT", new DecimalFormat("0.00").format((n2.get(key).getDouble("SelfControllT") - ave_cons) / s_cons * 10 + 50))
                    .set("learningResponsibilityT", new DecimalFormat("0.00").format((n2.get(key).getDouble("learningResponsibilityT") - ave_cons) / s_cons * 10 + 50))
                    .set("continueTnterestT", new DecimalFormat("0.00").format((n2.get(key).getDouble("learningResponsibilityT") - ave_cons) / s_cons * 10 + 50))
                    .set("continueEffortT", new DecimalFormat("0.00").format((n2.get(key).getDouble("continueEffortT") - ave_cons) / s_cons * 10 + 50))
                    .set("SocialActiveT", new DecimalFormat("0.00").format((n2.get(key).getDouble("SocialActiveT") - ave_cons) / s_cons * 10 + 50))
                    .set("situationSelT", new DecimalFormat("0.00").format((n2.get(key).getDouble("situationSelT") - ave_cons) / s_cons * 10 + 50))
                    .set("situationAdpT", new DecimalFormat("0.00").format((n2.get(key).getDouble("situationAdpT") - ave_cons) / s_cons * 10 + 50))
                    .set("distriOfAttenT", new DecimalFormat("0.00").format((n2.get(key).getDouble("distriOfAttenT") - ave_cons) / s_cons * 10 + 50))
                    .set("cognitiveChangeT", new DecimalFormat("0.00").format((n2.get(key).getDouble("cognitiveChangeT") - ave_cons) / s_cons * 10 + 50))
                    .set("responseModT", new DecimalFormat("0.00").format((n2.get(key).getDouble("responseModT") - ave_cons) / s_cons * 10 + 50));
            dao.saveSecond(score);
        }

    }



}
