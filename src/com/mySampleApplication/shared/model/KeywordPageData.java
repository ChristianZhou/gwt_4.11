package com.mySampleApplication.shared.model;


import java.io.Serializable;

/**
 * @author zhouguixing
 * @date 2019/4/18 13:11
 * @description 根据关键字查询、分页前端实体类
 */
public class KeywordPageData implements Serializable {
    private String keyword;//关键字
    private PageData pageData;//分页实体类

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public PageData getPageData() {
        return pageData;
    }

    public void setPageData(PageData pageData) {
        this.pageData = pageData;
    }
}
