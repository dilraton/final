package com.example.dell.linkgame.impl;

import java.util.ArrayList;
import java.util.List;

import com.example.dell.linkgame.board.AbstractBoard;
import com.example.dell.linkgame.utils.GameConfig;
import com.example.dell.linkgame.view.LinkPiece;

/**
 * 创建横的游戏区域
 */
public class HorizontalBoard extends AbstractBoard {
	@Override
	protected List<LinkPiece> createLinkPieces(GameConfig config, LinkPiece[][] linkPieces) {
		// 创建一个LinkPiece集合, 该集合里面存放初始化游戏时所需的LinkPiece对象
		List<LinkPiece> notNullLinkPieces = new ArrayList<LinkPiece>();
		for (int i = 1; i < linkPieces.length-1; i++) {
			for (int j = 0; j < linkPieces[i].length; j++) {
				// 加入判断, 符合一定条件才去构造LinkPiece对象, 并加到集合中
				if (j % 2 == 0) {
					// 如果x能被2整除, 即单数行不会创建方块
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
