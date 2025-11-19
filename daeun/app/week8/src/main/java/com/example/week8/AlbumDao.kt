package com.example.week8

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AlbumDao{
    @Insert
    fun insert(album: Album)
    @Delete
    fun delete(album: Album)
    @Update
    fun update(album: Album)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums(): List<Album>

    @Insert
    fun likeAlbum(like: Like)

    // 해당 유저가 좋아요를 눌렀는 지 아닌지 확인하는 함수
    @Query("SELECT id FROM LikeTable WHERE userId=:userId AND albumId=:albumId")
    fun isLikedAlbum(userId: Int?, albumId: Int): Int?

    @Query("DELETE FROM LikeTable WHERE userId=:userId AND albumId=:albumId")
    fun disLikedAlbum(userId: Int?, albumId: Int)

    @Query("SELECT AT.* FROM liketable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId =:userId")
    fun getLikedAlbums(userId: Int): List<Album>
}