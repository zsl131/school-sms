package com.zslin;

import com.zslin.cache.CacheTools;
import com.zslin.sms.tools.FileMD5Tools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/14 17:31.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("zsl")
public class MD5Test {

    @Autowired
    private CacheTools cacheTools;

    @Test
    public void test() throws Exception {
        System.out.println("");
        long l1 = System.currentTimeMillis();
//        FileInputStream fileInputStream = new FileInputStream();
        String md5 = FileMD5Tools.fileMD5(new File("D:/temp/app1.jar"));
        System.out.println(md5);
        long l2 = System.currentTimeMillis();
        System.out.println((l2-l1));
    }

    @Test
    public void fileTest2() throws Exception {
        FileInputStream fis = new FileInputStream(new File("D:/temp/add-stu-template.xls"));
        FileChannel channel = null;
        channel = fis.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)channel.size());
        while((channel.read(byteBuffer)) > 0){
        }
        String str = byteBuffer.toString();
        byte [] bytes = byteBuffer.array();
        System.out.println(str+"=========="+bytes.length);
        String newStr = new String(bytes);
        byte [] newByte = newStr.getBytes();
        System.out.println(newByte.length);
    }

    @Test
    public void fileTest() throws Exception {
        FileInputStream fileInputStream = new FileInputStream(new File("D:/temp/add-stu-template.xls"));
        int len = 0;
        byte [] bytes = new byte[fileInputStream.available()];
        FileOutputStream fos = new FileOutputStream(new File("D:/temp/aaa.xls"));
        while((len=fileInputStream.read(bytes))!=-1) {
//            fos.write(bytes, 0, len);
        }
        StringBuffer sb = new StringBuffer();
        for(byte b : bytes) {
            sb.append(b);
        }
        System.out.println("sb:"+sb.toString());
        cacheTools.putKey("myFile", bytes, 10 * 60);
        System.out.println("old:"+bytes.length);
//        System.out.println(str);

        String sbStr = sb.toString();
        byte [] newByte = new byte[sbStr.length()];

        System.out.println("newByte:"+newByte.length+"=====strLen:"+sbStr.getBytes().length);

        for(int i=0; i<sbStr.length(); i++) {
            char c = sbStr.charAt(i);
//            newByte[i] = charToByte(c);
            newByte[i] = (byte) c;
        }

        byte [] cacheByte = ((String) cacheTools.getKey("myFile")).getBytes();
        System.out.println("=======cache:"+cacheByte.length);

        System.out.println("new:"+newByte.length);
        fos.write(newByte);
        fos.flush();
        fos.close();
    }

    public byte[] charToByte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);
        b[1] = (byte) (c & 0xFF);
        return b;
    }
}
