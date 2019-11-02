package com.example.dell.linkgame.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.graphics.Point;
import android.util.Log;

import com.example.dell.linkgame.board.AbstractBoard;
import com.example.dell.linkgame.board.GameService;
import com.example.dell.linkgame.utils.GameConfig;
import com.example.dell.linkgame.utils.LinkPointsInfo;
import com.example.dell.linkgame.view.LinkPiece;

/**
 * 游戏逻辑的实现类
 */
public class GameServiceImpl implements GameService {

	private LinkPiece[][] linkPieces; //方块数组
	private GameConfig config;    //游戏配置对象
    private String TAG = "GameServiceImpl";
	/**
	 * 构造方法，将游戏配置对象设置在本类中
	 *@param config 游戏配置对象
	 */
	public GameServiceImpl(GameConfig config) {
		Log.i("GameServiceImpl", "GameServiceImpl Pre" );
		this.config = config;
		Log.i("GameServiceImpl", "GameServiceImpl Post:" + config );
	}

	@Override
	//游戏初始化
	public void start(String theme,int mode) {
		AbstractBoard board = null;  // 定义一个AbstractBoard对象
		switch (mode) { // 随机生成AbstractBoard的子类实例
			case 1:// 1返回HorizontalBoard(横向)
				board = new HorizontalBoard();
				break;
			case 2: //2返回VerticalBoard(竖向)
				board = new VerticalBoard();
				break;
			default:// 默认返回FullBoard
				board = new FullBoard();
				break;
		}
		// 初始化LinkPiece[][]数组
		this.linkPieces = board.create(config,theme);
	}

	@Override
	//获取存放方块对象的二维数组
	public LinkPiece[][] getLinkPieces() {
		return this.linkPieces;
	}

	@Override
	//判断LinkPiece[][]数组中是否还存在非空的LinkPiece对象
	public boolean hasLinkPieces() {
		for (int i = 0; i < linkPieces.length; i++) {// 遍历LinkPiece[][]数组的每个元素
			for (int j = 0; j < linkPieces[i].length; j++) {
				if (linkPieces[i][j] != null) {// 只要任意一个数组元素不为null，就还剩有非空的LinkPiece对象
					return true;
				}
			}
		}
		return false;
	}

	@Override
	//根据鼠标触碰点的位置查找相应的方块对象
	public LinkPiece findLinkPiece(float touchX, float touchY) {

		int relativeX = (int) touchX - this.config.getBeginImageX();//此处的touchX和touchY对于每个方块来说均加了GameConfig中的beginImageX、beginImageY值
		int relativeY = (int) touchY - this.config.getBeginImageY();

		if (relativeX < 0 || relativeY < 0) {//如果鼠标点击的地方比游戏区域中第一张图片的开始X坐标或Y坐标要小, 即没有找到相应的方块
			return null;
		}
		int indexX = getIndex(relativeX, GameConfig.PIECE_WIDTH); //获取relativeX坐标对应的方块在LinkPiece[][]数组中第一维的索引值
		int indexY = getIndex(relativeY, GameConfig.PIECE_HEIGHT);//获取relativeY坐标对应的方块在LinkPiece[][]数组中的第二维的索引值

		if (indexX < 0 || indexY < 0) { // 若这两个索引比数组的最小索引还小, 返回null
			return null;
		}
		if (indexX >= this.config.getXSize() || indexY >= this.config.getYSize()) { // 若这两个索引大于等于数组的最大索引, 返回null
			return null;
		}

		return this.linkPieces[indexX][indexY]; // 若找到，返回对应的LinkPiece[][]数组的指定元素
	}

	/**
	 * 工具方法：计算坐标相对于LinkPiece[][]数组的第一维或第二维的索引值
	 * @param relative 坐标
	 * @param size 每张图片边的长或宽
	 * @return relative坐标在LinkPiece数组中相应维度的索引值
	 */
	private int getIndex(int relative, int size) {

		int index = -1; // 初始化表示坐标relative对应的方块不在该数组中（数组下标从0开始）
		if (relative % size == 0) {
			index = relative / size - 1;
		} else {
			index = relative / size;
		}
		return index;
	}

