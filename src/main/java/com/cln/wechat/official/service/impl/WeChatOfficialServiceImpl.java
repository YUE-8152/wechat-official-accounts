package com.cln.wechat.official.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cln.wechat.official.bean.constant.WeChatConstant;
import com.cln.wechat.official.bean.mp.NewsMessage;
import com.cln.wechat.official.bean.mp.TextMessage;
import com.cln.wechat.official.common.utils.HttpClientUtils;
import com.cln.wechat.official.common.utils.MessageUtils;
import com.cln.wechat.official.service.WeChatOfficialService;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: wechat-official-accounts
 * @Package: com.cln.wechat.official.service.impl
 * @ClassName: WeChatOfficialServiceImpl
 * @Author: YUE
 * @Description:
 * @Date: 2021/8/3 14:17
 * @Version: 1.0
 */
@Service
public class WeChatOfficialServiceImpl implements WeChatOfficialService {

    private static final Logger logger = LoggerFactory.getLogger(WeChatOfficialServiceImpl.class);

    @Value("${bkh.mp.appId}")
    private String bkhMpAppId;

    @Value("${bkh.mp.secret}")
    private String bkhMpSecret;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String messageCallBack(HttpServletRequest request) {
        String result = "您好，欢迎关注“邦客惠乘车”！\n" +
                "如您想了解邦客惠-刷码乘车的使用教程，请回复 1\n" +
                "如您想了解邦客惠-公交卡功能的使用教程，请回复 2\n\n\n" +
                "提示：每天签到领积分，积分支持兑现金、兑换全网最低价商品！！\n\n" +
                "↓↓↓猛戳下方菜单，体验更多“惠民、便民、安民”服务";

        String result1 = "请您直接给我们公众号留言，我们将尽快回复您\n" +
                "如您想了解邦客惠-刷码乘车的使用教程，请回复 1\n" +
                "如您想了解邦客惠-公交卡功能的使用教程，请回复 2";

        String result2 = "如您想了解邦客惠-刷码乘车的使用教程，请回复 1\n" +
                "如您想了解邦客惠-公交卡功能的使用教程，请回复 2\n\n\n" +
                "提示：每天签到领积分，积分支持兑现金、兑换全网最低价商品！！\n\n" +
                "↓↓↓猛戳下方菜单，体验更多“惠民、便民、安民”服务";
        // xml格式的消息数据
        String respXml = null;
        // 调用parseXml方法解析请求消息
        Map<String, String> requestMap = MessageUtils.parseXmlToMap(request);
        logger.info("=============>邦客乘车公众号  调用parseXml方法解析请求消息 requestMap{}", requestMap);
        // 公众号OpenId
        String fromUserName = requestMap.get("FromUserName");
        // 消息类型
        String msgType = (String) requestMap.get(WeChatConstant.MsgType);
        String content = requestMap.get(WeChatConstant.Content);

        // 事件推送
        if (msgType.equals(WeChatConstant.REQ_MESSAGE_TYPE_EVENT)) {
            // 事件类型
            String eventType = (String) requestMap.get(WeChatConstant.Event);
            // 关注
            if (eventType.equals(WeChatConstant.EVENT_TYPE_SUBSCRIBE)) {
                Map<String, Object> map = new HashMap<>();
                String unionId = "";
                String officialOpenId = "";
                map = getOfficialInfo(fromUserName);
                unionId = MapUtils.getString(map, "unionId");
                logger.info("=============>邦客乘车公众号  关注公众号  参数信息：{}", map);
                officialOpenId = MapUtils.getString(map, "officialOpenId");
                //  重试刷新AccessToken
                if (!StringUtils.isNoneBlank(unionId) || !StringUtils.isNoneBlank(officialOpenId)) {
                    logger.info("=============>邦客乘车公众号  关注公众号  无法获取公众号信息  重置AccessToken：{}", map);
                    map = getOfficialInfo(fromUserName);
                    unionId = MapUtils.getString(map, "unionId");
                    officialOpenId = MapUtils.getString(map, "officialOpenId");
                }
                if (StringUtils.isNoneBlank(unionId) && StringUtils.isNoneBlank(officialOpenId)) {
                    // 根据unionId匹配小程序中获取到的unionId  实现相关业务

                } else {
                    return null;
                }
            }
        } else if (WeChatConstant.REQ_MESSAGE_TYPE_TEXT.equals(msgType)) {
            List<String> content1 = Arrays.asList("1", "乘车", "乘车码", "二维码", "怎么用乘车码", "扫码乘车", "付款", "怎么支付", "怎样支付",
                    "开通", "开通乘车码", "免密码支付", "免密支付", "微信支付", "微信付款");
            List<String> content2 = Arrays.asList("2", "公交卡", "充值公交卡", "查余额", "充值");
            List<String> content3 = Arrays.asList("客服", "人工客服", "公交客服");
            if (content1.contains(content)) {
                NewsMessage newsMessage = MessageUtils.mapToNewsMessage(requestMap, "邦客惠乘车码的正确打开方式 Get！", "如何使用邦客惠乘车码点击查看详情",
                        "https://client-upload-prod.oss-cn-shenzhen.aliyuncs.com/bkhBus/bkhccmdzqdkfs.jpg",
                        "https://mp.weixin.qq.com/s?__biz=Mzg4NjUyNDYwNg==&mid=100000126&idx=1&sn=e11ee829a3e9cb1e5f863b71753af97d&scene=19#wechat_redirect");
                respXml = MessageUtils.newsMessageToXml(newsMessage);
                logger.info("=============>邦客乘车公众号 公众号中发送消息【{}】返回结果：{}", content, respXml);
            } else if (content2.contains(content)) {
                NewsMessage newsMessage = MessageUtils.mapToNewsMessage(requestMap, "惠民公交卡 | 使用攻略", "惠民公交卡使用攻略点击查看详情",
                        "https://client-upload-prod.oss-cn-shenzhen.aliyuncs.com/bkhBus/hmgjsygl.jpg",
                        "https://mp.weixin.qq.com/s?__biz=Mzg4NjUyNDYwNg==&mid=100000036&idx=1&sn=45d6c11888a25777806302d63b265bad&scene=19#wechat_redirect");
                respXml = MessageUtils.newsMessageToXml(newsMessage);
                logger.info("=============>邦客乘车公众号 公众号中发送消息【{}】返回结果：{}", content, respXml);
            } else if (content3.contains(content)) {
                TextMessage textMessage = MessageUtils.mapToTextMessage(requestMap, result1);
                respXml = MessageUtils.textMessageToXml(textMessage);
                logger.info("=============>邦客乘车公众号 公众号中发送消息【{}】返回结果：{}", content, respXml);
            } else {
                TextMessage textMessage = MessageUtils.mapToTextMessage(requestMap, result2);
                respXml = MessageUtils.textMessageToXml(textMessage);
            }
        }
        if (respXml == null) {
            TextMessage textMessage = MessageUtils.mapToTextMessage(requestMap, result);
            respXml = MessageUtils.textMessageToXml(textMessage);
        }
        logger.info("=============>邦客乘车公众号 订阅公众号  发送消息结果:{}", respXml);
        return respXml;
    }

