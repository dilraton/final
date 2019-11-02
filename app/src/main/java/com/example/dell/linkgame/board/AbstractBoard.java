package com.example.dell.linkgame.board;

import android.util.Log;

import java.util.List;

import com.example.dell.linkgame.utils.GameConfig;
import com.example.dell.linkgame.utils.ImageUtil;
import com.example.dell.linkgame.view.LinkPiece;
import com.example.dell.linkgame.view.LinkPieceImage;

/**
 * 初始游戏区域创建的抽象类
 */

public abstract class AbstractBoard {

    /**
     * 抽象方法, 由子类实现
     * 返回一个List集合，该集合里面存放初始化游戏时所需填充的LinkPiece对象
     */
    protected abstract List<LinkPiece> createLinkPieces(GameConfig config, LinkPiece[][] linkPieces);

    /**
     * 返回LinkPiece数组
     * @param config
     * @return
     */
    public LinkPiece[][] create(GameConfig config,String theme) {

        LinkPiece[][] linkPieces = new LinkPiece[config.getXSize()][config.getYSize()];// 创建整体的LinkPiece[][]数组
        List<LinkPiece> notNullLinkPieces = createLinkPieces(config, linkPieces); // 返回非空的LinkPiece集合, 该集合由子类创建
        List<LinkPieceImage> playImages = ImageUtil.getPlayImages(config.getContext(), notNullLinkPieces.size(),theme); // 根据非空LinkPiece对象集合的大小获取要显示的图片

        int imageWidth = playImages.get(0).getImage().getWidth(); // 所有图片的宽、高都是相同的
        int imageHeight = playImages.get(0).getImage().getHeight();
        Log.i("AbstractBoard", "create:imageWidth" + imageWidth);
        Log.i("AbstractBoard", "create:imageHeight" + imageHeight);
        for (int i = 0; i < notNullLinkPieces.size(); i++) { // 遍历非空的LinkPiece集合
            LinkPiece linkPiece = notNullLinkPieces.get(i);  // 获取每个LinkPiece对象
            linkPiece.setLinkPieceImage(playImages.get(i));  // 为每个LinkPiece对象添加图片playImages.get(i)
            linkPiece.setBeginX(linkPiece.getIndexX() * imageWidth + config.getBeginImageX()); // 计算设置每个方块左上角的X、Y坐标
            linkPiece.setBeginY(linkPiece.getIndexY() * imageHeight + config.getBeginImageY());
            linkPieces[linkPiece.getIndexX()][linkPiece.getIndexY()] = linkPiece; // 将该方块对象放入方块数组的相应位置处
        }
        return linkPieces;
    }
}