	@Override
	//自动获取值相同，位置不同的两个方块对象的连接信息
	public List<LinkPiece> autoLink() {
		LinkPiece p1;
		LinkPiece p2;
		List<LinkPiece> list = new ArrayList<>();
		for (int i = 0; i < linkPieces.length; i++) { //将游戏区域中第一个不为空的方块作为p1
			for (int j = 0; j < linkPieces[i].length; j++) {
				if (linkPieces[i][j] != null) {
					p1 = linkPieces[i][j];
					for (int col = j+1; col < linkPieces[0].length; col++) { //寻找与p1同行不同列的位置上是否有值相同的方块p2
						if (linkPieces[i][col] != null) {
							p2 = linkPieces[i][col];
						}else
							continue;
						if (p1.isSameImage(p2)) {//如果p1和p2的值相同，则返回其连接信息，否则继续寻找
							list.add(p1);
							list.add(p2);
							return list;
						}
					}
					for (int row = i+1; row < linkPieces.length; row++) { //在与p1不同行上寻找与p1值相同的方块p2
						for (int col = 0; col < linkPieces[row].length; col++) {
							if (linkPieces[row][col] != null) {
								p2 = linkPieces[row][col];
								Log.i(TAG, "p2.X：" + p2.getIndexX() + ",p2.Y:" + p2.getIndexY());
							}else
								continue;
							if (p1.isSameImage(p2)) {//如果p1和p2的值相同，则返回其连接信息，否则继续寻找
								list.add(p1);
								list.add(p2);
								return list;
							}
						}
					}
				}

			}
		}
		return list;
	}

	@Override
	public List<LinkPiece> autoFindCueLink(){
		List<LinkPiece> list = new ArrayList<>();
		LinkPiece p1;
		LinkPiece p2;
		for (int i = 0; i < linkPieces.length; i++) { //将游戏区域中第一个不为空的方块作为p1
			for (int j = 0; j < linkPieces[i].length; j++) {
				if (linkPieces[i][j] != null) {
					p1 = linkPieces[i][j];
					for (int col = j+1; col < linkPieces[0].length; col++) { //寻找与p1同行不同列的位置上是否有值相同的方块p2
						if (linkPieces[i][col] != null) {
							p2 = linkPieces[i][col];
						}else
							continue;
						if (link(p1,p2) != null) {//如果p1和p2可连接，则返回其连接信息，否则继续寻找
							list.add(p1);
							list.add(p2);
							return list;
						}
					}
					for (int row = i+1; row < linkPieces.length; row++) { //在与p1不同行上寻找与p1值相同的方块p2
						for (int col = 0; col < linkPieces[row].length; col++) {
							if (linkPieces[row][col] != null) {
								p2 = linkPieces[row][col];
							}else
								continue;
							if (link(p1,p2) != null) {//如果p1和p2可连接，则返回其连接信息，否则继续寻找
								list.add(p1);
								list.add(p2);
								return list;
							}
						}
					}
				}

			}
		}
		return list;
	}

	@Override
	//判断是否为死局
	public boolean IsDie(){
		LinkPiece[][] linkPieces2 = new LinkPiece[linkPieces.length][linkPieces[0].length]; //存放待判断是否可连接的方块
		LinkPiece p1;
		LinkPiece p2;
		for(int i = 0;i < linkPieces.length;i++){
			for(int j = 0;j<linkPieces[i].length;j++){
				linkPieces2[i][j] = linkPieces[i][j];
			}
		}
		for (int row = 0; row < linkPieces2.length; row++){
			for (int col = 0; col < linkPieces2[row].length; col++) {
				if (linkPieces2[row][col] != null){//如果该方块没被消除，记为p1,继续寻找下一个没被消除的方块p2
					p1 = linkPieces2[row][col];
					for (int i = row; i < linkPieces2.length; i++){
						for (int j = 0; j < linkPieces2[i].length; j++){
							if (linkPieces2[i][j] != null) {
                                p2 = linkPieces2[i][j];
                            }else
								continue;
							if (link(p1, p2)!= null){// 如果p1和p2都找到了，则判断其是否可连接，若可连接，则不是死局
								linkPieces2[p1.getIndexX()][p1.getIndexY()] = null;
								linkPieces2[p2.getIndexX()][p2.getIndexY()] = null;
								return false;
							}
						}
					}
				}
			}
		}
		// 如果遍历结束后，仍未找到可连接的一对方块，则返回是死局
		return true;
	}

