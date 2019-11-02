package com.example.dell.linkgame.view;

import android.graphics.Bitmap;

/**
 * 封装图片与图片ID的类
 */
public class LinkPieceImage {

    private Bitmap image;  //图片
    private int imageId;   //图片ID

    /**
     * 构造函数
     * @param image 图片
     * @param imageId 图片ID
     */
    public LinkPieceImage(Bitmap image, int imageId) {
        super();
        this.image = image;
        this.imageId = imageId;
    }

    /**
     * 设置图片
     * @param image
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * @return 图片
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * 设置图片ID
     * @param imageId
     */
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    /**
     * @return 图片ID
     */
    public int getImageId() {
        return imageId;
    }

}

