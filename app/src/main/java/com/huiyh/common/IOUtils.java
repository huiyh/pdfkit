package com.huiyh.common;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by huiyh on 2017/2/7.
 */
public class IOUtils {

    public static void close(Closeable closeable) {
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
