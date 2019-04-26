package com.mySampleApplication.client.data;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author zhouguixing
 * @date 2019/4/24 19:56
 * @description 根据关键字查询分页参数
 */
public class KeywordPageData implements IsSerializable {
    private String keyword;
    private PageData page;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public PageData getPage() {
        return page;
    }

    public void setPage(PageData page) {
        this.page = page;
    }
}
