package com.position4g.thread;

import com.position4g.eventbusmessage.CodeMessage;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import static com.position4g.service.SocketService.socket2;
import static com.position4g.thread.ParamThread1.inputStream1;

/**
 * @createAuthor zfb
 * @createTime 2017/3/23${Time}
 * @describe ${TODO}
 */

public class ParamThread2 implements Runnable{
    Socket mSocket;
    String unix;
    StringBuffer sb=new StringBuffer();
    public static InputStream inputStream2=null;
    public static OutputStream os2=null;


    public ParamThread2(Socket socket){
        mSocket =socket;
    }

    @Override
    public void run() {
        int temp;
        byte bf[] = new byte[1024*10];
        unix=String.valueOf(System.currentTimeMillis());
        try {
            if(mSocket !=null){
                os2 = mSocket.getOutputStream();
                String reply2 = "Welecome to tcp server!^M";
                PrintWriter pw2 = new PrintWriter(os2);
                pw2.write(reply2);
                pw2.flush();
                inputStream2= mSocket.getInputStream();
                if(inputStream2.read(bf)>0){
                    reply2 = "{\"code\":54,\"msg\":\"Device registered successfully.\",\"stats\":4129,\"timestamp\":"+unix+"}";
                    pw2.write(reply2);
                    pw2.flush();
                    while (true){
                        if(mSocket !=null){
                            inputStream2= mSocket.getInputStream();
                            if((temp = inputStream2.read(bf)) > 0) {
                                sb=new StringBuffer();
                                sb.append(new String(bf,0,temp));
                                EventBus.getDefault().post(new CodeMessage(sb.toString()));

                            }else {
                                if (socket2 != null) {

                                    if(os2!=null){
                                        os2=null;
                                    }
//                                    if(os1!=null){
//                                        os1=null;
//                                    }
                                    inputStream2=null;

                                    inputStream1=null;

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
