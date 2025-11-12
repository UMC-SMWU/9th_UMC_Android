package com.example.week7

import android.content.Context
import androidx.room.*


@Database(entities = [Song::class], version = 3) // 이거 계속 늘어나면 어떻게 줄이지
abstract class SongDatabase: RoomDatabase(){
    abstract fun songDao(): SongDao

    companion object{
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase?{
            if(instance == null){
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    ).fallbackToDestructiveMigration() // 코드 업데이트에 따른 데이터베이스 변경
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return instance
        }
    }
}