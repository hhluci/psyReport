package com.grade.plugin;

import com.jfinal.plugin.activerecord.Record;
import com.sun.org.apache.regexp.internal.RECompiler;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.descriptive.summary.SumOfSquares;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description: 子维度计算
 * @Date: Created in 20:34 2018/9/4
 */
public class Subdimension {

    /**
     * 计算反向计分
     * @param queAns
     * @param optionNumber
     * @return
     */
    private double reverseScoring(double queAns, String optionNumber){
        switch (optionNumber){
            case "2":
                queAns = 3 - queAns;
                break;
            case "4":
                queAns = 5 - queAns;
                break;
            case "7":
                queAns = 8 - queAns;
                break;
        }
        return queAns;
    }

    /**
     * 判断是否反向计分
     * @param reverseScoring
     * @return
     */
    private boolean reverseScoring(String reverseScoring){
        return "1".equals(reverseScoring);
    }

    /**
     * 计算一组数据中的反向成绩
     * @param score
     * @return
     */
    private List<Record> primitive(List<Record> score){
        for (Record record : score){
            if (this.reverseScoring(record.getStr("reverse_scoring"))){
                record.set("queAns", this.reverseScoring(record.getDouble("queAns"), record.getStr("option_number")));
            }
        }
        return score;
    }

    /**
     * 计算子维度原始分数
     * @param ansSubdimension 某人指定子维度原始分
     * @return
     */
    public double primitiveScore(List<Record> ansSubdimension){

        if (ansSubdimension.size() == 0){
            return 0.00;
        }

        double sum = 0;
        for (Record ans : ansSubdimension){
            if ("1".equals(ans.getStr("reverse_scoring"))){
                sum += this.reverseScoring(ans.getInt("queAns"), ans.getStr("option_number"));
            }else {
                sum += ans.getInt("queAns");
            }
        }

        return Double.parseDouble(new DecimalFormat("0.00").format(sum / ansSubdimension.size()));
    }

    /**
     * 计算L成绩  测谎
     * @param answer
     * @return
     */
    public int getL(List<Record> answer){
        int num = 0;
        for (Record ans : answer){
            if (!this.reverseScoring(ans.getStr("reverse_scoring"))){
                if ("2".equals(ans.getStr("option_number")) && ans.getInt("queAns") == 2){
                    num++;
                }else if ("4".equals(ans.getStr("option_number")) && ans.getInt("queAns") == 4){
                    num++;
                }else if ("7".equals(ans.getStr("option_number")) && ans.getInt("queAns") == 7){
                    num++;
                }
            }else {
                if (ans.getInt("queAns") == 1){
                    num++;
                }
            }
        }
        return num;
    }

    /**
     * 获取F类型成绩  诈谎
     * @param answer
     * @return
     */
    public int getF(List<Record> answer){
        int num = 0;
        for (Record ans : answer){
            if (!this.reverseScoring(ans.getStr("reverse_scoring"))){
                if (ans.getInt("queAns") == 4){
                    num++;
                }
            }else {
                if (ans.getInt("queAns") == 1){
                    num++;
                }
            }
        }
        return num;
    }

    /**
     * 获取C指数  重复题计分
     * @param answerRepeat1 题号后为 %-1
     * @param answerRepeat2 题号后为 %-2
     * @return
     */
    public int getC(List<Record> answerRepeat1, List<Record> answerRepeat2){
        int num = 0;
        for (Record ans2 : answerRepeat2){
            for (Record ans1 : answerRepeat1){
                if (ans2.getStr("question_code").substring(0, ans2.getStr("question_code").lastIndexOf("-"))
                        .equals(ans1.getStr("question_code").substring(0, ans1.getStr("question_code").lastIndexOf("-")))){
                    switch (ans2.getStr("option_number")){
                        case "2":
                            if (!ans1.getInt("queAns").equals(ans2.getInt("queAns"))){
                                num++;
                            }
                            break;
                        case "4":
                            if (Math.abs(ans1.getInt("queAns") - ans2.getInt("queAns")) > 2){
                                num++;
                            }
                            break;
                        case "7":
                            if (Math.abs(ans1.getInt("queAns") - ans2.getInt("queAns")) > 2){
                                num++;
                            }
                            break;
                    }
                }
            }
        }
        return num;
    }


