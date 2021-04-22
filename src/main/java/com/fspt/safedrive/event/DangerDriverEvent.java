package com.fspt.safedrive.event;

/**
 * Create by Spring
 */
public class DangerDriverEvent {
    private String eyeCloseScore;
    private String cellPhoneScore;
    private String smokeScore;
    private String yawningScore;
    // 还可以有其它事件，比如双手离开方向盘，每个属性建议生成 set get方法

    private int type;  // 预留，比如可以明显区分为  eye yawning

    private String dangerDriverInfo;

    public DangerDriverEvent(){

    }
    public DangerDriverEvent(int type){
        this.type = type;
    }

    public DangerDriverEvent(int type,String dangerDriverInfo) {
        this.type = type;
        this.dangerDriverInfo = dangerDriverInfo;
    }

    public String getEyeCloseScore() {
        return eyeCloseScore;
    }

    public void setEyeCloseScore(String eyeCloseScore) {
        this.eyeCloseScore = eyeCloseScore;
    }

    public String getCellPhoneScore() {
        return cellPhoneScore;
    }

    public void setCellPhoneScore(String cellPhoneScore) {
        this.cellPhoneScore = cellPhoneScore;
    }

    public String getSmokeScore() {
        return smokeScore;
    }

    public void setSmokeScore(String smokeScore) {
        this.smokeScore = smokeScore;
    }

    public String getYawningScore() {
        return yawningScore;
    }

    public void setYawningScore(String yawningScore) {
        this.yawningScore = yawningScore;
    }


    public String getDangerDriverInfo() {
        return dangerDriverInfo;
    }


    public void setDangerDriverInfo(String dangerDriverInfo) {
        this.dangerDriverInfo = dangerDriverInfo;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "DangerDriverEvent{" + "\r\n" +
                "eyeCloseScore='" + eyeCloseScore + '\'' + "\r\n" +
                ", cellPhoneScore='" + cellPhoneScore + '\''  + "\r\n" +
                ", smokeScore='" + smokeScore + '\''  + "\r\n" +
                ", yawningScore='" + yawningScore + '\''  + "\r\n" +
                '}';
    }
}
