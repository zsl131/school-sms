package com.zslin.sms.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/15 11:35.
 * 对文件的MD5处理工具
 */
public class FileMD5Tools {

    public static String fileMD5(File f) {
        DigestInputStream digestInputStream = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            InputStream is = new FileInputStream(f);
            digestInputStream = new DigestInputStream(is, messageDigest);
            byte[] buffer =new byte[1024];
            while (digestInputStream.read(buffer) > 0);
            messageDigest= digestInputStream.getMessageDigest();

            byte[] resultByteArray = messageDigest.digest();
            is.close();
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    //下面这个函数用于将字节数组换成成16进制的字符串
    private static String byteArrayToHex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < b.length - 1) {
                hs = hs + "";
            }
        }
        return hs;
    }
}
