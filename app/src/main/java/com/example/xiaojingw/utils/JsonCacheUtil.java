package com.example.xiaojingw.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.xiaojingw.base.BaseApplication;
import com.example.xiaojingw.model.doment.CacheWithDuration;
import com.google.gson.Gson;

import java.util.List;

public class JsonCacheUtil {

    private static final String JSON_CACHE_SP_NAME="json_cache_sp_name";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    private JsonCacheUtil(){
        sharedPreferences = BaseApplication.getContext().getSharedPreferences(JSON_CACHE_SP_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveCache(String key,Object value){
        this.saveCache(key,value,-1L);
    }

    public void saveCache(String key,Object value, long duration){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        String toJson = gson.toJson(value);
        if (duration!=-1L){
            duration += System.currentTimeMillis();
        }
        CacheWithDuration cacheWithDuration = new CacheWithDuration(duration,toJson);
        String cacheWith = gson.toJson(cacheWithDuration);
        edit.putString(key,cacheWith);
        edit.apply();
    }

    public void delCache(String key){
        sharedPreferences.edit().remove(key).apply();
    }

    public <T> T getValue(String key,Class<T> tClass){
        String valueWithDuration = sharedPreferences.getString(key, null);
        if (valueWithDuration == null) {
            return null;
        }
        CacheWithDuration cacheWithDuration = gson.fromJson(valueWithDuration, CacheWithDuration.class);
        long duration = cacheWithDuration.getDuration();
        if (duration != -1 && duration - System.currentTimeMillis() <= 0){
            return null;
        }else {
            String cache = cacheWithDuration.getCache();
            T result = gson.fromJson(cache, tClass);
            return result;
        }
    }

    private static JsonCacheUtil jsonCacheUtil = null;

    public static JsonCacheUtil getInstance(){
        if (jsonCacheUtil == null) {
            jsonCacheUtil = new JsonCacheUtil();
        }
        return jsonCacheUtil;
    }

}
