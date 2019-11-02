package com.example.dell.linkgame.view;

import android.graphics.Point;
/**
 * 封装连连看中一个小方块的类
 */
public class LinkPiece {

    private LinkPieceImage linkPieceImage; //该方块对应的图片

    private int beginX;  //该方块左上角的x坐标
    private int beginY;  //该方块左上角的y座标

    private int indexX;  //该方块在LinkPiece[][]数组中第一维的索引值
    private int indexY;  //该方块在LinkPiece[][]数组中第二维的索引值

    /**
     * 设置该方块在数组中的索引值
     * @param indexX 该方块在数组中第一维的索引值
     * @param indexY 该方块在数组中第二维的索引值
     */
    public LinkPiece(int indexX, int indexY) {
        this.indexX = indexX;
        this.indexY = indexY;
    }

    /**
     * 设置方块所对应的图片
     * @param linkPieceImage
     */
    public void setLinkPieceImage(LinkPieceImage linkPieceImage) {
        this.linkPieceImage = linkPieceImage;
    }

    /**
     * @return 获得方块所对应的图片
     */
    public LinkPieceImage getLinkPieceImage() {
        return linkPieceImage;
    }

    /**
     * 设置该方块左上角的X坐标
     * @param beginX
     */
    public void setBeginX(int beginX) {
        this.beginX = beginX;
    }

    /**
     * @return 获得该方块左上角的X坐标
     */
    public int getBeginX() {
        return beginX;
    }

    /**
     * 设置该方块左上角的Y坐标
     * @param beginY
     */
    public void setBeginY(int beginY) {
        this.beginY = beginY;
    }

    /**
     * @return 获得该方块的上角的Y坐标
     */
    public int getBeginY() {
        return beginY;
    }

    /**
     * 设置该方块在LinkPiece[][]数组中第一维的索引值
     * @param indexX
     */
    public void setIndexX(int indexX) {
        this.indexX = indexX;
    }

    /**
     * @return 获得该方块在LinkPiece[][]数组中第一维的索引值
     */
    public int getIndexX() {
        return indexX;
    }

    /**
     * 设置该方块在LinkPiece[][]数组中第二维的索引值
     * @param indexY
     */
    public void setIndexY(int indexY) {
        this.indexY = indexY;
    }

    /**
     * @return 获得该方块在LinkPiece[][]数组中第二维的索引值
     */
    public int getIndexY() {
        return indexY;
    }

    /**
     * 获取该方块的中心位置
     * @return 中心点的坐标对象Point
     */
    public Point getCenter() {
        return new Point(getBeginX() + getLinkPieceImage().getImage().getWidth() / 2,
                getBeginY() + getLinkPieceImage().getImage().getHeight() / 2);
    }

    /**
     * 判断两个方块上的图片是否相同
     * @param otherLinkPiece 另外的一个LinkPiece方块对象
     * @return 是否相同
     */
    public boolean isSameImage(LinkPiece otherLinkPiece) {
        if (linkPieceImage == null) {
            if (otherLinkPiece.linkPieceImage != null)
                return false;
        }
        /*当两个Piece的图片ID相同时，即认为这两个Piece上的图片相同*/
        return linkPieceImage.getImageId() == otherLinkPiece.linkPieceImage.getImageId();
    }

}

