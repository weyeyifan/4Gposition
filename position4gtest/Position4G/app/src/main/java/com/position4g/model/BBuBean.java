package com.position4g.model;

/**
 * @createAuthor zfb
 * @createTime 2017/3/28${Time}
 * @describe ${TODO}
 */

public class BBuBean {
    /**
     * bbu1 : 1
     * bbu2 : 2
     * bbu6 : 1
     * bbu7 : 2
     */

    private BbuStatusBean bbuStatus;
    /**
     * bbuStatus : {"bbu1":1,"bbu2":2,"bbu6":1,"bbu7":2}
     * TddLocationEnable : 1
     * FddLocationEnable : 0
     */

    private int           TddLocationEnable;
    private int           FddLocationEnable;

    public BbuStatusBean getBbuStatus() {
        return bbuStatus;
    }

    public void setBbuStatus(BbuStatusBean bbuStatus) {
        this.bbuStatus = bbuStatus;
    }

    public int getTddLocationEnable() {
        return TddLocationEnable;
    }

    public void setTddLocationEnable(int TddLocationEnable) {
        this.TddLocationEnable = TddLocationEnable;
    }

    public int getFddLocationEnable() {
        return FddLocationEnable;
    }

    public void setFddLocationEnable(int FddLocationEnable) {
        this.FddLocationEnable = FddLocationEnable;
    }

    public static class BbuStatusBean {
        private String bbu1;
        private String bbu2;
        private String bbu3;
        private String bbu6;
        private String bbu7;

        public String getBbu1() {
            return bbu1;
        }

        public void setBbu1(String bbu1) {
            this.bbu1 = bbu1;
        }

        public String getBbu2() {
            return bbu2;
        }

        public void setBbu2(String bbu2) {
            this.bbu2 = bbu2;
        }

        public String getBbu3() {
            return bbu3;
        }

        public void setBbu3(String bbu2) {
            this.bbu3 = bbu3;
        }

        public String getBbu6() {
            return bbu6;
        }

        public void setBbu6(String bbu6) {
            this.bbu6 = bbu6;
        }

        public String getBbu7() {
            return bbu7;
        }

        public void setBbu7(String bbu7) {
            this.bbu7 = bbu7;
        }
    }
}
