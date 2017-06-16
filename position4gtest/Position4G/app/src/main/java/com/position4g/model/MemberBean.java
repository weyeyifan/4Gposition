package com.position4g.model;

/**
 * @createAuthor zfb
 * @createTime 2017/3/28${Time}
 * @describe ${TODO}
 */

public class MemberBean {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String name;
    public String imsi;
    public int    location;
    public int    gender;
    public String phone;
    public String remark;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked;
}
