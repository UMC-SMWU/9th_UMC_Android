package com.example.yoonseo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Song::class, Album::class, Like::class], version = 3)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun albumDao(): AlbumDao

    companion object {
        private var instance: SongDatabase? = null

        // Migration 1 -> 2
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("UPDATE SongTable SET coverImg = 0 WHERE coverImg IS NULL")
            }
        }

        // Migration 2 -> 3 (Like 테이블 추가)
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // LikeTable 생성
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS LikeTable (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        userId INTEGER NOT NULL,
                        albumId INTEGER NOT NULL
                    )
                    """.trimIndent()
                )

                // Album 테이블에 isLike 컬럼 추가
                database.execSQL("ALTER TABLE AlbumTable ADD COLUMN isLike INTEGER NOT NULL DEFAULT 0")
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
                        .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                        .fallbackToDestructiveMigration()  // 실패 시 DB 재생성
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return instance
        }
    }
}