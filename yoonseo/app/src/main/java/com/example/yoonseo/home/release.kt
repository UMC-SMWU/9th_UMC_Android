package com.example.yoonseo.home

import androidx.annotation.DrawableRes
import com.example.yoonseo.R

enum class Region {ALL, KOREA, GLOBAL}

data class ReleaseAlbum (
    val id: String,
    val title: String,
    val artist: String,
    @DrawableRes val coverImg: Int,
    val region: Region
)

object ReleaseRepository {
    fun loadToday(): List<ReleaseAlbum> = listOf(
        ReleaseAlbum("1", "Lost corner", "Kenshi Yonezu", R.drawable.img_album_exp2, Region.GLOBAL),
        ReleaseAlbum("2", "Fascination", "Moon (혜원) & Tsuyoshi Yamamoto", R.drawable.fascination_moon, Region.ALL),
        ReleaseAlbum("3", "Blossom", "아이유 (IU)", R.drawable.img_album_exp, Region.KOREA),
        ReleaseAlbum("4", "Lost corner", "Kenshi Yonezu", R.drawable.img_album_exp3, Region.GLOBAL),
        ReleaseAlbum("5", "밤하늘", "NewJeans", R.drawable.img_album_exp2, Region.KOREA),
        ReleaseAlbum("6", "愛を伝えたいだとか", "Aimyon", R.drawable.img_album_exp2, Region.GLOBAL)
    )
}