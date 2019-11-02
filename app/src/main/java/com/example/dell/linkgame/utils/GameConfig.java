package com.example.dell.linkgame.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 保存游戏初始化配置
 */
public class GameConfig {

    public static int PIECE_WIDTH = 40;     //每个方块图片的宽度
    public static int PIECE_HEIGHT = 40;   //每个方块图片的高度
    public static int DEFAULT_TIME = 100;          //每局游戏规定所用时间

    private int xSize;    //游戏面板的行数，即LinkPiece[][]数组第一维的长度
    private int ySize;    //游戏面板的列数，即LinkPiece[][]数组第二维的长度
    private int beginImageX; // 游戏面板中第一张图片出现的x坐标
    private int beginImageY; //游戏面板中第一张图片出现的y坐标

    private long gameTime; //记录游戏的总时间

    private Context context; //应用上下文

    private void calculateIconSize()    {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) this.getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm);
        PIECE_WIDTH= PIECE_HEIGHT = dm.widthPixels/(xSize);
    }

    /**
     * 提供一个参数构造器
     *
     * @param xSize
     *            LinkPiece[][]数组第一维长度
     * @param ySize
     *            LinkPiece[][]数组第二维长度
     * @param beginImageX
     *            Board中第一张图片出现的x坐标
     * @param beginImageY
     *            Board中第一张图片出现的y坐标
     * @param gameTime
     *            设置每局的时间, 单位是豪秒
     * @param context
     *            应用上下文
     */
    public GameConfig(int xSize, int ySize, int beginImageX, int beginImageY,
                      long gameTime, Context context) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.beginImageX = beginImageX;
        this.beginImageY = beginImageY;
        this.gameTime = gameTime;
        this.context = context;
        calculateIconSize();
    }

    /**
     * @return 游戏的总时间
     */
    public long getGameTime() {
        return gameTime;
    }

    /**
     * @return LinkPiece[][]数组第一维的长度
     */
    public int getXSize() {
        return xSize;
    }

    /**
     * @return LinkPiece[][]数组第二维的长度
     */
    public int getYSize() {
        return ySize;
    }

    /**
     * @return Board中第一张图片出现的x坐标
     */
    public int getBeginImageX() {
        return beginImageX;
    }

    /**
     * @return Board中第一张图片出现的y坐标
     */
    public int getBeginImageY() {
        return beginImageY;
    }

    /**
     * @return 应用上下文
     */
    public Context getContext() {
        return context;
    }
}
