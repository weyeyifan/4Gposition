/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.position4g;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;

import java.util.Stack;

//全局application
public class MyApp extends Application {

    private static int     mainThreadId;//主线程id
    private static Handler             mHandler;//线程通信工具
    //存放activity的桟
    private static Stack<Activity> activityStack;

    public String path;

    public         boolean           onCancellation = false;
    public         boolean           isNeedToUpdate = false;


    //获取applicationContext的单例
    private static MyApp app;

    public static MyApp getInstance() {
        if (app == null) {
            app = new MyApp();
        }
        return app;
    }

    @SuppressWarnings("unused")
    @Override
    public void onCreate() {
        app = this;
        mainThreadId=android.os.Process.myTid();//取得当前的线程的id
        mHandler=new Handler();
        super.onCreate();

    }

    public static Handler getHandler() {
        return mHandler;
    }
    //获得主线程id
    public static int getMainThreadId() {
        return mainThreadId;
    }
}