package com.position4g.thread;

import com.position4g.eventbusmessage.CodeMessage;
import com.position4g.eventbusmessage.DevSyncMessage;
import com.position4g.eventbusmessage.DeviceTypeMessage;
import com.position4g.eventbusmessage.LocationMessage;
import com.position4g.eventbusmessage.ResultMessage;
import com.position4g.eventbusmessage.TargetListMessage;
import com.position4g.eventbusmessage.WarnMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import static com.position4g.service.SocketService.socket1;
import static com.position4g.thread.ParamThread2.inputStream2;

/**
 * @createAuthor zfb
 * @createTime 2017/3/23${Time}
 * @describe ${TODO}
 */


public class ParamThread1 implements Runnable {
    public Socket mSocket;
    StringBuffer sb = new StringBuffer();
    String unix;
    public static OutputStream os1          = null;
    public static InputStream  inputStream1 = null;

    public ParamThread1(Socket socket) {
        mSocket = socket;
    }

    @Override
    public void run() {
        int temp;
        byte bf[] = new byte[10*1024];
        try {

            if(mSocket!=null){
                os1 = mSocket.getOutputStream();
                PrintWriter pw1 = new PrintWriter(os1);
                String reply1 = "Welecome to tcp server!^M";
                pw1.write(reply1);
                pw1.flush();
                inputStream1 = mSocket.getInputStream();
                if ((temp = inputStream1.read(bf)) > 0) {
                    sb = new StringBuffer();
                    sb.append(new String(bf, 0, temp));
                    EventBus.getDefault().post(new DeviceTypeMessage(sb.toString()));
                    unix = String.valueOf(System.currentTimeMillis());
                    reply1 = "{\"code\":54,\"msg\":\"Device registered successfully.\",\"stats\":4129,\"timestamp\":" + unix + "}";
                    pw1.write(reply1);
                    pw1.flush();
                    bf = new byte[10 * 1024];

                    while (true) {
                        if (mSocket != null) {
                            inputStream1 = mSocket.getInputStream();
                            if ((temp = inputStream1.read(bf)) > 0) {
                                sb = new StringBuffer();
                                sb.append(new String(bf, 0, temp));
                                EventBus.getDefault().post(new TargetListMessage(sb.toString()));
                                EventBus.getDefault().post(new DevSyncMessage(sb.toString()));
                                EventBus.getDefault().post(new DeviceTypeMessage(sb.toString()));
                                EventBus.getDefault().post(new LocationMessage(sb.toString()));
                                EventBus.getDefault().post(new WarnMessage(sb.toString()));
                                EventBus.getDefault().post(new ResultMessage(sb.toString()));
                                EventBus.getDefault().post(new CodeMessage(sb.toString()));
                            } else {
                                if (socket1 != null) {
                                    if(os1!=null){
                                        os1=null;
                                    }
                                    inputStream1=null;
                                    inputStream2=null;
                                    break;
                                }
                            }

                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}