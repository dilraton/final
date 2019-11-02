package com.example.dell.linkgame.utils;

import java.util.List;
import java.util.ArrayList;

import android.graphics.Point;

/**
 * 连接信息类
 */
public class LinkPointsInfo {

    private List<Point> points = new ArrayList<Point>(); //保存连接点的集合

    /**
     *  设置第一个构造器, 两个Point可以直接相连, 中间没有转折点
     * @param p1
     * @param p2
     */
    public LinkPointsInfo(Point p1, Point p2) {
        // 将连接点加到集合中
        points.add(p1);
        points.add(p2);
    }

    /**
     *  设置第二个构造器, 三个Point可以相连, p2是p1与p3之间的转折点
     * @param p1
     * @param p2
     * @param p3
     */
    public LinkPointsInfo(Point p1, Point p2, Point p3) {
        points.add(p1);
        points.add(p2);
        points.add(p3);
    }

    /**
     *  设置第三个构造器, 四个Point可以相连, p2, p3是p1与p4的转折点
     * @param p1
     * @param p2
     * @param p3
     * @param p4
     */
    public LinkPointsInfo(Point p1, Point p2, Point p3, Point p4) {
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
    }

    /**
     * 获取连接点集合
     * @return 连接点集合
     */
    public List<Point> getLinkPoints() {
        return points;
    }
}