    /**
     * 对所有作答有效的学生，计算相同子维度上的平均分
     * @param answer 每个学生在相同子维度下的原始分（完成反向处理）
     * @return
     */
    public double getAverage(double[] answer){
        if (answer.length == 0){
            return 0.00;
        }
        return Double.parseDouble(new DecimalFormat("0.00").format(new Mean().evaluate(answer)));
    }

    /**
     * 对所有作答有效的学生，计算相同子维度上的标准差
     * @param answer 每个学生在相同子维度下的原始分（完成反向处理）
     * @return
     */
    public double getStandardDeviation(double[] answer){
        if (answer.length == 0){
            return 0.000;
        }
        try {
            return Double.parseDouble(new DecimalFormat("0.000").format(new StandardDeviation(false).evaluate(answer)));
        } catch (NumberFormatException | MathIllegalArgumentException e) {
            e.printStackTrace();
            return 0.00;
        }
    }

    /**
     * 求每个学生的相同维度上的Z分数
     * @param score 每个学生相同维度的原始分
     * @return
     */
    public double[] getZ(double[] score){
        double[] zScore = new double[score.length];
        double average = this.getAverage(score);
        double standard = this.getStandardDeviation(score);
        try {
            for (int i = 0; i < score.length; i++){
                zScore[i] = (score[i] - average) / standard;
            }
            return zScore;
        } catch (Exception e) {
            return new double[]{0.00};
        }
    }

    /**
     * 求每个学生的相同维度上的T分数
     * @param score 每个学生相同维度的原始分
     * @return
     */
    public double[] getT(double[] score){

        if (score.length == 0){
            return new double[]{0.00};
        }

        double[] Z = this.getZ(score);
        double[] result = new double[Z.length];
        for (int i = 0; i < Z.length; i++){
            result[i] =  Z[i] * 10 + 50;
        }
        return result;
    }

    /**
     * 求某个学生的某个维度的百分等级
     * @param score 每个学生相同维度的原始分
     * @return
     */
    public double[] getPercentLevel(double[] score){

        double[] zScore = this.getZ(score);
        double[] result = new double[zScore.length];
        for (int i = 0; i < zScore.length; i++){
            double sum = new NormalDistribution().cumulativeProbability(zScore[i]);
            result[i] = Double.parseDouble(new DecimalFormat("0.00").format((1 - sum) * 100));
            System.out.println(result[i]);
        }
        return result;
    }

    /**
     * 求学生的SST分数
     * @param score 某个学生所有题目原始成绩数组（完成反向处理）
     * @return
     */
    public double getSST(double[] score){
        double average = this.getAverage(score);
        double[] result = new double[score.length];
        for (int i = 0; i < score.length; i++){
            result[i] = score[i] - average;
        }
        return Double.parseDouble(new DecimalFormat("0.00").format(new SumOfSquares().evaluate(result)));
    }

    /**
     * 求某个学生的SSB分数
     * @param number 某个学生每个维度上的题目数
     * @param average 某个学生每个维度上的平均分数（反向处理）
     * @param score 某个学生所哟题目原始成绩数组（完成反向处理）
     * @return
     */
    public double getSSB(double[] number, double[] average, double[] score){
        double ave = this.getAverage(score);
        double result = 0;
        for (int i = 0; i < number.length; i++){
            result += number[i] * Math.pow(average[i] - ave, 2);
        }
        return Double.parseDouble(new DecimalFormat("0.00").format(result));
    }

    /**
     * 计算学生η2 的值
     * @param number
     * @param average
     * @param score
     * @return
     */
    public double getN2(double[] number, double[] average, double[] score){
        double SST = this.getSST(score);
        double SSB = this.getSSB(number, average, score);
        return Double.parseDouble(new DecimalFormat("0.00").format(SST/SSB));
    }

