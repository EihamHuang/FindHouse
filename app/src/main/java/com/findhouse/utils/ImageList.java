package com.findhouse.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageList {

    //获取文件夹下所有文件名
    public static List<String> printFile(String path) {
        File file = new File(path);
        List<String> images = new ArrayList<String>();

        // 是文件夹的话
        if (file.isDirectory()) {
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(path + "/" + filelist[i]);
                if (!readfile.isDirectory()) {
                    images.add(readfile.getName());
                }
            }

        }
        return images;
    }

}
