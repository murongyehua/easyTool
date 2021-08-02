package com.tool.common;

import java.io.*;

public class ExecCMD {

    public static void exec(String stam){
        BufferedReader br = null;
        try {
            File file = new File("C:\\easyTool");
            File tmpFile = new File("C:\\easyTool\\temp.tmp");//新建一个用来存储结果的缓存文件
            if (!file.exists()){
                file.mkdirs();
            }
            if(!tmpFile.exists()) {
                tmpFile.createNewFile();
            }
            ProcessBuilder pb = new ProcessBuilder().command("cmd.exe", "/c", stam).inheritIO();
            pb.redirectErrorStream(true);//这里是把控制台中的红字变成了黑字，用通常的方法其实获取不到，控制台的结果是pb.start()方法内部输出的。
            pb.redirectOutput(tmpFile);//把执行结果输出。
            pb.start().waitFor();//等待语句执行完成，否则可能会读不到结果。
            InputStream in = new FileInputStream(tmpFile);
            br= new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
            br.close();
            br = null;
            tmpFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
