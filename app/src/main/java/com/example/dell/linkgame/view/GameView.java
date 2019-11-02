package com.example.dell.linkgame.view;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.dell.linkgame.board.GameService;
import com.example.dell.linkgame.utils.GameConfig;
import com.example.dell.linkgame.utils.ImageUtil;
import com.example.dell.linkgame.utils.LinkPointsInfo;

/**
 * 游戏主界面
 */
public class GameView extends View {

    private GameService gameService;      // 游戏逻辑的实现类
    private LinkPiece selectedLinkPiece; //当前已被选中的方块
    private LinkPointsInfo linkPointsInfo;        //连接点信息

    private Paint paint; //画笔
    private Paint paintC;//圆角边框画笔
    private Bitmap selectImage; //选中的图片对象
    private static String TAG ="GameView";
    /**
     * 构造方法
     * @param context
     * @param attrs
     */
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
        this.paint.setColor(Color.RED); // 设置连接线的颜色
        this.paint.setStrokeWidth(3);// 设置连接线的粗细

        this.selectImage = ImageUtil.getSelectImage(context); // 初始化被选中的图片
    }

    /**
     * 设置连接信息对象
     * @param linkPointsInfo
     */
    public void setLinkPointsInfo(LinkPointsInfo linkPointsInfo) {
        this.linkPointsInfo = linkPointsInfo;
    }

    /**
     * 设置当前选中方块
     * @param linkPiece
     */
    public void setSelectedLinkPiece(LinkPiece linkPiece) {
        this.selectedLinkPiece = linkPiece;
    }

    /**
     * 设置游戏逻辑的实现类
     * @param gameService
     */
    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    /* 绘制图形*/
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.gameService == null)
            return;
        LinkPiece[][] linkPieces = gameService.getLinkPieces();
        if (linkPieces != null) {
            for (int i = 0; i < linkPieces.length; i++) { // 遍历LinkPieces[][]二维数组
                for (int j = 0; j < linkPieces[i].length; j++) {
                    if (linkPieces[i][j] != null) { // 如果二维数组中该元素不为空，即有方块，将这个方块的图片画出来
                        this.paintC = new Paint();
                        this.paintC.setAntiAlias(true); //设置画笔为无锯齿
                        this.paintC.setColor(Color.GRAY);
                        this.paintC.setStyle(Paint.Style.STROKE);  //空心效果
                        LinkPiece linkPiece = linkPieces[i][j];

                        RectF rect = new RectF(linkPiece.getBeginX()+2, linkPiece.getBeginY()+2, linkPiece.getBeginX()+ GameConfig.PIECE_WIDTH-2, linkPiece.getBeginY()+GameConfig.PIECE_HEIGHT-2);
                        canvas.drawRoundRect(rect, 5, 5, paintC);
                        paintC.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                        canvas.drawBitmap(linkPiece.getLinkPieceImage().getImage(), linkPiece.getBeginX(), linkPiece.getBeginY(), null);// 根据方块左上角X、Y坐标绘制方块

                    }
                }
            }
        }

        if (this.linkPointsInfo != null) {// 如果当前对象中有linkInfo对象, 即连接信息
            drawLine(this.linkPointsInfo, canvas);// 绘制连接线
            this.linkPointsInfo = null;// 处理完后清空linkInfo对象
        }

        if (this.selectedLinkPiece != null) { // 画选中方块的标识图片
            canvas.drawBitmap(this.selectImage, this.selectedLinkPiece.getBeginX(), this.selectedLinkPiece.getBeginY(), null);
        }
    }

    /**
     * 根据LinkInfo绘制连接线
     * @param linkPointsInfo 连接信息对象
     * @param canvas 画布
     */
    private void drawLine(LinkPointsInfo linkPointsInfo, Canvas canvas) {
        List<Point> points = linkPointsInfo.getLinkPoints(); // 获取LinkInfo中封装的所有连接点
        for (int i = 0; i < points.size() - 1; i++) {// 依次遍历linkInfo中的每个连接点
            Point currentPoint = points.get(i);  // 获取当前连接点
            Point nextPoint = points.get(i + 1); //获取下一个连接点
            canvas.drawLine(currentPoint.x, currentPoint.y, nextPoint.x, nextPoint.y, this.paint);// 绘制连线
        }
    }

    // 开始游戏方法
    public void startGame(String theme,int mode) {
        this.gameService.start(theme,mode);
        this.postInvalidate();
    }
}

