package com.cln.wechat.official.bean.mp;

import java.io.Serializable;

/**
 * @ProjectName: cln-school-app
 * @Package: com.cln.school.bean.mp
 * @ClassName: Articles
 * @Author: YUE
 * @Description: 图文信息详情类
 * @Date: 2020/9/4 9:17
 * @Version: 1.0
 */
public class Articles implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    private String Title;
    private String Description;
    private String PicUrl;
    private String Url;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
