package com.example.xiaojingw.utils;

public class UrlUtils {

    public static String createHomePagerUrl(int materiaId,int page){
        return "discovery/"+materiaId+"/"+page;
    }

    public static String getCoverPath(String pict_url,int size) {
        return "https:"+pict_url+"_"+size+"x"+size+".jpg";
    }

    public static String getCoverPath(String pict_url) {
        return "https:"+pict_url;
    }

    public static String getTicketUrl(String url) {
        if (url.startsWith("http") || url.startsWith("https")){
            return url;
        }else {
            return "https:" + url;
        }
    }

    public static String getSelectedPageContentUrl(int favorites_id) {
        return "recommend/"+favorites_id;
    }


    public static String getOnSellPageUrl(int mCurrentPage) {
        return "onSell/"+mCurrentPage;
    }
}
