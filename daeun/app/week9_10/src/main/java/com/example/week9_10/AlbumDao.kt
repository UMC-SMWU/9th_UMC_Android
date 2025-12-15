package com.example.week9_10

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.week9_10.data.entities.Album
import com.example.week9_10.data.entities.Like

@Dao
interface AlbumDao{
    @Insert
    fun insertAlbum(album: Album):Unit
//    @Update
//    fun updateAlbum(album: Album):Unit
    @Query("DELETE FROM AlbumTable")
    fun clearAlbums()
    @Query("SELECT * FROM AlbumTable")
    fun getAlbums(): List<Album>
    @Insert
    fun likeAlbum(like: Like): Unit
    @Query("SELECT id FROM LikeTable WHERE userId=:userId AND albumId=:albumId")
    fun isLikedAlbum(userId: Int?, albumId: Int?): Int?
    @Query("DELETE FROM LikeTable WHERE userId=:userId AND albumId=:albumId")
    fun disLikedAlbum(userId: Int?, albumId: Int?)
    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId=:userId")
    fun getLikedAlbums(userId: Int): List<Album>
}