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

    DruidPlugin druidPlugin = new DruidPlugin("jdbc:mysql://127.0.0.1:3306/bank1?characterEncoding=utf8&useSSL=false", "root", "root");


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
    public  List<Record> find(String sql){
        return Db.find(sql);
    }

    public boolean save(Record firstPart){
        return Db.save("tbproblembehavrep", firstPart);
    }

    public boolean saveSecond(Record record){
        return Db.save("tbpsyqualityrep", record);
    }

    public boolean saveClassAction(Record record){
        return Db.save("class_problem_behavior", record);
    }

    public boolean saveClassPsychology(Record record){
        return Db.save("class_positive_psychology", record);
    }

    public boolean update(String table, String primaryKey, Record date){
        return Db.update(table, primaryKey, date);
    }


}
