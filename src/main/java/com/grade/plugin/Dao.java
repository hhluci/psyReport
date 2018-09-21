package com.grade.plugin;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.druid.DruidPlugin;

import java.util.List;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.plugin
 * @Description: 数据库操作
 * @Date: Created in 12:37 2018/9/3
 */
public class Dao {

    DruidPlugin druidPlugin = new DruidPlugin("jdbc:mysql://127.0.0.1:3306/bank?characterEncoding=utf8&useSSL=false", "root", "root");


    public Dao(String configName) {
        ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(configName, druidPlugin);
        druidPlugin.start();
        activeRecordPlugin.start();
    }

    /**
     * 查询数据库
     * @param sql
     * @return
     */
    public List<Record> find(String sql){
        return Db.find(sql);
    }

    /**
     * 查询数据库模板  只需更改where字段即可
     * @param sql
     * @return
     */
    public List<Record> findTemplate(String sql){
        String SQL = "SELECT\n" +
                "\ta.schoolId, a.gradeId, a.classId, a.stuId, a.queAns, a.queSeq, a.startTime, a.submitTime,\n" +
                "  b.mode_code, b.third_scale_type_code, b.question_code, b.option_number, b.reverse_scoring\n" +
                "FROM\n" +
                "\tanswer a\n" +
                "JOIN question_bank b ON a.queCode = b.question_code \n" +
                "WHERE\t";
        return Db.find(SQL + sql);
    }

    public boolean save(Record firstPart){
        return Db.save("tbproblembehavrep", firstPart);
    }

    public boolean saveSecond(Record record){
        return Db.save("tbpsyqualityrep", record);
    }

    public boolean update(String table, String primaryKey, Record date){
        return Db.update(table, primaryKey, date);
    }


}
