package com.position4g.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;

import com.position4g.model.DevBean;
import com.position4g.thread.ParamThread1;
import com.position4g.thread.ParamThread2;
import com.position4g.utils.DataUtils;
import com.position4g.utils.ThreadUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static com.position4g.thread.ParamThread1.os1;
import static com.position4g.thread.ParamThread2.os2;

/**
 * @createAuthor zfb
 * @createTime 2017/3/22${Time}
 * @describe ${TODO}
 */

public class SocketService extends Service {
    public static ServerSocket mSocket1 = null;
    public static ServerSocket mSocket2 = null;
    public static Socket       socket1  = null;
    public static Socket       socket2  = null;

    ParamThread1 thread1;
    ParamThread2 thread2;

    Handler mHandler = new Handler();
    Runnable timeRunnable;
    static String unix;
    PowerManager          pm;
    PowerManager.WakeLock wl;
    Runnable              run1;
    Runnable              run2;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "keepsocket");
        wl.acquire();
        init();

        timeRunnable = new Runnable() {
            @Override
            public void run() {

                        unix = String.valueOf(System.currentTimeMillis());
                        final String reply = "{\"stats\":4128, \"timestamp\":" + unix + "}";



                ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (os1 != null) {
                            PrintWriter pw1 = new PrintWriter(os1);
                            pw1.write(reply);
                            pw1.flush();
                        }
                        if (os2 != null) {
                            PrintWriter pw2 = new PrintWriter(os2);
                            pw2.write(reply);
                            pw2.flush();
                        }
                    }
                });

                mHandler.postDelayed(this, 1000 * 5);
            }
        };

                mHandler.postDelayed(timeRunnable, 1000 * 5);
        return super.onStartCommand(intent, flags, startId);
    }


    public void init() {
//        ThreadUtils.getThreadPoolProxy().removeTask(run1);
//
//        ThreadUtils.getThreadPoolProxy().removeTask(run2);
        run1 = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        mSocket1=null;
                        mSocket1 = new ServerSocket(16385);
                        socket1 = mSocket1.accept();
                        thread1 = new ParamThread1(socket1);
                        ThreadUtils.getThreadPoolProxy().execute(thread1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        run2 = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        mSocket2=null;
                        mSocket2 = new ServerSocket(16387);
                        socket2 = mSocket2.accept();
                        thread2 = new ParamThread2(socket2);
                        ThreadUtils.getThreadPoolProxy().execute(thread2);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        ThreadUtils.getThreadPoolProxy().execute(run1);

        ThreadUtils.getThreadPoolProxy().execute(run2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        wl.release();
        try {
            if (socket1 != null) {
                socket1.close();
                socket1 = null;
            }
            if (socket2 != null) {
                socket2.close();
                socket2 = null;
            }
            if (mSocket1 != null) {
                mSocket1.close();
                mSocket1 = null;
            }
            if (mSocket2 != null) {
                mSocket2.close();
                mSocket2 = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //主页设备页面开启定位
    public static void openLocation(final String deplex) {
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (os1 != null) {
                        os1.flush();
                        PrintWriter pw1 = new PrintWriter(os1);
                        String reply = DataUtils.getOpenLocationJson(deplex);
                        pw1.write(reply);
                        pw1.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //主页设备页面关闭定位
    public static void closeLocation(final String deplex) {
        unix = String.valueOf(System.currentTimeMillis());
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                String reply = "{\"code\": 4609,\"DeplexMode\": \"" + deplex + "\", \"timestamp\":" + unix + ",\"type\":4192}";
                try {
                    if (os1 != null) {
                        os1.flush();
                        PrintWriter pw1 = new PrintWriter(os1);
                        pw1.write(reply);
                        pw1.flush();
                    }
                    if (os2 != null) {
                        os2.flush();
                        PrintWriter pw2 = new PrintWriter(os2);
                        pw2.write(reply);
                        pw2.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //切换电信/联通
    public static void changeLocation(final int type) {
        unix = String.valueOf(System.currentTimeMillis());
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                String reply = "{\"code\":4615, \"data\" : {\"deviceType\" : " + type + ", \"timestamp\":" + unix + ",\"type\":4193}}";
                try {
                    if (os1 != null) {
                        os1.flush();
                        PrintWriter pw1 = new PrintWriter(os1);
                        pw1.write(reply);
                        pw1.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //参数管理页面修改设备参数
    public static void updateDev(DevBean bean) {
        unix = String.valueOf(System.currentTimeMillis());
        String B1;
        String B3;
        String B38;
        String B39;
        String B40;


        int bbudianxin = bean.bbu_dianxin;
        int bbuliantong = bean.bbu_liantong;
        int bbuyidong = bean.bbu_yidong;

        int B1dianxin = bbudianxin == 6 ? 1 : 0;
        int B1liantong = bbuliantong == 6 ? 1 : 0;

        int B3dianxin = bbudianxin == 7 ? 1 : 0;
        int B3liantong = bbudianxin == 7 ? 1 : 0;

        int B38yidong = bbuyidong == 1 ? 1 : 0;
        int B39yidong = bbuyidong == 2 ? 1 : 0;
        int B40yidong = bbuyidong == 3 ? 1 : 0;

        B1 = "{ \"cellNumber\":6,\"freqList\": \"" + bean.freq_liantong_b1 + "\", \"CtFreqList\": \"" + bean.freq_dianxin_b1 + "\",\"isCULocaiton\":  " + B1liantong + ", \"isCTLocaiton\": " + B1dianxin + "},";

        B3 = "{ \"cellNumber\":7,\"freqList\": \"" + bean.freq_liantong_b3 + "\", \"CtFreqList\": \"" + bean.freq_dianxin_b3 + "\",\"isCULocaiton\":  " + B3liantong + ", \"isCTLocaiton\": " + B3dianxin + "},";

        B38 = "{ \"cellNumber\":1 ,\"freqList\":\"" + bean.freq_yidong_b38 + "\", \"gpsDelay\":" + bean.delay_b38 + ", \"isCMLocaiton\": " + B38yidong + "},";

        B39 = "{ \"cellNumber\":2 ,\"freqList\":\"" + bean.freq_yidong_b39 + "\", \"gpsDelay\":" + bean.delay_b39 + ", \"isCMLocaiton\": " + B39yidong + "},";

        B40 = "{ \"cellNumber\":3 ,\"freqList\":\"" + bean.freq_yidong_b40 + "\", \"gpsDelay\":" + bean.delay_b40 + ", \"isCMLocaiton\": " + B40yidong + "}";

        final String reply = "{\"code\" : 4610, \"data\" : [" + B1 + B3 + B38 + B39 + B40 + "], \"timestamp\":" + unix + ",\"type\":4193}";


        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    if (os1 != null) {
                        os1.flush();
                        PrintWriter pw1 = new PrintWriter(os1);
                        pw1.write(reply);
                        pw1.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void updateSync(final int cellNum, final int syncMode) {
        unix = String.valueOf(System.currentTimeMillis());
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                String reply = "{\"code\": 4617,\"data\":{ \"cellNumber\":" + cellNum + ",\"syncMode\":" + syncMode + " },\"timestamp\":" + unix + ",\"type\":4193}";
                try {
                    if (os1 != null) {
                        os1.flush();
                        PrintWriter pw1 = new PrintWriter(os1);
                        pw1.write(reply);
                        pw1.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //更改bbu输出功率
    public static void updateBBUOutput(final int cellNum, final int value) {
        unix = String.valueOf(System.currentTimeMillis());
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                String reply = "{\"code\": 4611,\"data\":{ \"cellNumber\":" + cellNum + ",\"value\":" + value + " },\"timestamp\":" + unix + ",\"type\":4193}";
                try {
                    if (os1 != null) {
                        os1.flush();
                        PrintWriter pw1 = new PrintWriter(os1);
                        pw1.write(reply);
                        pw1.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //打开扫描模式
    public static void openScan(final int second) {
        unix = String.valueOf(System.currentTimeMillis());
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                String reply = "{\"code\":4618,\"data\":{\"scanPeriod\" : " + second + "}, \"timestamp\":" + unix + ",\"type\":4192}";
                try {
                    if (os1 != null) {
                        os1.flush();
                        PrintWriter pw1 = new PrintWriter(os1);
                        pw1.write(reply);
                        pw1.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //关闭扫描模式
    public static void closeScan() {
        unix = String.valueOf(System.currentTimeMillis());
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                String reply = "{\"code\" : 4619, \"timestamp\":" + unix + ",\"type\":4192}";
                try {
                    if (os1 != null) {
                        os1.flush();
                        PrintWriter pw1 = new PrintWriter(os1);
                        pw1.write(reply);
                        pw1.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //打开基带板输出,一次只能带一个参数(关闭省电模式)
    public static void OpenBBU(final int cellNumber) {
        unix = String.valueOf(System.currentTimeMillis());
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                String reply = "{\"code\" : 4612,\"data\":{\"cellNumber\":" + cellNumber + "}, \"timestamp\":" + unix + ",\"type\":4192}";
                try {
                    if (os1 != null) {
                        os1.flush();
                        PrintWriter pw1 = new PrintWriter(os1);
                        pw1.write(reply);
                        pw1.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //关闭基带板输出,一次只能带一个参数(打开省电模式)
    public static void CloseBBU(final int cellNumber) {
        unix = String.valueOf(System.currentTimeMillis());
        ThreadUtils.getThreadPoolProxy().execute(new Runnable() {
            @Override
            public void run() {
                String reply = "{\"code\" : 4613,\"data\":{\"cellNumber\":" + cellNumber + "}, \"timestamp\":" + unix + ",\"type\":4192}";
                try {
                    if (os1 != null) {
                        os1.flush();
                        PrintWriter pw1 = new PrintWriter(os1);
                        pw1.write(reply);
                        pw1.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
