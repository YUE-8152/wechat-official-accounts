package com.cln.wechat.official.bean.mp;

import org.springframework.beans.BeanUtils;

/**
 * @ProjectName: cln-school-app
 * @Package: com.cln.school.bean.mp
 * @ClassName: TextMessage
 * @Author: YUE
 * @Description:
 * @Date: 2020/9/3 19:29
 * @Version: 1.0
 */
public class TextMessage extends WxMessage{
    /**
     * 文本消息内容
     **/
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    /**
     * 用来把基类的属性值复制给子类
     *
     * @param msg:
     * @return: com.cln.school.bean.mp.TextMessage
     * @Author: YUE
     * @Date: 2020/9/3 19:30
     **/
    public static TextMessage adapt(WxMessage msg) {
        TextMessage textMessage = new TextMessage();
        BeanUtils.copyProperties(msg, textMessage);
        return textMessage;
    }
}
