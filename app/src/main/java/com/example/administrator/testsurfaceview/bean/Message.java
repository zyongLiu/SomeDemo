package com.example.administrator.testsurfaceview.bean;

/**
 * Created by Liu on 2016/12/8.
 */
public class Message {

    /**
     * version : 1.1
     * description : 更加流畅了。。。
     * url : http://192.168.1.128/test/app-debug.apk
     */

    private String version;
    private String description;
    private String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return version+"----"+description+"----"+url;
    }
}
