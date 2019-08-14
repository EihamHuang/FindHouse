package com.huangyihang.data;

import java.io.Serializable;

/**
 * - @Description:  新闻类
 * - @Author:  huangyihang
 * - @Time:  2019-08-14 15:05
 */
public class News implements Serializable {
    private String id;
    private String title;
    private String content;
    private String pdate;
    private String src;
    private String img;
    private String url;
    private String pdate_src;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPdate_src() {
        return pdate_src;
    }

    public void setPdate_src(String pdate_src) {
        this.pdate_src = pdate_src;
    }
}
