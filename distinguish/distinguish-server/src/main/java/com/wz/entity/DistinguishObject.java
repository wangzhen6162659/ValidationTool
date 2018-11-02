package com.wz.entity;

import lombok.Data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Data
public class DistinguishObject {
    /**
     * 用户需要点击次数
     */
    private int clickNum;
    /**
     * 后端验证点击下标集合
     */
    private List<Integer> locations = new ArrayList<>();
    /**
     * 验证图片集合
     */
    private InputStream[] finalImages;
    /**
     * 验证图片
     */
    private BufferedImage img;
}
