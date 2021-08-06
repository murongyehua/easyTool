package com.tool.client;

public class Main {

    public static void main(String[] args) {
//        if (!CefAp) {
//            System.out.println("Startup initialization failed!");
//
//            return;
//        }

        // The simple example application is created as anonymous class and points
        // to Google as the very first loaded page. Windowed rendering mode is used by
        // default. If you want to test OSR mode set |useOsr| to true and recompile.
        // 这个简单的示例应用程序被创建为匿名类，并指向 Google 作为第一个加载的页面。默认情况下使用窗口渲染模式。如果要测试OSR模式，请将 |useOsr| 设置为 true 并重新编译。
        boolean useOsr = false;
        new MyBrowser("www.baidu.com", false, false);
    }

}
