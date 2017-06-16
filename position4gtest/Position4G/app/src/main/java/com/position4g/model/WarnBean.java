package com.position4g.model;

/**
 * @createAuthor zfb
 * @createTime 2017/4/6${Time}
 * @describe ${TODO}
 */

public class WarnBean {


    private int      alarmType;


    private DataBean data;
    private int timestamp;

    public int getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(int alarmType) {
        this.alarmType = alarmType;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public static class DataBean {
        private int    alarmCode;
        private String msg;

        public int getAlarmCode() {
            return alarmCode;
        }

        public void setAlarmCode(int alarmCode) {
            this.alarmCode = alarmCode;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
