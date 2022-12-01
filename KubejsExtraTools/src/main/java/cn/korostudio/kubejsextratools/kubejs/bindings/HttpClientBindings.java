package cn.korostudio.kubejsextratools.kubejs.bindings;

import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.http.HttpUtil;

import java.util.Map;

public class HttpClientBindings {
    public static String get(String url){
        return HttpUtil.get(url);
    }
    public static String get(String url, Map<String, Object> args){
        return HttpUtil.get(url,args);
    }
    public static String get(String url,int timeout,Map<String,Object>args){
        return HttpUtil.get(url,args,timeout);
    }
    public static void setMaxRedirectCount(int maxReject){
        HttpGlobalConfig.setMaxRedirectCount(maxReject);
    }
    public static String downloadString(String url,String charset){
        return HttpUtil.downloadString(url,charset);
    }
    public static long download(String url,String file){
        return HttpUtil.downloadFile(url,file);
    }
    public static String post(String url,Map<String,Object>args){
        return HttpUtil.post(url,args);
    }
    public static boolean isHttps(String url){
        return HttpUtil.isHttps(url);
    }

}
