package com.example.week7

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface AlbumDao{
    @Insert
    fun insert(album: Album)
    @Delete
    fun delete(album: Album)
    @Update
    fun update(album: Album)
}