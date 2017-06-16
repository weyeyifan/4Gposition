package com.position4g.model;

import java.util.List;

/**
 * @createAuthor zfb
 * @createTime 2017/3/28${Time}
 * @describe ${TODO}
 */

public class PaTemperBean {
    private int                   autoSniffer;
    private  int                   boardtemp;
    private  int                   cpu;
    private  int                   devtemp;
    private  int                   memory;
    private  int                   smsSwitch;
    private  int                   uptime;
    private  int                   wirelessSwitch;
    /**
     * paNumber : 1
     * power : 40
     */

    private  List<PaPowerBean>     paPower;
    /**
     * paNumber : 1
     * paSwitch : 0
     */

    private  List<PaSwitchBean>    paSwitch;
    /**
     * paNumber : 1
     * waveRadio : 0
     */

    private  List<PaWaveRatioBean> paWaveRatio;
    /**
     * paNumber : 1
     * temp : 0
     */

    private  List<PatempBean>      patemp;
    /**
     * cellNumber : 1
     * syncmethod : 0
     * syncstatus : 0
     */

    private  List<SyncInfoBean>    syncInfo;

    public int getAutoSniffer() {
        return autoSniffer;
    }

    public void setAutoSniffer(int autoSniffer) {
        this.autoSniffer = autoSniffer;
    }

    public int getBoardtemp() {
        return boardtemp;
    }

    public void setBoardtemp(int boardtemp) {
        this.boardtemp = boardtemp;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getDevtemp() {
        return devtemp;
    }

    public void setDevtemp(int devtemp) {
        this.devtemp = devtemp;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getSmsSwitch() {
        return smsSwitch;
    }

    public void setSmsSwitch(int smsSwitch) {
        this.smsSwitch = smsSwitch;
    }

    public int getUptime() {
        return uptime;
    }

    public void setUptime(int uptime) {
        this.uptime = uptime;
    }

    public int getWirelessSwitch() {
        return wirelessSwitch;
    }

    public void setWirelessSwitch(int wirelessSwitch) {
        this.wirelessSwitch = wirelessSwitch;
    }

    public List<PaPowerBean> getPaPower() {
        return paPower;
    }

    public void setPaPower(List<PaPowerBean> paPower) {
        this.paPower = paPower;
    }

    public List<PaSwitchBean> getPaSwitch() {
        return paSwitch;
    }

    public void setPaSwitch(List<PaSwitchBean> paSwitch) {
        this.paSwitch = paSwitch;
    }

    public List<PaWaveRatioBean> getPaWaveRatio() {
        return paWaveRatio;
    }

    public void setPaWaveRatio(List<PaWaveRatioBean> paWaveRatio) {
        this.paWaveRatio = paWaveRatio;
    }

    public List<PatempBean> getPatemp() {
        return patemp;
    }

    public void setPatemp(List<PatempBean> patemp) {
        this.patemp = patemp;
    }

    public List<SyncInfoBean> getSyncInfo() {
        return syncInfo;
    }

    public void setSyncInfo(List<SyncInfoBean> syncInfo) {
        this.syncInfo = syncInfo;
    }

    public static class PaPowerBean {
        private int paNumber;
        private int power;

        public int getPaNumber() {
            return paNumber;
        }

        public void setPaNumber(int paNumber) {
            this.paNumber = paNumber;
        }

        public int getPower() {
            return power;
        }

        public void setPower(int power) {
            this.power = power;
        }
    }

    public static class PaSwitchBean {
        private int paNumber;
        private int paSwitch;

        public int getPaNumber() {
            return paNumber;
        }

        public void setPaNumber(int paNumber) {
            this.paNumber = paNumber;
        }

        public int getPaSwitch() {
            return paSwitch;
        }

        public void setPaSwitch(int paSwitch) {
            this.paSwitch = paSwitch;
        }
    }

    public static class PaWaveRatioBean {
        private int paNumber;
        private int waveRadio;

        public int getPaNumber() {
            return paNumber;
        }

        public void setPaNumber(int paNumber) {
            this.paNumber = paNumber;
        }

        public int getWaveRadio() {
            return waveRadio;
        }

        public void setWaveRadio(int waveRadio) {
            this.waveRadio = waveRadio;
        }
    }

    public static class PatempBean {
        private int paNumber;
        private int temp;

        public int getPaNumber() {
            return paNumber;
        }

        public void setPaNumber(int paNumber) {
            this.paNumber = paNumber;
        }

        public int getTemp() {
            return temp;
        }

        public void setTemp(int temp) {
            this.temp = temp;
        }
    }

    public static class SyncInfoBean {
        private String cellNumber;
        private int syncmethod;
        private int syncstatus;

        public String getCellNumber() {
            return cellNumber;
        }

        public void setCellNumber(String cellNumber) {
            this.cellNumber = cellNumber;
        }

        public int getSyncmethod() {
            return syncmethod;
        }

        public void setSyncmethod(int syncmethod) {
            this.syncmethod = syncmethod;
        }

        public int getSyncstatus() {
            return syncstatus;
        }

        public void setSyncstatus(int syncstatus) {
            this.syncstatus = syncstatus;
        }
    }
}