	@Override
	//判断两个LinkPiece是否可以连接
	public LinkPointsInfo link(LinkPiece p1, LinkPiece p2) {

		if (p1.equals(p2)) {//若两个LinkPiece是同一个, 返回null
			return null;
		}

		if (!p1.isSameImage(p2)) { //若p1的图片与p2的图片不相同, 则返回null
			return null;
		}

		if (p2.getIndexX() < p1.getIndexX()) {//若p2在p1的左边, 则将两个参数互换重新执行本方法
			return link(p2, p1);
		}
		Point Point1 = p1.getCenter(); //获取p1的中心点
		Point Point2 = p2.getCenter(); //获取p2的中心点

		if (p1.getIndexY() == p2.getIndexY()) { //情形1：如果两个方块在同一行，并且可以直接相连
			if (!isHorizonBlock(Point1, Point2, GameConfig.PIECE_WIDTH)) {// 若两点之间没有障碍，返回连接点信息
				return new LinkPointsInfo(Point1, Point2);
			}
		}

		if (p1.getIndexX() == p2.getIndexX()) { // 情形2：如果两个方块在同一列，并且可以直接相连
			if (!isVerticalBlock(Point1, Point2, GameConfig.PIECE_HEIGHT)) { //若两点之间没有障碍,返回连接点信息
				return new LinkPointsInfo(Point1, Point2);
			}
		}

		//情形3：如果两个方块可以以两条线段相连，即有一个转折点
		Point cornerPoint = getCornerPoint(Point1,Point2, GameConfig.PIECE_WIDTH, GameConfig.PIECE_HEIGHT); //获取两个点之间直角相连的转折点
		if (cornerPoint != null) { // 若两点之间有一个转折点，返回连接点信息
			return new LinkPointsInfo(Point1, cornerPoint, Point2);
		}

		//情形4：如果两个方块可以以三条线段相连，即有两个转折点
		Map<Point, Point> turns = getLinkPoints(Point1, Point2, GameConfig.PIECE_WIDTH, GameConfig.PIECE_WIDTH);// 该map的key存放第一个转折点,value存放第二个转折点,map的size()说明有多少种可以连的方式
		if (turns.size() != 0) {// 若两点之间有转折点
			return getShortcut(Point1, Point2, turns, getDistance(Point1, Point2)); // 获取p1和p2之间最短的连接信息
		}
		return null;
	}

	/**
	 * 获取两个转折点
	 * @param point1
	 * @param point2
	 * @return Map对象的每个键值对代表一种连接方式， 其中key、value分别代表第1个、第2个连接点
	 */
	private Map<Point, Point> getLinkPoints(Point point1, Point point2, int pieceWidth, int pieceHeight) {
		Map<Point, Point> result = new HashMap<Point, Point>();

		int heightMax = (this.config.getYSize() + 1) * pieceHeight + this.config.getBeginImageY();// 获取游戏面区域的最大高度
		int widthMax = (this.config.getXSize() + 1) * pieceWidth + this.config.getBeginImageX();// 获取游戏区域的最大宽度

		List<Point> p1HorizontalChannel = getHorizontalChannel(point1, 0, widthMax, pieceWidth); //获取p1的水平通道
		List<Point> p1VerticalChannel = getVerticalChannel(point1, 0, heightMax, pieceHeight);   // 获取p1的垂直通道
		List<Point> p2HorizontalChannel = getHorizontalChannel(point2, 0, widthMax, pieceWidth); //获取p2的水平通道
		List<Point> p2VerticalChannel = getVerticalChannel(point2, 0, heightMax, pieceHeight);   // 获取p2的垂直通道

		Map<Point, Point> vLinkPoints = getHorizonLinkPoints(p1VerticalChannel, p2VerticalChannel, pieceHeight);
		Map<Point, Point> hLinkPoints = getVerticalLinkPoints(p1HorizontalChannel, p2HorizontalChannel, pieceWidth);

		result.putAll(vLinkPoints);
		result.putAll(hLinkPoints);

		return result;
	}

