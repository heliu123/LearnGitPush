package com.example.springBoot.CountdownLatchTest.bean;

/**
 * @author heliu
 * @date 2021/1/13 15:03.
 */
public class ActivityBean {
    private String whiteDefineId;
    private String activityName;
    private String activityType;

    public ActivityBean(String whiteDefineId) {
        this.whiteDefineId = whiteDefineId;
    }

    public String getWhiteDefineId() {
        return whiteDefineId;
    }

    public void setWhiteDefineId(String whiteDefineId) {
        this.whiteDefineId = whiteDefineId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    @Override
    public String toString() {
        return "ActivityBean{" +
                "whiteDefineId='" + whiteDefineId + '\'' +
                ", activityName='" + activityName + '\'' +
                '}';
    }
}
