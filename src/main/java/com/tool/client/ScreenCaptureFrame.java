package com.tool.client;

import cn.hutool.core.util.StrUtil;
import com.sun.awt.AWTUtilities;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import com.tool.common.ImageUtil;
import com.tool.common.ThreadPoolManager;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component("screenCaptureFrame")
public class ScreenCaptureFrame extends CommonFrame {

    private static WinUser.HHOOK hhk;
    final static User32 lib = User32.INSTANCE;
    private final List<Integer> activeKey = new LinkedList<>();

    JButton button;

    @Override
    public void initFrame() {
        super.initFrame();
        this.setTitle("截图助手");
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        JPanel mainPanel = new JPanel();
        mainPanel.setSize(this.getSize());
        mainPanel.setLayout(new BorderLayout());
        // top
        JPanel topPanel = new JPanel();
        topPanel.setSize(this.getWidth(), 200);
        JTextArea useTip = new JTextArea(6,45);
        JLabel useTipLabel = new JLabel("使用说明");
        useTip.setText("在截图助手开启后，点击截图按钮或者使用快捷键Alt+X开启截图(暂不支持自定义快捷键..orz)" +
                StrUtil.CRLF + "在截图状态下，可以用鼠标左键划定最终截图区域" +
                StrUtil.CRLF + "双击鼠标左键确定截图，鼠标右键取消截图" +
                StrUtil.CRLF + "所截图片会在新窗口打开，可以右键图片进行置顶/取消置顶/关闭操作" +
                StrUtil.CRLF + "同时该图片会自动复制到剪切板~");
        topPanel.add(useTipLabel);
        topPanel.add(useTip);
        useTip.setEditable(false);
        // center
        JPanel centerPanel = new JPanel();
        button = new JButton("截图");
        centerPanel.add(button);
        mainPanel.add(BorderLayout.NORTH, topPanel);
        mainPanel.add(BorderLayout.CENTER, centerPanel);
        this.getContentPane().add(mainPanel);
        //鼠标点击按钮，new 一个ScreenFrame，设置可见，
        button.addActionListener(e -> activeCat());
        ThreadPoolManager.addBaseTask(this::startHotKey);
        setVisible(true);
    }

    private void startHotKey() {
        WinDef.HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);

        // 按下
        // 抬起
        WinUser.LowLevelKeyboardProc keyBroadHook = (nCode, wParam, info) -> {
            if (!this.isVisible()) {
                Thread.interrupted();
                return lib.CallNextHookEx(hhk, nCode, wParam, null);
            }
            if (User32.WM_SYSKEYDOWN == wParam.intValue()) {
                // 按下
                if (!activeKey.contains(info.vkCode)) {
                    activeKey.add(info.vkCode);
                    if (activeKey.contains(User32.VK_LMENU) && activeKey.contains(KeyEvent.VK_X)) {
                        activeCat();
                    }
                }
            }
            if (User32.WM_SYSKEYUP == wParam.intValue()) {
                // 抬起
                activeKey.remove(new Integer(info.vkCode));
            }
            return lib.CallNextHookEx(hhk, nCode, wParam, null);
        };
        hhk = lib.SetWindowsHookEx(User32.WH_KEYBOARD_LL, keyBroadHook, hMod, 0);
        WinUser.MSG msg = new WinUser.MSG ();
        while (lib.GetMessage(msg, null, 0, 0) != 0) {
        }
        lib.UnhookWindowsHookEx(hhk);
    }

    private void activeCat() {
        ScreenFrame sf = new ScreenFrame();
        sf.setAlwaysOnTop(true);
        sf.setUndecorated(true);
        AWTUtilities.setWindowOpaque(sf, false);
        sf.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        sf.setVisible(true);
    }
}

class ScreenFrame extends JFrame {
    private static final long serialVersionUID = 2L;
    /*
     * 创建一个全屏的窗口，将全屏的图像放在JFrame的窗口上，以供来截屏。
     */

    GraphicsDevice graphDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    DisplayMode disMode = graphDevice.getDisplayMode();
    Dimension di = Toolkit.getDefaultToolkit().getScreenSize();
    int width = disMode.getWidth();
    int height = disMode.getHeight();
    ScreenFrame() {
        //设置大小，即全屏
        setSize(width, height);
        //返回此窗体的 contentPane对象
        getContentPane().add(new DrawRect());
    }

