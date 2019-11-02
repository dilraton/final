package com.example.dell.linkgame.impl;

import java.util.ArrayList;
import java.util.List;

import com.example.dell.linkgame.board.AbstractBoard;
import com.example.dell.linkgame.utils.GameConfig;
import com.example.dell.linkgame.view.LinkPiece;

/**
 * 创建竖的游戏区域，竖向排列的方块以垂直的空列分隔开
 */
public class VerticalBoard extends AbstractBoard {
	@Override
	protected List<LinkPiece> createLinkPieces(GameConfig config, LinkPiece[][] linkPieces) {
		// 创建一个LinkPiece集合, 该集合里面存放初始化游戏时所需的LinkPiece对象
		List<LinkPiece> notNullLinkPieces = new ArrayList<LinkPiece>();
		for (int i = 0; i < linkPieces.length; i++) {
			for (int j = 0; j < linkPieces[i].length; j++) {
				// 加入判断, 符合一定条件才去构造LinkPiece对象, 并加到集合中
				if (i % 2 == 0) {
					// 如果x能被2整除, 即单数列不会创建方块
					// 先构造一个LinkPiece对象, 只设置它在LinkPiece[][]数组中的索引值，
					// 所需要的LinkPieceImage由其父类负责设置。
					LinkPiece linkPiece = new LinkPiece(i, j);
					// 添加到LinkPiece集合中
					notNullLinkPieces.add(linkPiece);
				}
			}
		}
		return notNullLinkPieces;
	}
}
