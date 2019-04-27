package com.mySampleApplication.shared.model;

import com.zgx.bootdemo.entity.Page;

import java.io.Serializable;

/**
 * @author zhouguixing
 * @date 2019/4/18 13:11
 * @description 根据关键字查询、分页前端实体类
 */
public class KeywordPageData implements Serializable {
    private String keyword;//关键字
    private Page page;//分页实体类

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }
}
