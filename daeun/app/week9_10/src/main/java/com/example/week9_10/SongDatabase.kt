package com.example.week9_10

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.week9_10.data.entities.Album
import com.example.week9_10.data.entities.Like
import com.example.week9_10.data.entities.Song
import com.example.week9_10.data.entities.User

@Database(entities = [Song::class, User::class, Like::class, Album::class], version = 2)
abstract class SongDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun userDao(): UserDao
    abstract fun albumDao(): AlbumDao

    companion object{
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if(instance == null){
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    ).allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance
        }
    }
}