    /**
     * 求学生每组题目的排序成绩
     * @param score
     * @return
     */
    public int[] getSortScore(double[] score){
        int[] result = new int[score.length];
        double[] sort = score;
        List list = new ArrayList();
        Arrays.sort(sort);
        for (int i = 0; i < score.length; i++){
            list.add(sort[i]);
        }
        for (int j = 0; j < score.length; j++){
            result[j] = list.indexOf(score[j]) + 1;
        }
        return result;
    }

    /**
     * 求子维度原始分数的平均数和排序分数的平均数之间的相关系数
     * @param originalScore 子维度原始分数数组
     * @param sortScore 子维度排序分数数组
     * @param number 组数
     * @return
     */
    public double getR(double[] originalScore, double[] sortScore, int number){

        double averageOriginal = this.getAverage(originalScore);
        double averageSort = this.getAverage(sortScore);
        double standardOriginal = this.getStandardDeviation(originalScore);
        double standardSort = this.getStandardDeviation(sortScore);
        if (standardOriginal == 0 || standardSort ==0){
            return 0.00;
        }
        double sum = 0;
        for (int i = 0; i < number; i++){
            sum += (originalScore[i] - averageOriginal) * (sortScore[i] - averageSort);
        }
        double result = sum / (number * standardOriginal * standardSort);
        return Double.parseDouble(new DecimalFormat("0.00").format(result));
    }

    /**
     * 矫正分数
     * @param score 某同学某一组成绩 size = 6
     * @return
     */
    public List<Record> rectifyScore(List<Record> score){

        Map<Object, List<Record>> map = this.getRepeateGroup(score);
        for (Object object : map.keySet()){
            this.getRectify(map.get(object).size(), map.get(object));
        }
        return this.primitive(score);
    }

    /**
     * 获取矫正分数
     * @param number
     * @param score
     * @return
     */
    public List<Record> getRectify(int number, List<Record> score){

        switch (number){
            case 2:
                score.get(0).set("queAns", score.get(0).getInt("queAns") + 0.25);
                score.get(1).set("queAns", score.get(1).getInt("queAns") - 0.25);
                break;
            case 3:
                score.get(0).set("queAns", score.get(0).getInt("queAns") + 0.33);
                score.get(2).set("queAns", score.get(2).getInt("queAns") - 0.33);
                break;
            case 4:
                score.get(0).set("queAns", score.get(0).getInt("queAns") + 0.375);
                score.get(1).set("queAns", score.get(1).getInt("queAns") + 0.125);
                score.get(2).set("queAns", score.get(2).getInt("queAns") - 0.125);
                score.get(3).set("queAns", score.get(3).getInt("queAns") - 0.375);
                break;
            case 5:
                score.get(0).set("queAns", score.get(0).getInt("queAns") + 0.4);
                score.get(1).set("queAns", score.get(1).getInt("queAns") + 0.2);
                score.get(3).set("queAns", score.get(3).getInt("queAns") - 0.2);
                score.get(4).set("queAns", score.get(4).getInt("queAns") - 0.4);
                break;
            case 6:
                score.get(0).set("queAns", score.get(0).getInt("queAns") + 0.417);
                score.get(1).set("queAns", score.get(1).getInt("queAns") + 0.25);
                score.get(2).set("queAns", score.get(2).getInt("queAns") + 0.08);
                score.get(3).set("queAns", score.get(3).getInt("queAns") - 0.08);
                score.get(4).set("queAns", score.get(4).getInt("queAns") - 0.25);
                score.get(5).set("queAns", score.get(5).getInt("queAns") - 0.417);
                break;
            default:
                break;
        }

        return score;
    }

    /**
     * 返回分组后的Map集合
     * @param list
     * @return
     */
    public Map<Object, List<Record>> getRepeateGroup(List<Record> list){
        Map<Object, List<Record>> listMap = list.stream()
                .collect(Collectors.groupingBy(x -> x.getInt("queAns")))
                .entrySet()
                .stream()
                .filter(x -> x.getValue().size() >= 2)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
        return listMap;
    }


}
