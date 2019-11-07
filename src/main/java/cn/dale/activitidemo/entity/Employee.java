package cn.dale.activitidemo.entity;

import cn.dale.activitidemo.enums.ApplyStatusEnum;

import java.io.Serializable;

/**
 * @Author: dale
 * @Date: 2019/11/6 14:52
 */
public class Employee implements Serializable{

    //职员姓名
    private String employeeName;

    //请假天数
    private int day;

    //结果状态
    private ApplyStatusEnum applyStatus;

    public Employee(String employeeName, int day) {
        this.employeeName = employeeName;
        this.day = day;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }


    public ApplyStatusEnum getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(ApplyStatusEnum applyStatus) {
        this.applyStatus = applyStatus;
    }
}
