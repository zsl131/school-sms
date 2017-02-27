package com.zslin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/22 16:48.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class OtherTest {

    private String words = "@163.com";

    @Test
    public void test() throws Exception {
        String path = "F:\\";
        File file = new File(path);
        readDir(file);
    }

    private void readDir(File file) {
        if(file==null) {return;}
        if(file.isFile()) {
            readFile(file);
        } else {
            File [] files = file.listFiles();
            if(files==null || files.length<=0) {return;}
            for(File f : files) {
                readDir(f);
            }
        }
    }

    private void readFile(File f) {
        try {
            String name = f.getName();
            if(!type(name)) {return;}
            if(name.indexOf(words)>=0) {
                System.out.println(f.getAbsolutePath()+"=="+name);
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
            String line = null;
            while((line=br.readLine())!=null) {
                if(line.indexOf(words)>=0) {
                    System.out.println(f.getAbsolutePath()+"==>"+line);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean type(String name) {
        name = name.toLowerCase();
        if(name.indexOf(".")>=0 && (name.endsWith(".md") || name.endsWith(".txt") || name.endsWith(".doc") || name.endsWith(".docx"))) {return true;}
        return false;
    }
}