    class DrawRect extends JPanel implements MouseMotionListener, MouseListener {
        private static final long serialVersionUID = 3L;
        /*
         * 将全屏的图像放在JPanel 上， 可以通过new DrawRect来获得JPanel，并且JPanel上有全屏图像
         */
        int sx, sy, ex, ey;
        double factor = disMode.getWidth() / di.getWidth();
        int count = 1;
        File file = null;
        BufferedImage image, getImage;
        boolean flag = true;

        public DrawRect() {
            try {//获取全屏图像数据，返回给image
                Robot robot = new Robot();
                image = robot.createScreenCapture(new Rectangle(0, 0, width, height));
            } catch (Exception e) {
                throw new RuntimeException("截图失败");
            }
            //添加 鼠标活动事件
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        //重写paintComponent，通过repaint 显示出来截屏的范围
        public void paintComponent(Graphics g) {
            // 显示的时候需要按照缩放后的分辨率来显示
            g.drawImage(image, 0, 0, di.width, di.height, this);
            g.setColor(Color.RED);
            if (sx < ex && sy < ey)//右下角
                g.drawRect(sx, sy, ex - sx, ey - sy);
            else                 //左上角
                g.drawRect(ex, ey, sx - ex, sy - ey);
        }

        //以下都是鼠标事件
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3)//右键退出程序
                dispose();
            else if (e.getClickCount() == 2)   //双击截屏
            {
                try {
                    cutScreens();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        //自定义截屏函数
        private void cutScreens() throws Exception {
            Robot ro = new Robot();
            if (sx < ex && sy < ey)//右下角
                getImage = ro.createScreenCapture(new Rectangle(culIndex(sx + 2), culIndex(sy + 2), culIndex(ex) - culIndex(sx) - 3, culIndex(ey) - culIndex(sy) - 3));
            else                  //左上角
                getImage = ro.createScreenCapture(new Rectangle(culIndex(ex + 2), culIndex(ey + 2), culIndex(sx) - culIndex(ex) - 3, culIndex(sy) - culIndex(ey) - 3));
            ImageUtil.setClipboardImage(new JFrame(), getImage);
            new ShowImageFrame(getImage).repaint();
            dispose();
        }

        public void mousePressed(MouseEvent e) {
            if (flag) {
                sx = e.getX();
                sy = e.getY();
            }
        }

        public void mouseReleased(MouseEvent e) {
            flag = false;
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        //鼠标移动中，通过repaint 画出要截屏的范围
        public void mouseDragged(MouseEvent e) {
            ex = e.getX();
            ey = e.getY();
            repaint();
        }

        public void mouseMoved(MouseEvent e) {
        }

        private int culIndex(int val) {
            return (int) (val * factor);
        }
    }
}

class ShowImageFrame extends JFrame {

    static Point origin = new Point();

    ShowImageFrame(BufferedImage image) {
        this.setIconImage(new ImageIcon(Objects.requireNonNull(CommonFrame.class.getResource("k7.png"))).getImage());
        this.setSize(image.getWidth(), image.getHeight());
        this.getContentPane().add(new JPanel().add(new JLabel(new ImageIcon(image))));
        this.setDefaultCloseOperation(HIDE_ON_CLOSE);
        this.setUndecorated(true);
        AWTUtilities.setWindowOpaque(this, false);
        this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        this.setVisible(true);


        this.addMouseListener(new MouseAdapter() {
            // 按下（mousePressed 不是点击，而是鼠标被按下没有抬起）
            public void mousePressed(MouseEvent e) {
                // 当鼠标按下的时候获得窗口当前的位置
                origin.x = e.getX();
                origin.y = e.getY();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = getLocation();
                setLocation(p.x + e.getX() - origin.x, p.y + e.getY() - origin.y);
            }
        });

        JFrame temp = this;
        final JPopupMenu jp = new JPopupMenu();
        JMenuItem holdTop = new JMenuItem("置顶");
        holdTop.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    if (holdTop.getText().length() == 2) {
                        temp.setAlwaysOnTop(true);
                        holdTop.setText("取消置顶");
                    } else {
                        temp.setAlwaysOnTop(false);
                        holdTop.setText("置顶");
                    }
                }
            }
        });
        JMenuItem close = new JMenuItem("关闭");
        close.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                if (e.getButton() == MouseEvent.BUTTON1)
                {
                    temp.dispose();
                }
            }
        });
        jp.add(holdTop);
        jp.add(close);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3)
                {
                    // 弹出菜单
                    jp.show(temp, e.getX(), e.getY());
                }
            }
        });
    }

}
