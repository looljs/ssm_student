package com.ljs.ssm_student.page;

/**
 * 页面信息
 */
public class Page {
    private int page;
    private int rows;
    private int start;
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getStart() {
        return (page-1)*rows;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
