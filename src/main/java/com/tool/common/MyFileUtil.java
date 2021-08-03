package com.tool.common;

import cn.hutool.core.io.IORuntimeException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MyFileUtil {

    public static boolean clean(File directory) throws IORuntimeException {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (null != files) {
                File[] var3 = files;
                int var4 = files.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    File childFile = var3[var5];
                    boolean isOk = del(childFile);
                    if (!isOk) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return true;
        }
    }

    public static boolean del(File file) throws IORuntimeException {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                boolean isOk = clean(file);
                if (!isOk) {
                    return false;
                }
            }
            try {
                Files.delete(file.toPath());
            } catch (IOException var2) {
            }
        }

        return true;
    }
}
