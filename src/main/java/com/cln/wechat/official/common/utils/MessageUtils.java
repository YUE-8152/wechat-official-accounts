package com.cln.wechat.official.common.utils;


import com.cln.wechat.official.bean.constant.WeChatConstant;
import com.cln.wechat.official.bean.mp.Articles;
import com.cln.wechat.official.bean.mp.NewsMessage;
import com.cln.wechat.official.bean.mp.TextMessage;
import com.cln.wechat.official.bean.mp.WxMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.apache.commons.collections4.MapUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.*;

/**
 * @ProjectName: wechat-official-accounts
 * @Package: com.cln.wechat.official.common.utils
 * @ClassName: MessageUtils
 * @Author: YUE
 * @Description:
 * @Date: 2021/8/3 17:08
 * @Version: 1.0
 */
public class MessageUtils {
    /**
     * 接收request对象，读取xml内容，转换成map对象
     *
     * @param request:
     * @return: java.util.Map<java.lang.String, java.lang.String>
     * @Author: YUE
     * @Date: 2020/9/4 9:13
     **/
    public static Map<String, String> parseXmlToMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        InputStream ins = null;
        try {
            ins = request.getInputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Document doc = null;
        try {
            doc = reader.read(ins);
            Element root = doc.getRootElement();
            List<Element> list = root.elements();
            for (Element e : list) {
                map.put(e.getName(), e.getText());
            }
            return map;
        } catch (DocumentException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (null != ins) {
                    ins.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将消息转换成xml格式的字符串
     *
     * @param msg:   各种信息类，如文本信息类，图片信息类，音频信息类等。（都是WxMessage的子类）
     * @param child: 用来确定到底是哪一种子类
     * @return: java.lang.String
     * @Author: YUE
     * @Date: 2020/9/4 9:12
     **/
    public static String parseMsgToXml(WxMessage msg, Class child) {
        XStream xstream = new XStream();
        xstream.alias("xml", child);
        return xstream.toXML(msg);
    }

    /**
     * 接收request对象，转换成map对象后，再转成WxMessage对象
     *
     * @param map:
     * @param msgType:
     * @return: com.cln.school.bean.mp.WxMessage
     * @Author: YUE
     * @Date: 2020/9/4 9:48
     **/
    public static WxMessage mapToWxMessage(Map<String, String> map, String msgType) {
        WxMessage wxMessage = new WxMessage();
        wxMessage.setToUserName(MapUtils.getString(map, "FromUserName"));
        wxMessage.setFromUserName(MapUtils.getString(map, "ToUserName"));
        Date date = new Date();
        wxMessage.setCreateTime(date.getTime());
        wxMessage.setMsgType(msgType);
        return wxMessage;
    }

    /**
     * 接收request对象，转换成map对象后，再转成TextMessage对象
     *
     * @param map:
     * @param content:
     * @return: com.cln.school.bean.mp.TextMessage
     * @Author: YUE
     * @Date: 2020/9/4 9:57
     **/
    public static TextMessage mapToTextMessage(Map<String, String> map, String content) {
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(MapUtils.getString(map, "FromUserName"));
        textMessage.setFromUserName(MapUtils.getString(map, "ToUserName"));
        Date date = new Date();
        textMessage.setCreateTime(date.getTime());
        textMessage.setMsgType(WeChatConstant.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setContent(content);
        return textMessage;
    }

    /**
     * 接收request对象，转换成map对象后，再转成NewsMessage对象
     *
     * @param map:
     * @param title:
     * @param description:
     * @param picUrl:
     * @param url:
     * @return: com.cln.school.bean.mp.NewsMessage
     * @Author: YUE
     * @Date: 2020/9/4 10:21
     **/
    public static NewsMessage mapToNewsMessage(Map<String, String> map, String title, String description, String picUrl, String url) {
        NewsMessage message = new NewsMessage();
        message.setToUserName(MapUtils.getString(map, "FromUserName"));
        message.setFromUserName(MapUtils.getString(map, "ToUserName"));
        Date date = new Date();
        message.setCreateTime(date.getTime());
        message.setMsgType(WeChatConstant.RESP_MESSAGE_TYPE_NEWS);

        List<Articles> articles = new ArrayList<>();
        Articles item = new Articles();
        item.setTitle(title);
        item.setDescription(description);
        item.setPicUrl(picUrl);
        item.setUrl(url);
        articles.add(item);
        message.setArticles(articles);
        message.setArticleCount(articles.size());
        return message;
    }

    /**
     * TextMessage对象转成xml
     *
     * @param textMessage:
     * @return: java.lang.String
     * @Author: YUE
     * @Date: 2020/9/4 10:22
     **/
    public static String textMessageToXml(TextMessage textMessage) {
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }

    /**
     * NewsMessage对象转成xml
     *
     * @param newsMessage:
     * @return: java.lang.String
     * @Author: YUE
     * @Date: 2020/9/4 10:22
     **/
    public static String newsMessageToXml(NewsMessage newsMessage) {
        xstream.alias("xml", newsMessage.getClass());
        xstream.alias("item", new Articles().getClass());
        return xstream.toXML(newsMessage);
    }

    /**
     * 对象到xml的处理
     *
     * @param null:
     * @return: null
     * @Author: YUE
     * @Date: 2020/9/4 10:23
     **/
    private static XStream xstream = new XStream(new XppDriver() {
        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                @Override
                @SuppressWarnings("rawtypes")
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                @Override
                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });
}
