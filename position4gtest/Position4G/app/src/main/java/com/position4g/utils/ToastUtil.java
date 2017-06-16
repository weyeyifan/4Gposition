package com.position4g.utils;

import android.widget.Toast;

import com.position4g.MyApp;

/**
 * @createAuthor zfb
 * @createTime 2016/9/23${Time}
 * @describe ${ToastUtil}
 * @updateAuthor $Author$
 * @updateDate 2016/9/23${Time}
 * @updateDescribe ${TODO}
 */

public class ToastUtil {

    private static Toast toast;

    public static void show(String content) {
        if (toast == null) {
            toast = Toast.makeText(MyApp.getInstance(), content, Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}