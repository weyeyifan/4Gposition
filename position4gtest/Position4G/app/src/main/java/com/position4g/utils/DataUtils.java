package com.position4g.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.position4g.MyApp;
import com.position4g.R;
import com.position4g.db.MemberDao;
import com.position4g.model.BBuBean;
import com.position4g.model.CodeBean;
import com.position4g.model.DevTypeBean;
import com.position4g.model.MemberBean;
import com.position4g.model.PaTemperBean;
import com.position4g.model.ResultBean;
import com.position4g.model.TargetBean;
import com.position4g.model.WarnBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @createAuthor zfb
 * @createTime 2017/3/22${Time}
 * @describe ${TODO}
 */

public class DataUtils {
    static String unix;

    /**
     * 截取第一段有效json的长度
     *
     * @param data
     * @return
     */
    public static int getLen(String data) {
        int temp = 0;
        int i = 0;
        int j = 0;
        while (temp < data.length()) {
            if (data.charAt(temp) == '{') {
                i++;
            } else if (data.charAt(temp) == '}') {
                j++;
                if (j == i) {
                    temp++;
                    break;
                }

            }
            temp++;
        }
        return temp;
    }

    /**
     * 获取目标记录的javabean列表
     *
     * @param data
     * @return
     */
    public static ArrayList<TargetBean> getTargetList(String data) {
        int tempLen;
        String tempData = data;
        String tempJson;
        ArrayList<TargetBean> list = new ArrayList<>();
        Gson gson = new Gson();
        TargetBean bean;
        while (tempData.length() > 1) {
            tempLen = getLen(tempData);
            tempJson = tempData.substring(0, tempLen);
            tempData = tempData.substring(tempLen, tempData.length());
            try {
                bean = gson.fromJson(tempJson, TargetBean.class);
                if (bean.getImsi() != null && bean.getDelay() != null) {
                    list.add(bean);
                }

            } catch (JsonSyntaxException e) {
                continue;
            }
        }
        return list;
    }

    /**
     * 获取帧码记录的javabean列表
     *
     * @param data
     * @return
     */
    public static ArrayList<CodeBean> getCodeList(String data) {
        int tempLen;
        String tempData = data;
        String tempJson;
        ArrayList<CodeBean> list = new ArrayList<>();
        Gson gson = new Gson();
        CodeBean bean;
        int j = 0;
        while (tempData.length() > 1) {
            tempLen = getLen(tempData);
            for (int i = 0; i < tempLen; i++) {
                if (!tempData.substring(i, i + 1).equals("{")) {
                    i++;
                } else {
                    j = i;
                    break;
                }
            }
            tempJson = tempData.substring(j, tempLen);
            j=0;
            tempData = tempData.substring(tempLen, tempData.length());
            try {
                bean = gson.fromJson(tempJson, CodeBean.class);
                if (bean.getUptime() > 0 && bean.getImei() != null) {
                    list.add(bean);
                }

            } catch (JsonSyntaxException e) {
                continue;
            }
        }
        //        bean=gson.fromJson(data,CodeBean.class);
        //        list.add(bean);
        return list;
    }

    /**
     * 获取bbubean的javabean列表
     *
     * @param data
     * @return
     */
    public static ArrayList<BBuBean> getBBUBean(String data) {
        int tempLen;
        String tempData = data;
        String tempJson;
        ArrayList<BBuBean> list = new ArrayList<>();
        Gson gson = new Gson();
        BBuBean bean;
        while (tempData.length() > 1) {
            tempLen = getLen(tempData);
            tempJson = tempData.substring(0, tempLen);
            tempData = tempData.substring(tempLen, tempData.length());
            try {
                bean = gson.fromJson(tempJson, BBuBean.class);
                if (bean.getBbuStatus() != null) {
                    list.add(bean);
                }

            } catch (JsonSyntaxException e) {
                continue;
            }
        }
        return list;
    }

    /**
     * 获取bbubean的javabean列表
     *
     * @param data
     * @return
     */
    public static ArrayList<PaTemperBean> getPaTemBean(String data) {
        int tempLen;
        String tempData = data;
        String tempJson;
        ArrayList<PaTemperBean> list = new ArrayList<>();
        Gson gson = new Gson();
        PaTemperBean bean;
        while (tempData.length() > 1) {
            tempLen = getLen(tempData);
            tempJson = tempData.substring(0, tempLen);
            tempData = tempData.substring(tempLen, tempData.length());
            try {
                bean = gson.fromJson(tempJson, PaTemperBean.class);
                if (bean.getSyncInfo() != null) {
                    list.add(bean);
                }

            } catch (JsonSyntaxException e) {
                continue;
            }
        }
        return list;
    }

