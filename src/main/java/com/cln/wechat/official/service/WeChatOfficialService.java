package com.cln.wechat.official.service;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: wechat-official-accounts
 * @Package: com.cln.wechat.official.service
 * @ClassName: WeChatOfficialService
 * @Author: YUE
 * @Description:
 * @Date: 2021/8/3 14:17
 * @Version: 1.0
 */
public interface WeChatOfficialService {
    String messageCallBack(HttpServletRequest request);

    String createWeChatOfficialMenu();
}
