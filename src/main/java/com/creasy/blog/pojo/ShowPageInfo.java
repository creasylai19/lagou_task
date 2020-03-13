package com.creasy.blog.pojo;

import java.util.List;

public class ShowPageInfo {

    private Integer currentPage;
    private List<Integer> showPages;
    private boolean hasPrevious;
    private boolean hasNext;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public List<Integer> getShowPages() {
        return showPages;
    }

    public void setShowPages(List<Integer> showPages) {
        this.showPages = showPages;
    }

    public boolean isHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    @Override
    public String toString() {
        return "ShowPageInfo{" +
                "currentPage=" + currentPage +
                ", showPages=" + showPages +
                ", hasPrevious=" + hasPrevious +
                ", hasNext=" + hasNext +
                '}';
    }
}
