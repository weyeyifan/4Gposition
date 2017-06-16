package com.position4g.model;

/**
 * @createAuthor zfb
 * @createTime 2017/4/7${Time}
 * @describe ${TODO}
 */

public class ResultBean {

    /**
     * result : 0x1050
     * cmdType : 0x1060
     * code : 0x1070
     * msg :
     * timestamp : 1438148173
     */

    private String result;
    private String cmdType;
    private String code;
    private String msg;
    private int    timestamp;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
