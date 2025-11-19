package com.example.yoonseo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Song::class], version = 2)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao

    companion object {
        private var instance: SongDatabase? = null

        // Migration 정의
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // coverImg를 NULL이 아닌 0으로 업데이트
                database.execSQL("UPDATE SongTable SET coverImg = 0 WHERE coverImg IS NULL")
            }
        }

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if (instance == null) {
                synchronized(SongDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    )
                        .addMigrations(MIGRATION_1_2)
                        .fallbackToDestructiveMigration()  // 실패 시 DB 재생성
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return instance
        }
    }
}