package com.example.dell.linkgame.board;

import com.example.dell.linkgame.utils.LinkPointsInfo;
import com.example.dell.linkgame.view.LinkPiece;

import java.util.List;

/**
 * 游戏逻辑接口
 */
public interface GameService {
    /**
     * 游戏初始化
     * @param theme 游戏主题
     * @param mode 游戏模式
     */
    public void start(String theme,int mode);

    /**
     * 获取存放方块对象的二维数组
     * @return 存放方块对象的二维数组
     */
    public LinkPiece[][] getLinkPieces();

    /**
     * 判断LinkPiece[][]数组中是否还存在非空的LinkPiece对象
     * @return 若还有LinkPiece对象返回true, 否则返回false
     */
    public boolean hasLinkPieces();

    /**
     * 根据鼠标的x坐标和y坐标, 查找出对应的LinkPiece对象
     * @param touchX 鼠标点击的x坐标
     * @param touchY 鼠标点击的y坐标
     * @return 对应的LinkPiece对象, 若没有返回null
     */
    public LinkPiece findLinkPiece(float touchX, float touchY);

    /**
     * 自动获取值相同，位置不同的两个方块对象
     * @return 返回LinkPiece对象列表
     */
    public List<LinkPiece> autoLink();

    /**
     * 判断两个LinkPiece是否可以连接, 返回LinkPointsInfo对象
     * @param p1 第一个LinkPiece对象
     * @param p2 第二个LinkPiece对象
     * @return 若可以相连，返回LinkPointsInfo对象, 若两个LinkPiece不可以连接, 返回null
     */
    public LinkPointsInfo link(LinkPiece p1, LinkPiece p2);

    /**
     * 自动获取一对可连接的方块对象
     * @return LinkPiece列表
     */
    public List<LinkPiece> autoFindCueLink();

    /**
     * 判断当前游戏是否出现死局，即区域中是否还有可连接的方块
     * @return 出现死局返回true，否则返回false
     */
    public boolean IsDie();

}
