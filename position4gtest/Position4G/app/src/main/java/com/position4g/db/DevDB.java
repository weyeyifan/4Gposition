package com.position4g.db;

/**
 * @createAuthor zfb
 * @createTime 2017/3/31${Time}
 * @describe ${TODO}
 */

public interface DevDB {
    String DB_NAME = "device.db";
    int    version = 1;

    interface DBList {
        String DEV_TABLE_NAME      = "devlist";
        String ID                  = "id";
        String CITY                = "city";
        String FREQ_DIANXIN_B1     = "freq_dianxin_b1";
        String FREQ_DIANXIN_B3     = "freq_dianxin_b3";
        String BBU_DIANXIN         = "bbu_dianxin";
        String FREQ_LIANTONG_B1    = "freq_liantong_b1";
        String FREQ_LIANTONG_B3    = "freq_liantong_b3";
        String BBU_LIANTONG        = "bbu_liantong";
        String FREQ_YIDONG_B38     = "freq_yidong_b38";
        String FREQ_YIDONG_B39     = "freq_yidong_b39";
        String FREQ_YIDONG_B40     = "freq_yidong_b40";
        String DELAY_B38               = "delay_b38";
        String DELAY_B39               = "delay_b39";
        String DELAY_B40               = "delay_b40";
        String BBU_YIDONG          = "bbu_yidong";
        String CREATE_DEVTABLE_SQL = "create table " + DEV_TABLE_NAME + "(" + ID + " integer primary key autoincrement," + CITY + " text," + FREQ_DIANXIN_B1 + " text," + FREQ_DIANXIN_B3 + " text," + BBU_DIANXIN + " INTEGER," + FREQ_LIANTONG_B1 + " text," + FREQ_LIANTONG_B3 + " text," + BBU_LIANTONG + " text," + FREQ_YIDONG_B38 + " text," + FREQ_YIDONG_B39 + " text," + FREQ_YIDONG_B40 + " text," + DELAY_B38 + " text," + DELAY_B39 + " text,"+ DELAY_B40 + " text,"+ BBU_YIDONG + " INTEGER)";

    }
}
