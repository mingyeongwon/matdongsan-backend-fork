package com.mycompany.matdongsan.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.mycompany.matdongsan.dto.Favorite;

@Mapper
public interface FavoriteDao {
	
	// 좋아요 여부
	public int existsFavorite(int pnumber, int userNumber);
	
	// 좋아요 추가
	public void addLikeButton(Favorite favorite);
	
	// 좋아요 취소
	public void cancelLikeButton(int pnumber, int userNumber);
	
	// 좋아요 리스트
	public List<Favorite> getUserFavoriteList(int unumber);

}
