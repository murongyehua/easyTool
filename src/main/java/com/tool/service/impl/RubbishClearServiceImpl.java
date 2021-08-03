package com.tool.service.impl;

import cn.hutool.core.io.FileUtil;
import com.tool.common.ExecCMD;
import com.tool.common.MyFileUtil;
import com.tool.service.RubbishClearService;
import org.springframework.stereotype.Service;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FilenameFilter;

@Service
public class RubbishClearServiceImpl implements RubbishClearService {


    @Override
    public void baseClear() {
        MyFileUtil.clean(FileUtil.getTmpDir());
    }

    @Override
    public void diyClear() {
        ExecCMD.exec("cleanmgr");
    }

    @Override
    public void clearForTencent() {
        if (FileUtil.exist(FileSystemView.getFileSystemView().getDefaultDirectory() + "\\WeChat Files")) {
            File[] wechatDirNames = FileUtil.file(FileSystemView.getFileSystemView().getDefaultDirectory() + "\\WeChat Files").listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith("wxid");
                }
            });
            assert wechatDirNames != null;
            for (File file : wechatDirNames) {
                MyFileUtil.clean(file);
            }
        }

        if (FileUtil.exist(FileSystemView.getFileSystemView().getDefaultDirectory() + "\\Tencent Files")) {
            File[] qqDirNames = FileUtil.file(FileSystemView.getFileSystemView().getDefaultDirectory() + "\\Tencent Files").listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return !name.startsWith("All");
                }
            });
            assert qqDirNames != null;
            for (File file : qqDirNames) {
                MyFileUtil.clean(file);
            }
        }

    }

    @Override
    public int canBaseClearSize() {
        return (int) (FileUtil.size(FileUtil.getTmpDir()) / (1024*1024));
    }

    @Override
    public int canTencentClearSize() {
        long wechatSize = 0;
        long qqSize = 0;
        // 微信
        if (FileUtil.exist(FileSystemView.getFileSystemView().getDefaultDirectory() + "\\WeChat Files")) {
            // 获取所有用户文件夹
            File[] wechatDirNames = FileUtil.file(FileSystemView.getFileSystemView().getDefaultDirectory() + "\\WeChat Files").listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.startsWith("wxid");
                }
            });
            // 遍历获取大小
            assert wechatDirNames != null;
            for (File file : wechatDirNames) {
                wechatSize += FileUtil.size(file);
            }
        }
        // QQ
        if (FileUtil.exist(FileSystemView.getFileSystemView().getDefaultDirectory() + "\\Tencent Files")) {
            File[] qqDirNames = FileUtil.file(FileSystemView.getFileSystemView().getDefaultDirectory() + "\\Tencent Files").listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return !name.startsWith("All");
                }
            });
            assert qqDirNames != null;
            for (File file : qqDirNames) {
                qqSize += FileUtil.size(file);
            }
        }
        return (int) ((wechatSize + qqSize) / (1024*1024));
    }
}
