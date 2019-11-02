package com.example.dell.linkgame.impl;

import java.util.ArrayList;
import java.util.List;

import com.example.dell.linkgame.board.AbstractBoard;
import com.example.dell.linkgame.utils.GameConfig;
import com.example.dell.linkgame.view.LinkPiece;

/**
 * 创建矩阵分布的游戏区域，矩阵排列的方块会填满二维数组的每个数组元素，仅把四周留空
 */
public class FullBoard extends AbstractBoard {
	@Override
	protected List<LinkPiece> createLinkPieces(GameConfig config, LinkPiece[][] linkPieces) {
		// 创建一个Piece集合, 该集合里面存放初始化游戏时所需的Piece对象
		List<LinkPiece> notNullLinkPieces = new ArrayList<LinkPiece>();
		for (int i = 1; i < linkPieces.length - 1; i++) {
			for (int j = 1; j < linkPieces[i].length - 1; j++) {
				LinkPiece linkPiece = new LinkPiece(i, j);// 先构造一个LinkPiece对象, 只设置非空方块在LinkPiece[][]数组中的索引值，所需要的LinkPieceImage由其父类负责设置
				notNullLinkPieces.add(linkPiece); // 添加到非空LinkPiece集合中
			}
		}
		return notNullLinkPieces;
	}

}
