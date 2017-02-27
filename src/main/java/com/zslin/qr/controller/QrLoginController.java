package com.zslin.qr.controller;

import com.zslin.basic.tools.ConfigTools;
import com.zslin.qr.tools.QrGenUtil;
import com.zslin.wx.tools.ExchangeTools;
import com.zslin.wx.tools.InternetTools;
import com.zslin.wx.tools.JsonTools;
import com.zslin.wx.tools.WxConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 钟述林 393156105@qq.com on 2017/2/8 16:46.
 */
@Controller
@RequestMapping(value = "qr/login")
public class QrLoginController {

    @Autowired
    private WxConfig wxConfig;

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private ExchangeTools exchangeTools;

    @GetMapping(value = "index")
    public String index(HttpServletRequest request, HttpServletResponse response) {

        return "qr/login/index";
    }

    /**
     * 获取uuid及二维码图片地址
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @GetMapping(value = "showQrGen")
    public void showQrGen(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //生成UUID随机数
//        UUID randomUUID = UUID.randomUUID();
        String uuid = UUID.randomUUID().toString();
        //通过应用获取共享的uuid集合
//        Map<String,String> map = (Map) req.getServletContext().getAttribute("UUID_MAP");
//        if (map == null) {
//            map = new HashMap<>();
//            req.getServletContext().setAttribute("UUID_MAP", map);
//        }
        //把uuid放入map中
//        map.put(randomUUID.toString(), null);
        //二维码图片扫描后的链接
        String url = "http://zsl8.5166.info/qr/login/loginByQrGen?uuid="+ uuid;
        String fullPath = URLEncoder.encode(url, "UTF-8");
//        String authPath = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=hztOauth2#wechat_redirect";
        String authPath = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect";
        authPath = authPath.replace("APPID", wxConfig.getAppid()).replace("REDIRECT_URI", fullPath);
        System.out.println(authPath);
        //生成二维码图片
        ByteArrayOutputStream qrOut = QrGenUtil.createQrGen(authPath);
        String fileName = uuid+ ".jpg";
        String path = configTools.getUploadPath("/temp");
        OutputStream os = new FileOutputStream(new File(path,fileName));
        os.write(qrOut.toByteArray());
        os.flush();
        os.close();
        //返回页面json字符串，uuid用于轮询检查时所带的参数， img用于页面图片显示
        String jsonStr = "{\"uuid\":\"" + uuid + "\",\"img\":\"" + "/temp/"+fileName + "\"}";
        OutputStream outStream = resp.getOutputStream();
        outStream.write(jsonStr.getBytes());
        outStream.flush();
        outStream.close();
    }

    /**
     * 手机端扫描二维码执行的方法
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @GetMapping(value = "loginByQrGen")
    public void loginByQrGen(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 获取二维码链接中的uuid
        String uuid = req.getParameter("uuid");
        // 通过应用获取共享的uuid集合
//        Map uuidMap = (Map) req.getServletContext().getAttribute("UUID_MAP");
        // 如果集合内没有这个uuid，则响应结果
//        if (uuidMap == null || !uuidMap.containsKey(uuid)) {
//            resp.getOutputStream().write("二维码不存在或已失效！".getBytes());
//            return;
//        }
        // 根据微信传来的code来获取用户的openID
        String code = req.getParameter("code");
        System.out.println("================myCode:"+code);
        try {
//            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+wxConfig.getAppid()
//                    + "&secret="+wxConfig.getSecret()
//                    + "&grant_type=authorization_code" + "&code=" + code;

//            String openid = exchangeTools.getUserOpenId(code);
//            System.out.println("===================myOpenid:"+openid);

            resp.getOutputStream().write("你还未绑定，请关注微信号并绑定账号！并使用微信客户端扫描！".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * PC端不断进行轮询检查的方法
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @GetMapping(value = "checkScan")
    public void checkScan(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //获取页面的uuid参数
        String uuid = req.getParameter("uuid");
        //通过应用获取共享的uuid集合
        Map<String, String> map = (Map) req.getServletContext().getAttribute("UUID_MAP");
        if (map != null) {
            //查询该uuid是否存在，且二维码已经被扫描并匹配到账号
            if(map.containsKey(uuid)){
                String openid = (String) map.get(uuid);
                if (openid != null) {
                    //从集合中移除
                    map.remove(uuid);
                    //设置登录信息
                    req.getSession().setAttribute("openid", openid);
                    resp.getOutputStream().write("ok".getBytes());
                }else{
                    resp.getOutputStream().write("native".getBytes());
                }
            }
        }
    }
}
