package com.tool.client;

import com.melloware.jintellitype.JIntellitype;

public class Main {

    public static void main(String[] args) {
        JIntellitype.getInstance().addHotKeyListener(i -> {
            if (i == 88) {
                System.out.println("123");
            }
        });
        JIntellitype.getInstance().registerHotKey(88, JIntellitype.MOD_CONTROL, (int)'B');
        System.out.println("finish");
    }
}
