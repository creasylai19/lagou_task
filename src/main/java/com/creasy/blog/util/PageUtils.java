package com.creasy.blog.util;

import com.creasy.blog.config.Constants;
import com.creasy.blog.pojo.ShowPageInfo;

import java.util.ArrayList;
import java.util.List;

public class PageUtils {

    public static ShowPageInfo getShowPageInfo(Integer pageNum, Long total){
        ShowPageInfo showPageInfo = new ShowPageInfo();
        showPageInfo.setCurrentPage(pageNum);
        long totalPage = total/ Constants.PAGE_SIZE + (total%Constants.PAGE_SIZE >= 1 ? 1 : 0);
        List<Integer> showPages = new ArrayList<>();
        if( totalPage >= 3 ) {//如果总页数大于等于3页，则显示当前页和前后页
            if( pageNum == 1 ) {
                showPages.add(pageNum);
                showPages.add(pageNum + 1);
                showPages.add(pageNum + 2);
            } else if( pageNum == totalPage ) {
                showPages.add(pageNum - 2);
                showPages.add(pageNum - 1);
                showPages.add(pageNum);
            } else {
                showPages.add(pageNum - 1);
                showPages.add(pageNum);
                showPages.add(pageNum + 1);
            }
        } else {
            for (int i = 1; i < totalPage + 1; i++) {
                showPages.add(i);
            }
        }
        if( pageNum < totalPage ) {
            showPageInfo.setHasNext(true);
        } else {
            showPageInfo.setHasNext(false);
        }
        if( pageNum > 1 ) {
            showPageInfo.setHasPrevious(true);
        } else {
            showPageInfo.setHasPrevious(false);
        }
        showPageInfo.setShowPages(showPages);
        return showPageInfo;
    }

}
