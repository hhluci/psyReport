package com.grade.empty;

import java.util.List;

/**
 * @Author: yang
 * @ProjectName: grade
 * @Package: com.grade.empty
 * @Description:
 * @Date: Created in 21:51 2018/11/1
 */
public class JsonToObject {

    /**
     * model : 1
     * date : [{"schoolId":"","gradeId":"","classId":""},{"schoolId":"","gradeId":"","classId":""}]
     */

    /**
     * model：操作的类型
     * 只计算个人问题行为：
     * 只计算个人积极心理品质：
     * 只计算个人问题行为和积极心理品质：
     * 只计算班级问题行为：
     * 只计算班级积极心理品质：
     * 只计算班级班级问题行为和积极心理品质：
     */
    private int model;
    private List<DateBean> date;

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public List<DateBean> getDate() {
        return date;
    }

    public void setDate(List<DateBean> date) {
        this.date = date;
    }

    public static class DateBean {
        /**
         * schoolId :
         * gradeId :
         * classId :
         */

        private String schoolId;
        private String gradeId;
        private String classId;

        public String getSchoolId() {
            return schoolId;
        }

        public void setSchoolId(String schoolId) {
            this.schoolId = schoolId;
        }

        public String getGradeId() {
            return gradeId;
        }

        public void setGradeId(String gradeId) {
            this.gradeId = gradeId;
        }

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }
    }
}