    /**
     * 获取deviceType的javabean列表
     *
     * @param data
     * @return
     */
    public static ArrayList<DevTypeBean> getDeviceTypeBean(String data) {
        int tempLen;
        String tempData = data;
        String tempJson;
        ArrayList<DevTypeBean> list = new ArrayList<>();
        Gson gson = new Gson();
        DevTypeBean bean;
        while (tempData.length() > 1) {
            tempLen = getLen(tempData);
            tempJson = tempData.substring(0, tempLen);
            tempData = tempData.substring(tempLen, tempData.length());
            try {
                bean = gson.fromJson(tempJson, DevTypeBean.class);
                if (bean.getDevNumber() != null && bean.getDevName() != null) {
                    list.add(bean);
                }

            } catch (JsonSyntaxException e) {
                continue;
            }
        }
        return list;
    }

    /**
     * 获取运营商
     */
    public static String getOpera(String imsi) {
        imsi = imsi.substring(0, 5);
        switch (imsi) {
            case "46000":

            case "46002":

            case "46007":

            case "41004":

                return MyApp.getInstance().getResources().getStringArray(R.array.operator)[0];

            case "46001":

            case "46006":

                return MyApp.getInstance().getResources().getStringArray(R.array.operator)[1];

            case "46011":

            case "46005":
                return MyApp.getInstance().getResources().getStringArray(R.array.operator)[2];

            default:

                return MyApp.getInstance().getResources().getStringArray(R.array.operator)[3];
        }
    }

    /**
     * 获取bbu编号
     */
    public static String getCellNumber(String bbu) {
        switch (bbu) {
            case "1":
                return "B38";
            case "2":
                return "B39";
            case "3":
                return "B40";
            case "6":
                return "B1";
            case "7":
                return "B3";
            default:
                return null;
        }
    }

    public static String getSyncState(int code) {
        switch (code) {
            case 0:
                return "未同步";
            case 1:
                return "初始同步失败";
            case 2:
                return "初始同步成功";
            case 3:
                return "同步失败";
            case 4:
                return "空口同步";
            case 5:
                return "同步失败";
            case 6:
                return "同步失败";
            case 7:
                return "GPS同步";
            case 8:
                return "初始串口失败";
            case 9:
                return "锁星失败";
            case 10:
                return "无GPS信号";
            case 11:
                return "GPS初始化";

            default:
                return "未同步";
        }

    }

    //获得入库人员的imsi集合
    public static List<String> getIMSIlib() {

        List<String> imsiList = new ArrayList<String>();
        MemberDao mDao = new MemberDao();
        List<MemberBean> list = mDao.queryAllLibMember();
        for (int i = 0; i < list.size(); i++) {
            imsiList.add(list.get(i).getImsi());
        }
        mDao.close();
        return imsiList;
    }

    //拼凑开启定位json字符串
    public static String getOpenLocationJson(String deplex) {
        unix = String.valueOf(System.currentTimeMillis());
        List<String> imsiList = getIMSIlib();
        StringBuffer bf = new StringBuffer();
        for (int i = 0; i < imsiList.size(); i++) {
            bf.append("{\"imsi\": ");
            bf.append(imsiList.get(i));
            bf.append("}");
            if (i != (imsiList.size() - 1)) {
                bf.append(" ,");
            }
        }
        String json = "{\"code\": 4608, \"DeplexMode\":\"" + deplex + "\"  ,\"imsiList\" :[" + bf.toString()
                + "],\"timestamp\":" + unix + ",\"type\":4192}";
        return json;
    }

    //获取告警信息
    public static ArrayList<WarnBean> getWarnBean(String data) {
        int tempLen;
        String tempData = data;
        String tempJson;
        ArrayList<WarnBean> list = new ArrayList<>();
        Gson gson = new Gson();
        WarnBean bean;
        while (tempData.length() > 1) {
            tempLen = getLen(tempData);
            tempJson = tempData.substring(0, tempLen);
            tempData = tempData.substring(tempLen, tempData.length());
            try {
                bean = gson.fromJson(tempJson, WarnBean.class);
                if (bean.getAlarmType() != 0) {
                    list.add(bean);
                }

            } catch (JsonSyntaxException e) {
                continue;
            }
        }
        return list;
    }

    //获取执行返回信息
    public static ArrayList<ResultBean> getResult(String data) {
        int tempLen;
        String tempData = data;
        String tempJson;
        ArrayList<ResultBean> list = new ArrayList<>();
        Gson gson = new Gson();
        ResultBean bean;
        while (tempData.length() > 1) {
            tempLen = getLen(tempData);
            tempJson = tempData.substring(0, tempLen);
            tempData = tempData.substring(tempLen, tempData.length());
            try {
                bean = gson.fromJson(tempJson, ResultBean.class);
                if (bean.getResult() != null) {
                    list.add(bean);
                }

            } catch (JsonSyntaxException e) {
                continue;
            }
        }
        return list;
    }
}
