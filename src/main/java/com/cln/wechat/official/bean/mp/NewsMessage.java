package com.cln.wechat.official.bean.mp;

import java.util.List;

/**
 * @ProjectName: cln-school-app
 * @Package: com.cln.school.bean.mp
 * @ClassName: NewsMessage
 * @Author: YUE
 * @Description:
 * @Date: 2020/9/4 9:23
 * @Version: 1.0
 */
public class NewsMessage extends WxMessage {
    private int ArticleCount;

    private List<Articles> Articles;

    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }

    public List<Articles> getArticles() {
        return Articles;
    }

    public void setArticles(List<Articles> articles) {
        Articles = articles;
    }
}