    @Override
    public String createWeChatOfficialMenu() {
        String jsonMenu = getWeChatOfficialMenu();
        String accessToken = getOfficialAccessToken();
        String path = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessToken;
        try {
            URL url = new URL(path);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDoOutput(true);
            http.setDoInput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.connect();
            OutputStream os = http.getOutputStream();
            os.write(jsonMenu.getBytes("UTF-8"));
            os.close();

            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] bt = new byte[size];
            is.read(bt);
            String message = new String(bt, "UTF-8");
            JSONObject jsonMsg = JSONObject.parseObject(message);
            return jsonMsg.getString("errcode");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getWeChatOfficialMenu() {
        JSONObject jsonObject = new JSONObject();
        JSONArray btnArr = new JSONArray();
//        //一级菜单
//        JSONObject menu1 = new JSONObject();
//        menu1.put("name", "公交助手");
//        menu1.put("type", "miniprogram");
//        menu1.put("url", "http://mp.weixin.qq.com");
//        menu1.put("appid", "wxb16bc1912a87168e");
//        menu1.put("pagepath", "pages/login/login");
//        btnArr.add(menu1);
//
//        //一级菜单
//        JSONObject menu3 = new JSONObject();
//        menu3.put("name", "惠民福利");
//        menu3.put("type", "miniprogram");
//        menu3.put("url", "http://mp.weixin.qq.com");
//        menu3.put("appid", "wxb16bc1912a87168e");
//        menu3.put("pagepath", "minePages/parents-relevance/parents-relevance");
//        btnArr.add(menu3);

        //一级菜单
        JSONObject menu1 = new JSONObject();
        menu1.put("name", "公交助手");
        menu1.put("type", "click");

        JSONArray subBtn1Arr = new JSONArray();
        //二级菜单
        JSONObject subMenu1_1 = new JSONObject();
        subMenu1_1.put("name", "公告/通知");
        subMenu1_1.put("type", "miniprogram");
        subMenu1_1.put("url", "http://mp.weixin.qq.com");
        subMenu1_1.put("appid", "wx51103d007a84d074");
        subMenu1_1.put("pagepath", "minePages/parents-relevance/parents-relevance");
        subBtn1Arr.add(subMenu1_1);

        JSONObject subMenu1_2 = new JSONObject();
        subMenu1_2.put("name", "扫码乘车");
        subMenu1_2.put("type", "miniprogram");
        subMenu1_2.put("url", "http://mp.weixin.qq.com");
        subMenu1_2.put("appid", "wx51103d007a84d074");
        subMenu1_2.put("pagepath", "minePages/parents-relevance/parents-relevance");
        subBtn1Arr.add(subMenu1_2);

        JSONObject subMenu1_3 = new JSONObject();
        subMenu1_3.put("name", "线路查询");
        subMenu1_3.put("type", "miniprogram");
        subMenu1_3.put("url", "http://mp.weixin.qq.com");
        subMenu1_3.put("appid", "wx51103d007a84d074");
        subMenu1_3.put("pagepath", "minePages/parents-relevance/parents-relevance");
        subBtn1Arr.add(subMenu1_3);

        JSONObject subMenu1_4 = new JSONObject();
        subMenu1_4.put("name", "公交卡功能");
        subMenu1_4.put("type", "miniprogram");
        subMenu1_4.put("url", "http://mp.weixin.qq.com");
        subMenu1_4.put("appid", "wx51103d007a84d074");
        subMenu1_4.put("pagepath", "minePages/parents-relevance/parents-relevance");
        subBtn1Arr.add(subMenu1_4);

        JSONObject subMenu1_5 = new JSONObject();
        subMenu1_5.put("name", "乘车记录");
        subMenu1_5.put("type", "miniprogram");
        subMenu1_5.put("url", "http://mp.weixin.qq.com");
        subMenu1_5.put("appid", "wx51103d007a84d074");
        subMenu1_5.put("pagepath", "minePages/parents-relevance/parents-relevance");
        subBtn1Arr.add(subMenu1_5);

        menu1.put("sub_button", subBtn1Arr);
        btnArr.add(menu1);

        //一级菜单
        JSONObject menu2 = new JSONObject();
        menu2.put("name", "惠民福利 ");
        menu2.put("type", "click");

        JSONArray subBtn2Arr = new JSONArray();
        //二级菜单
        JSONObject subMenu2_1 = new JSONObject();
        subMenu2_1.put("name", "签到领积分");
        subMenu2_1.put("type", "miniprogram");
        subMenu2_1.put("url", "http://mp.weixin.qq.com");
        subMenu2_1.put("appid", "wx988f5db282500323");
        subMenu2_1.put("pagepath", "pages/integral/integral");
        subBtn2Arr.add(subMenu2_1);

        JSONObject subMenu2_2 = new JSONObject();
        subMenu2_2.put("name", "积分兑换");
        subMenu2_2.put("type", "miniprogram");
        subMenu2_2.put("url", "http://mp.weixin.qq.com");
        subMenu2_2.put("appid", "wx988f5db282500323");
        subMenu2_2.put("pagepath", "pages/index/index");
        subBtn2Arr.add(subMenu2_2);

        JSONObject subMenu2_3 = new JSONObject();
        subMenu2_3.put("name", "积分提现");
        subMenu2_3.put("type", "miniprogram");
        subMenu2_3.put("url", "http://mp.weixin.qq.com");
        subMenu2_3.put("appid", "wx988f5db282500323");
        subMenu2_3.put("pagepath", "pages/users/user_cash/index");
        subBtn2Arr.add(subMenu2_3);

        menu2.put("sub_button", subBtn2Arr);
        btnArr.add(menu2);


        //一级菜单
        JSONObject menu3 = new JSONObject();
        menu3.put("name", "更多服务");
        menu3.put("type", "click");

        JSONArray subBtn3Arr = new JSONArray();
        //二级菜单
        JSONObject subMenu3_1 = new JSONObject();
        subMenu3_1.put("name", "了解邦客惠");
        subMenu3_1.put("type", "miniprogram");
        subMenu3_1.put("url", "http://mp.weixin.qq.com");
        subMenu3_1.put("appid", "wx51103d007a84d074");
        subMenu3_1.put("pagepath", "pages/users/user_cash/index");
        subBtn3Arr.add(subMenu3_1);

        JSONObject subMenu3_2 = new JSONObject();
        subMenu3_2.put("name", "积分查询");
        subMenu3_2.put("type", "miniprogram");
        subMenu3_2.put("url", "http://mp.weixin.qq.com");
        subMenu3_2.put("appid", "wx988f5db282500323");
        subMenu3_2.put("pagepath", "pages/integral/integral");
        subBtn3Arr.add(subMenu3_2);

        JSONObject subMenu3_3 = new JSONObject();
        subMenu3_3.put("name", "积分规则");
        subMenu3_3.put("type", "view");
        subMenu3_3.put("url", "https://mp.weixin.qq.com/s?__biz=Mzg4NjUyNDYwNg==&mid=100000087&idx=1&sn=15bbe6c16848e7135a79e2f82bc43a8f&scene=19#wechat_redirect");
        subBtn3Arr.add(subMenu3_3);

        JSONObject subMenu3_4 = new JSONObject();
        subMenu3_4.put("name", "商务合作");
        subMenu3_4.put("type", "miniprogram");
        subMenu3_4.put("url", "http://mp.weixin.qq.com");
        subMenu3_4.put("appid", "wx51103d007a84d074");
        subMenu3_4.put("pagepath", "pages/integral/integral");
        subBtn3Arr.add(subMenu3_4);

        menu3.put("sub_button", subBtn3Arr);
        btnArr.add(menu3);

        jsonObject.put("button", btnArr);
        return jsonObject.toJSONString();
    }

    private Map<String, Object> getOfficialInfo(String openId) {
        String accessToken = getOfficialAccessToken();
        logger.info("=============>邦客乘车公众号 根据AccessToken获取公众号相关信息请求  accessToken={}", accessToken);
        String params = "access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
        logger.info("=============>邦客乘车公众号 根据AccessToken获取公众号相关信息请求参数：{}", params);
        Map<String, Object> map = new HashMap<>();
        String result = HttpClientUtils.sendPost("https://api.weixin.qq.com/cgi-bin/user/info", params);
        logger.info("=============>邦客乘车公众号 获取公众号相关信息结果：{}", result);
        JSONObject json = JSONObject.parseObject(result);
        map.put("unionId", json.getString("unionid"));
        map.put("officialOpenId", json.getString("openid"));
        return map;
    }

    private String getOfficialAccessToken() {
        String accessToken = redisTemplate.opsForValue().get(WeChatConstant.BKH_OFFICIAL_ACCESS_TOKEN);
        if (!StringUtils.isNoneBlank(accessToken)) {
            String params = "grant_type=client_credential" + "&appid=" + bkhMpAppId + "&secret=" + bkhMpSecret;
            logger.info("=============>邦客乘车公众号 获取AccessToken参数：{}", params);
            String result = HttpClientUtils.sendGet("https://api.weixin.qq.com/cgi-bin/token", params);
            JSONObject jsonObject = JSONObject.parseObject(result);
            logger.info("=============>邦客乘车公众号 获取AccessToken Http请求返回结果：{}", jsonObject);
            accessToken = jsonObject.getString("access_token");
            if (StringUtils.isNoneBlank(accessToken)) {
                logger.info("=============>邦客乘车公众号 获取AccessToken结果：{}", accessToken);
                redisTemplate.opsForValue().set(WeChatConstant.BKH_OFFICIAL_ACCESS_TOKEN, accessToken);
                return accessToken;
            }
        }
        return accessToken;
    }
}