	/**
	 * 获取p1和p2之间最短的连接信息
	 * @param p1
	 * @param p2
	 * @param turns 存放转折点的map
	 * @param shortDistance 两点之间的最短距离
	 * @return p1和p2之间最短的连接信息
	 */
	private LinkPointsInfo getShortcut(Point p1, Point p2, Map<Point, Point> turns, int shortDistance) {
		List<LinkPointsInfo> infoList = new ArrayList<LinkPointsInfo>();
		for (Point point1 : turns.keySet()) {// 遍历结果Map
			Point point2 = turns.get(point1);
			// 将转折点与选择点封装成LinkInfo对象, 放到List集合中
			infoList.add(new LinkPointsInfo(p1, point1, point2, p2));
		}
		return getShortInfo(infoList, shortDistance);
	}

	/**
	 * 从infoList中获取连接线最短的那个LinkInfo对象
	 * @param infoList
	 * @return 连接线最短的那个LinkInfo对象
	 */
	private LinkPointsInfo getShortInfo(List<LinkPointsInfo> infoList, int shortDistance) {
		int temp1 = 0;
		LinkPointsInfo result = null;
		for (int i = 0; i < infoList.size(); i++) {
			LinkPointsInfo info = infoList.get(i);
			int distance = countAll(info.getLinkPoints()); // 计算该连接线上的几个点的总距离
			if (i == 0) { // 将循环中第一个info的距离与最短距离的差距用temp1保存
				temp1 = distance - shortDistance;
				result = info;
			}
			if (distance - shortDistance < temp1) { // 如果下一个info的距离与最短距离的差值比temp1还小, 则用当前的值作为temp1
				temp1 = distance - shortDistance;
				result = info;
			}
		}
		return result;
	}

	/**
	 * 计算List<Point>中所有点的距离总和
	 * @param points 需要计算的连接点
	 * @return 所有点的距离的总和
	 */
	private int countAll(List<Point> points) {
		int result = 0;
		for (int i = 0; i < points.size() - 1; i++) {
			// 获取第i个点
			Point point1 = points.get(i);
			// 获取第i + 1个点
			Point point2 = points.get(i + 1);
			// 计算第i个点与第i + 1个点的距离，并添加到总距离中
			result += getDistance(point1, point2);
		}
		return result;
	}

	/**
	 * 获取两个LinkPoint之间的最短距离
	 * @param p1
	 * @param p2
	 * @return 两个点的距离距离总和
	 */
	private int getDistance(Point p1, Point p2) {
		int xDistance = Math.abs(p1.x - p2.x);
		int yDistance = Math.abs(p1.y - p2.y);
		return xDistance + yDistance;
	}

