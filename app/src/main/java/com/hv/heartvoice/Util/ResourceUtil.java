package com.hv.heartvoice.Util;

/**
 * 资源工具类
 */
public class ResourceUtil {

    /**
     * 将相对资源路径,转化为绝对资源路径
     * @param uri
     * @return
     */
    public static String resourceUri(String uri){
        return String.format(Constant.RESOURCE_ENDPOINT, uri);
    }
}
