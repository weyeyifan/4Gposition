package com.position4g.model;

/**
 * @createAuthor zfb
 * @createTime 2017/3/30${Time}
 * @describe ${TODO}
 */

public class DevTypeBean {

    private String devNumber;
    private String devName;
    private String typeModel;
    private String phoneNumber;
    /**
     * height : 3.5
     * longitude : 0.01
     * latitude : 0.01
     */

    private DevPosBean devPos;
    private String devAddresss;
    private String mac;
    private int    paMaxPower;
    private int    devType;
    private int    devConformation;
    private int    timestamp;

    public String getDevNumber() {
        return devNumber;
    }

    public void setDevNumber(String devNumber) {
        this.devNumber = devNumber;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getTypeModel() {
        return typeModel;
    }

    public void setTypeModel(String typeModel) {
        this.typeModel = typeModel;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public DevPosBean getDevPos() {
        return devPos;
    }

    public void setDevPos(DevPosBean devPos) {
        this.devPos = devPos;
    }

    public String getDevAddresss() {
        return devAddresss;
    }

    public void setDevAddresss(String devAddresss) {
        this.devAddresss = devAddresss;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public int getPaMaxPower() {
        return paMaxPower;
    }

    public void setPaMaxPower(int paMaxPower) {
        this.paMaxPower = paMaxPower;
    }

    public int getDevType() {
        return devType;
    }

    public void setDevType(int devType) {
        this.devType = devType;
    }

    public int getDevConformation() {
        return devConformation;
    }

    public void setDevConformation(int devConformation) {
        this.devConformation = devConformation;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public static class DevPosBean {
        private double height;
        private double longitude;
        private double latitude;

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }
    }
}
