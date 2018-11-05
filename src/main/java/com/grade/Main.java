package com.grade;

import com.google.gson.Gson;
import com.grade.empty.JsonToObject;
import com.grade.plugin.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade
 * @Description: 分数计算入口（如何判断计算的是学校的， 班级的， 个人的， 积极心理的还是 问题行为的）
 * @Date: Created in 21:46 2018/9/17
 */
public class Main {

    /*计算个人问题行为分数*/
    static final int PERSON_PROBLEM_ACTION = 1;
    /*计算个人积极心理品质分数*/
    static final int PERSON_POSITIVE_PSYCHOLOGY = 2;
    /*计算个人问题行为和积极心理品质*/
    static final int PERSON_ACTION_AND_PSYCHOLOGY = 3;
    /*计算班级问题行为成绩*/
    static final int CLASS_PROBLEM_ACTION = 4;
    /*计算班级积极心理品质成绩*/
    static final int CLASS_POSITIVE_PSYCHOLOGY = 5;
    /*计算班级问题行为和积极性心理品质*/
    static final int CLASS_ACTION_AND_PSYCHOLOGY = 6;

    /**初始化线程池*/
    private static ExecutorService mFixedThreadPool= new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args){
        /*提取出数据库配置文件，防止出现配置文件争夺的空指针错误*/
        Dao dao = new Dao("mainDao");
        JsonToObject json = parseFile(args[0]);
        List<JsonToObject.DateBean> result = json.getDate();


        switch (json.getModel()){
            case PERSON_PROBLEM_ACTION:
                System.out.println("开始计算个人问题行为成绩");
                for (JsonToObject.DateBean bean : result){
                    //System.out.println(bean.getSchoolId() + "---" + bean.getGradeId() + "---"+ bean.getClassId());
                    mFixedThreadPool.execute(() -> new FirstService().firstPart(bean.getSchoolId(), bean.getGradeId(), bean.getClassId(), dao));
                }
                break;
            case PERSON_POSITIVE_PSYCHOLOGY:
                System.out.println("开始计算个人积极心理品质成绩");
                for (JsonToObject.DateBean bean : result){
                    mFixedThreadPool.execute(() -> new SecondService().firstPart(bean.getSchoolId(), bean.getGradeId(), bean.getClassId(), dao));
                }
                break;
            case PERSON_ACTION_AND_PSYCHOLOGY:
                System.out.println("开始计算个人问题行为和积极心理品质成绩");
                for (JsonToObject.DateBean bean : result){
                    mFixedThreadPool.execute(() -> new FirstService().firstPart(bean.getSchoolId(), bean.getGradeId(), bean.getClassId(), dao));
                    mFixedThreadPool.execute(() -> new SecondService().firstPart(bean.getSchoolId(), bean.getGradeId(), bean.getClassId(), dao));
                }
                break;
            case CLASS_PROBLEM_ACTION:
                System.out.println("开始计算班级问题行为成绩");
                for (JsonToObject.DateBean bean : result){
                    mFixedThreadPool.execute(() -> new ClassActionService().getClassAction(bean.getSchoolId(), bean.getGradeId(), bean.getClassId(), dao));
                }
                break;
            case CLASS_POSITIVE_PSYCHOLOGY:
                System.out.println("开始计算班级积极心理品质成绩");
                for (JsonToObject.DateBean bean : result){
                    mFixedThreadPool.execute(() -> new ClassPositivePsychology().getClassPsychology(bean.getSchoolId(), bean.getGradeId(), bean.getClassId(), dao));
                }
                break;
            case CLASS_ACTION_AND_PSYCHOLOGY:
                System.out.println("开始计算班级问题行为和积极心理品质成绩");
                for (JsonToObject.DateBean bean : result){
                    mFixedThreadPool.execute(() -> new ClassActionService().getClassAction(bean.getSchoolId(), bean.getGradeId(), bean.getClassId(), dao));
                    mFixedThreadPool.execute(() -> new ClassPositivePsychology().getClassPsychology(bean.getSchoolId(), bean.getGradeId(), bean.getClassId(), dao));
                }
                break;
            default:
                break;
        }
        //顺序关闭一次线程，执行未完成的线程，不接受新的任务
        mFixedThreadPool.shutdown();
        while (true){
            if (mFixedThreadPool.isTerminated()){
                System.out.println("计算完成");
                break;
            }
            System.out.println("计算中。。。");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将json资源文件转为JavaBean对象
     * @param filePath
     * @return
     */
    public static JsonToObject parseFile(String filePath){
        String encoding = "UTF-8";
        File file = new File(filePath);
        String result = null;
        FileInputStream fileInputStream = null;
        Gson gson = new Gson();

        if (file.length() == 0){
            return null;
        }
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(fileContent);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("文件解析失败，请检查文件及文件路径是否合法！");
            return null;
        }finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            result = new String(fileContent, encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.err.println("文件转为UTF-8错误，请检查文件合法性！");
            return null;
        }
        return gson.fromJson(result, JsonToObject.class);
    }

    /**
     * 在执行jar包时传入参数
     * 限定jar包后第一个参数必须为文件路径
     * @param args
     */
    /*public static void main(String[] args) {

        System.out.println(args[0]);
        get(parseFile(args[0]));
        //顺序关闭一次线程，执行未完成的线程，不接受新的任务
        mFixedThreadPool.shutdown();
        while (true){
            if (mFixedThreadPool.isTerminated()){
                System.out.println("计算完成");
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

}


