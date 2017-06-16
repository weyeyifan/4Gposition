package com.position4g.model;

/**
 * @createAuthor zfb
 * @createTime 2017/3/27${Time}
 * @describe ${TODO}
 */

public class SyncBean {

    public String getBbu() {
        return bbu;
    }

    public void setBbu(String bbu) {
        this.bbu = bbu;
    }

    public String getBbuStatus() {
        return bbuStatus;
    }

    public void setBbuStatus(String bbuStatus) {
        this.bbuStatus = bbuStatus;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSyncstatus() {
        return syncstatus;
    }

    public void setSyncstatus(int syncstatus) {
        this.syncstatus = syncstatus;
    }

    public String bbu;
    public String    bbuStatus;
    public String    location;
    public int    syncstatus;
}
