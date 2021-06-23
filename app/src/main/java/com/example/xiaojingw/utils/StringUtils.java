package com.example.xiaojingw.utils;

public class StringUtils {

 public static String results(String str,String b){

     int strStartIndex = str.indexOf(b);
     int strEndIndex = str.indexOf(b);
     /* 开始截取 */
     if (str!=null && str.length()>0){
         String result = str.substring(strStartIndex, strEndIndex+13);
         return result;
     }
    return null;
 }

}
