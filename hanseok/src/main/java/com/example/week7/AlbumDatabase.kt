package com.example.week7

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlbumTypeConverters {
    @TypeConverter
    fun fromSongArrayList(value: ArrayList<Song>?): String? {
        return if (value == null) null else Gson().toJson(value)
    }

    @TypeConverter
    fun toSongArrayList(value: String?): ArrayList<Song>? {
        return if (value == null) null else Gson().fromJson(value, object : TypeToken<ArrayList<Song>>() {}.type)
    }
}

@Database(entities = [Album::class], version = 1)
@TypeConverters(AlbumTypeConverters::class)
abstract class AlbumDatabase: RoomDatabase(){
    abstract fun albumDao(): AlbumDao

    companion object{
        private var instance: AlbumDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AlbumDatabase?{
            if(instance == null){
                synchronized(AlbumDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AlbumDatabase::class.java,
                        "album-database"
                    ).fallbackToDestructiveMigration() // 코드 업데이트에 따른 데이터베이스 변경
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return instance
        }
    }
}