	/**
	 * 获取两个通道集合中的处于同一水平方向上且中间没有障碍的两点
	 * @param p1Chanel
	 * @param p2Chanel
	 * @param pieceWidth
	 * @return 存放可以横向直线连接的连接点的键值对
	 */
	private Map<Point, Point> getHorizonLinkPoints(List<Point> p1Chanel, List<Point> p2Chanel, int pieceWidth) {
		Map<Point, Point> result = new HashMap<Point, Point>();
		for (int i = 0; i < p1Chanel.size(); i++) {
			Point temp1 = p1Chanel.get(i); // 从第一通道中取一个点
			for (int j = 0; j < p2Chanel.size(); j++) { // 再遍历第二个通道, 判断第二通道中是否有点可以与temp1横向相连
				Point temp2 = p2Chanel.get(j);
				if (temp1.y == temp2.y) { // 如果Y坐标相同，即在同一行, 判断它们之间是否有直接障碍
					if (!isHorizonBlock(temp1, temp2, pieceWidth)) {// 若没有障碍，则直接加到结果的map中
						result.put(temp1, temp2);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 获取两个通道集合中的处于同一垂直方向上且中间没有障碍的两点
	 * @param p1Chanel
	 * @param p2Chanel
	 * @param pieceHeight
	 * @return
	 */
	private Map<Point, Point> getVerticalLinkPoints(List<Point> p1Chanel, List<Point> p2Chanel, int pieceHeight) {
		Map<Point, Point> result = new HashMap<Point, Point>();
		for (int i = 0; i < p1Chanel.size(); i++) {
			Point temp1 = p1Chanel.get(i);
			for (int j = 0; j < p2Chanel.size(); j++) {
				Point temp2 = p2Chanel.get(j);
				if (temp1.x == temp2.x) { // 如果X坐标相同，即在同一列，判断它们之间是否有直接障碍
					if (!isVerticalBlock(temp1, temp2, pieceHeight)) { // 若没有障碍, 则直接加到结果的map中
						result.put(temp1, temp2);
					}
				}
			}
		}
		return result;
	}

	/**
	 * 判断水平直线上两点之间是否有障碍
	 * @param p1
	 * @param p2
	 * @param pieceWidth 每个方块图片的宽
	 * @return 两个点之间有障碍返回true，否则返回false
	 */
	private boolean isHorizonBlock(Point p1, Point p2, int pieceWidth) {
		if (p2.x < p1.x) {
			// 如果p2在p1左边, 调换参数位置调用本方法
			return isHorizonBlock(p2, p1, pieceWidth);
		}
		for (int i = p1.x + pieceWidth; i < p2.x; i = i + pieceWidth) {
			if (hasPiece(i, p1.y)) {// 有障碍
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断竖直直线上两点之间是否有障碍
	 * @param p1
	 * @param p2
	 * @param pieceHeight 每个方块图片的高
	 * @return 两个点之间有障碍返回true，否则返回false
	 */
	private boolean isVerticalBlock(Point p1, Point p2, int pieceHeight) {
		if (p2.y < p1.y) {
			// 如果p2在p1的上面, 调换参数位置重新调用本方法
			return isVerticalBlock(p2, p1, pieceHeight);
		}
		for (int i = p1.y + pieceHeight; i < p2.y; i = i + pieceHeight) {
			if (hasPiece(p1.x, i)) {
				// 有障碍
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取两个不在同一行或者同一列的点之间的直角连接点, 即只有一个转折点
	 * @param point1
	 * @param point2
	 * @return 两个不在同一行或者同一列的点之间的直角连接点
	 */
	private Point getCornerPoint(Point point1, Point point2, int pieceWidth, int pieceHeight) {

		int heightMax = (this.config.getYSize() + 1) * pieceHeight + this.config.getBeginImageY();
		int widthMax = (this.config.getXSize() + 1) * pieceWidth + this.config.getBeginImageX();

		List<Point> p1HorizontalChannel = getHorizontalChannel(point1, 0, widthMax, pieceWidth); //获取p1的水平通道
		List<Point> p1VerticalChannel = getVerticalChannel(point1, 0, heightMax, pieceHeight);   // 获取p1的垂直通道
		List<Point> p2HorizontalChannel = getHorizontalChannel(point2, 0, widthMax, pieceWidth); //获取p2的水平通道
		List<Point> p2VerticalChannel = getVerticalChannel(point2, 0, heightMax, pieceHeight);   // 获取p2的垂直通道

		Point linkPoint1 = getWrapPoint(p1HorizontalChannel, p2VerticalChannel);
		Point linkPoint2 = getWrapPoint(p2HorizontalChannel, p1VerticalChannel);

		return (linkPoint1 == null) ? linkPoint2 : linkPoint1;
	}

	/**
	 * 遍历两个通道, 获取它们的交点
	 * @param p1Chanel
	 * @param p2Chanel
	 * @return 两个通道有交点，返回交点，否则返回null
	 */
	private Point getWrapPoint(List<Point> p1Chanel, List<Point> p2Chanel) {
		for (int i = 0; i < p1Chanel.size(); i++) {
			Point temp1 = p1Chanel.get(i);
			for (int j = 0; j < p2Chanel.size(); j++) {
				Point temp2 = p2Chanel.get(j);
				if (temp1.equals(temp2)) {// 若两个List中有同一个元素, 表明这两个通道有交点
					return temp1;
				}
			}
		}
		return null;
	}

	/**
	 * 给一个Point对象,返回它的左边通道
	 * @param p
	 * @param pieceWidth LinkPiece图片的宽
	 * @param min 向左遍历时最左界限
	 * @return 给定Point左边的通道
	 */
	private List<Point> getLeftChannel(Point p, int min, int pieceWidth) {
		List<Point> result = new ArrayList<Point>();
		for (int i = p.x - pieceWidth; i >= min; i = i - pieceWidth) {// 获取向左通道, 由点p向左遍历, 步长为LinkPiece图片的宽
			if (hasPiece(i, p.y)) { // 遇到障碍, 表示通道已经到尽头, 直接返回
				return result;
			}
			result.add(new Point(i, p.y));
		}
		return result;
	}

	/**
	 * 给一个Point对象, 返回它的右边通道
	 * @param p
	 * @param pieceWidth
	 * @param max 向右时的最右界限
	 * @return 给定Point右边的通道
	 */
	private List<Point> getRightChannel(Point p, int max, int pieceWidth) {
		List<Point> result = new ArrayList<Point>();
		for (int i = p.x + pieceWidth; i <= max; i = i + pieceWidth) { // 获取向右通道, 由点p向右遍历, 步长为LinkPiece图片的宽
			if (hasPiece(i, p.y)) { // 遇到障碍, 表示通道已经到尽头, 直接返回
				return result;
			}
			result.add(new Point(i, p.y));
		}
		return result;
	}

	/**
	 * 给一个Point对象, 返回它的水平通道
	 * @param p
	 * @param pieceWidth
	 * @param min 最左界限
	 * @param max 最右界限
	 * @return 给定Point的水平通道
	 */
	private List<Point> getHorizontalChannel(Point p, int min, int max, int pieceWidth) {
		List<Point> result = getLeftChannel(p, min, pieceWidth);
		List<Point> resultRight = getRightChannel(p, max, pieceWidth);
		result.addAll(resultRight);
		return result;
	}

	/**
	 * 给一个Point对象, 返回它的上面通道
	 * @param p
	 * @param min 向上遍历时最上的界限
	 * @param pieceHeight
	 * @return 给定Point上面的通道
	 */
	private List<Point> getUpChannel(Point p, int min, int pieceHeight) {
		List<Point> result = new ArrayList<Point>();
		for (int i = p.y - pieceHeight; i >= min; i = i - pieceHeight) {// 获取向上通道, 由点p向上遍历, 步长为LinkPiece图片的高
			if (hasPiece(p.x, i)) {// 遇到障碍, 表示通道已经到尽头, 直接返回
				return result;
			}
			result.add(new Point(p.x, i));
		}
		return result;
	}

	/**
	 * 给一个Point对象, 返回它的下面通道
	 * @param p
	 * @param max 向下遍历时的最下界限
	 * @return 给定Point下面的通道
	 */
	private List<Point> getDownChannel(Point p, int max, int pieceHeight) {
		List<Point> result = new ArrayList<Point>();
		for (int i = p.y + pieceHeight; i <= max; i = i + pieceHeight) { // 获取向下通道, 由点p向下遍历, 步长为LinkPiece图片的高
			if (hasPiece(p.x, i)) {// 遇到障碍, 表示通道已经到尽头, 直接返回
				return result;
			}
			result.add(new Point(p.x, i));
		}
		return result;
	}

	/**
	 * 给一个Point对象, 返回它的垂直通道
	 * @param p
	 * @param pieceWidth
	 * @param min 最上界限
	 * @param max 最下界限
	 * @return 给定Point的垂直通道
	 */
	private List<Point> getVerticalChannel(Point p, int min, int max, int pieceWidth) {
		List<Point> result = getUpChannel(p, min, pieceWidth);
		List<Point> resultDown = getDownChannel(p, max, pieceWidth);
		result.addAll(resultDown);
		return result;
	}

	/**
	 * 判断游戏区域中（x,y）坐标对应的位置上是否有LinkPiece对象
	 * @param x
	 * @param y
	 * @return 若该坐标上有LinkPiece对象，返回true，否则返回false
	 */
	private boolean hasPiece(int x, int y) {
		if (findLinkPiece(x, y) == null)
			return false;
		return true;
	}
}