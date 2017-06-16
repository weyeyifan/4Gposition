package com.position4g.model;

/**
 * @createAuthor zfb
 * @createTime 2017/3/22${Time}
 * @describe ${TODO}
 */

public class TargetBean {

    /**
     * imsi : 460000560736203
     * delay : 11
     * sinr : 12
     * freq : 39098
     * bbu : 1
     * rsrp : 12
     */

    private String imsi;
    private String delay;
    private String freq;
    private String bbu;
    private float rsrp;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private String time;
    private int    count;

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getBbu() {
        return bbu;
    }

    public void setBbu(String bbu) {
        this.bbu = bbu;
    }

    public Float getRsrp() {
        return rsrp;
    }

    public void setRsrp(float rsrp) {
        this.rsrp = rsrp;
    }
}
