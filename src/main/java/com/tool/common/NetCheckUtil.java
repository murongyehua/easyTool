package com.tool.common;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NetCheckUtil {

    public static boolean check(String host, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port), 1000);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "连接服务器失败", "提示", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

}
