package com.example.week7

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Album::class, Song::class], version = 1)
abstract class AlbumDatabase: RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun songDao(): SongDao

    companion object{
        private var instance: AlbumDatabase? = null
        @Synchronized
        fun getInstance(context: Context): AlbumDatabase?{
            if(instance == null){
                synchronized(AlbumDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AlbumDatabase::class.java,
                        "album-table"
                    ).allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance
        }
    }
}