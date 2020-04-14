package com.findhouse.utils;

public class Url {
    private static String server = "192.168.0.105";  // 服务器
    private String type;    // 请求类目
    private String route;   // 请求路由

    public static String getServer() {
        return server;
    }

    public static void setServer(String server) {
        Url.server = server;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String toString(){
        return "http://"+server+":8080"+type+route;
    }
}
