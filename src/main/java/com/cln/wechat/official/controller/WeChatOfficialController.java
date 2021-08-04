package com.cln.wechat.official.controller;

import com.cln.wechat.official.common.core.Result;
import com.cln.wechat.official.common.core.ServiceException;
import com.cln.wechat.official.common.utils.AESUtils;
import com.cln.wechat.official.service.WeChatOfficialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: wechat-official-accounts
 * @Package: com.cln.wechat.official.controller
 * @ClassName: WeChatOfficialController
 * @Author: YUE
 * @Description:
 * @Date: 2021/8/3 15:05
 * @Version: 1.0
 */
@RestController
@RequestMapping("/bkh")
public class WeChatOfficialController {
    private static final Logger logger = LoggerFactory.getLogger(WeChatOfficialController.class);

    @Autowired
    private WeChatOfficialService weChatOfficialService;

    @RequestMapping(value = "/createMenu")
    public Result createWeChatOfficialMenu() {
        try {
            logger.info("=============>邦客乘车公众号 创建菜单Controller");
            weChatOfficialService.createWeChatOfficialMenu();
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("=============>邦客乘车公众号 创建菜单Controller 异常", e);
            throw new ServiceException();
        }
    }

    @RequestMapping(value = "/messageCallBack", method = RequestMethod.GET)
    public String messageCallBack(String signature, String timestamp, String nonce, String echostr) {
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        logger.info("=============>邦客乘车公众号 回调参数：signature={}，timestamp={}， nonce={}， echostr={}", signature, timestamp, nonce, echostr);
        if (AESUtils.checkSignature(signature, timestamp, nonce)) {
            logger.info("=============>邦客乘车公众号 回调返回结果：echostr={}", echostr);
            return echostr;
        } else {
            return "";
        }
    }

    @RequestMapping(value = "/messageCallBack", method = RequestMethod.POST)
    public String messageCallBack(HttpServletRequest request) {
        String processRequest = weChatOfficialService.messageCallBack(request);
        logger.info("=============>邦客乘车公众号  返回结果：{} ", processRequest);
        return processRequest;
    }

}
