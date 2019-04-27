package com.mySampleApplication.shared.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhouguixing
 * @date 2019/4/18 9:17
 * @description 分页
 */
public class PageData<T> implements Serializable {
    private Integer offset;//起始码
    private Integer limit;//记录数
    private Long totalSize;//总记录数
    private List<T> list;//列表
    private String sortField;//排序字段
    private Integer sortBy;//1（正序），-1（倒序）

    public Integer getSortBy() {
        return sortBy;
    }

    public void setSortBy(Integer sortBy) {
        this.sortBy = sortBy;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }
}
