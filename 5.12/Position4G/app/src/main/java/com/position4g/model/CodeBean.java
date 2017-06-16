package com.position4g.model;

/**
 * @createAuthor zfb
 * @createTime 2017/4/12${Time}
 * @describe ${TODO}
 */

public class CodeBean {

    /**
     * cellNumber : 2
     * imei : 486544563372875
     * imsi : 460020286433452260
     * tmsi :
     * uptime : 1426014675
     */

    private int cellNumber;
    private String imei;
    private String imsi;
    private String tmsi;
    private int    uptime;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count;

    public int getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(int cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getTmsi() {
        return tmsi;
    }

    public void setTmsi(String tmsi) {
        this.tmsi = tmsi;
    }

    public int getUptime() {
        return uptime;
    }

    public void setUptime(int uptime) {
        this.uptime = uptime;
    }
}
