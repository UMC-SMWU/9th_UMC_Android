package com.example.week7

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.week7.Album
import com.example.week7.databinding.ActivityWeek7Binding
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.gson.Gson
import java.util.Timer

import kotlin.jvm.java

class Week7 : AppCompatActivity() {
    private lateinit var binding: ActivityWeek7Binding
    private var mediaPlayer: MediaPlayer? = null
    lateinit var timer : Timer
    var nowPos = 0
    private var song:Song = Song()
    private var gson: Gson = Gson()
    private var flag_return_songActivity = false
    var songs = arrayListOf<Song>()
//    lateinit var songDB: SongDatabase
    private val database = Firebase.database



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityWeek7Binding.inflate(layoutInflater)
        setContentView(binding.root)
//        inputDummySongs()
//        inputDummyAlbums()
        initDummyData()
        initClickListener()

        Log.d("Song",song.title + song.singer)

        // 메인 플레이어 눌렀을 때 인텐트에 정보 담아서 보내기
        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId", song.id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        // BottomNavView에 프래그먼트 할당하기
        binding.mainBnv.setOnItemSelectedListener { menuItem ->
            changeFragment(
                when (menuItem.itemId) {
                    R.id.homeFragment -> HomeFragment()
                    else -> LockerFragment()
                }
            )
            true
        }

        // 일단 시작은 홈 프래그먼트를 띄우기
        supportFragmentManager.beginTransaction()
            .replace(binding.mainFrm.id, HomeFragment())
            .commit()

    }

    private fun initDummyData(){
        val spf = getSharedPreferences("database", MODE_PRIVATE)
        val isFirstLoading = spf.getBoolean("isFirstLoading", true)
        if(isFirstLoading){
            inputDummyDataToFirebase()
            spf.edit().putBoolean("isFirstLoading", false).apply()
        }
    }

    private fun inputDummyDataToFirebase() {
        val songsRef = database.getReference("songs")
        val albumsRef = database.getReference("albums")

        val dummySongs = arrayListOf(
            Song(0, "Lilac", "아이유 (IU)", 0, 200, false, "music_lilac", R.drawable.img_album_exp2, false, 1),
            Song(1, "Flu", "아이유 (IU)", 0, 200, false, "music_flu", R.drawable.img_album_exp2, false, 1),
            Song(2, "Butter", "방탄소년단 (BTS)", 0, 190, false, "music_butter", R.drawable.img_album_exp, false, 0),
            Song(3, "Next Level", "에스파 (AESPA)", 0, 210, false, "music_next", R.drawable.img_album_exp3, false, 3),
            Song(4, "Boy with Luv", "방탄소년단", 0, 230, false, "music_lilac", R.drawable.img_album_exp4, false, 0),
            Song(5, "BBoom BBoom", "모모랜드 (MOMOLAND)", 0, 240, false, "music_bboom", R.drawable.img_album_exp5, false, 1)
        )

        val dummyAlbums = arrayListOf(
            Album("방탄소년단 정규집", "방탄소년단", R.drawable.img_album_exp),
            Album("아이유, 모모랜드 정규집", "아이유", R.drawable.img_album_exp2),
            Album("에스파 정규집", "에스파", R.drawable.img_album_exp3),
            Album("태연 정규집", "태연", R.drawable.img_album_exp6)
        )

        songsRef.setValue(dummySongs)
        albumsRef.setValue(dummyAlbums)
    }

    private fun initClickListener(){
        // 일시정지, 재생버튼 토글
        binding.mainMiniplayerBtn.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.mainPauseBtn.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.miniplayerPreviousBtn.setOnClickListener {
            moveSong(-1)
        }

        binding.miniplayerNextBtn.setOnClickListener {
            moveSong(1)
        }
    }
    private fun setPlayerStatus(isPlaying : Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.mainMiniplayerBtn.visibility = View.GONE
            binding.mainPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else{
            binding.mainMiniplayerBtn.visibility = View.VISIBLE
            binding.mainPauseBtn.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }

    private fun moveSong(direct: Int){
        if(nowPos + direct < 0){
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos + direct >= songs.size){
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }
        nowPos += direct

        timer.interrupt()
        startTimer()
        mediaPlayer?.release()
        mediaPlayer = null
        setPlayer(songs[nowPos])
    }

    private fun initPlayList(){
//        songDB = SongDatabase.getInstance(this)!!
//        songs.clear()
//        songs.addAll(songDB.songDao().getSongs())

        val songsRef = database.getReference("songs")
        songsRef.get().addOnSuccessListener { dataSnapshot ->
            songs.clear()
            for (songSnapshot in dataSnapshot.children) {
                val song = songSnapshot.getValue(Song::class.java)
                song?.let {
                    songs.add(it)
                }
            }
            Log.d("Firebase", "Songs 로드 성공: ${songs.size}개")
            // songs 리스트가 채워진 후 initSong() 호출
            initSong()
        }.addOnFailureListener {
            Log.e("Firebase", "Songs 로드 실패", it)
        }
    }

    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int) : Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId) return i
        }
        return 0
    }

    private fun setPlayer(song : Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer

        binding.songProgressSb.progress = (song.second/song.playTime) * 1000
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        if (music == 0) {
            Log.e("Week7", "Resource not found: ${song.music}")
        } else {
            mediaPlayer = MediaPlayer.create(this, music)
            mediaPlayer?.seekTo(song.second * 1000)
        }

        setPlayerStatus(song.isPlaying)
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying : Boolean = true) : Thread(){
        private var second : Int = 0
        private var mills : Float = 0f

        override fun run(){
            super.run()

            try{
                while(true){
                    if(second >= playTime) break;
                    if(isPlaying){
                        sleep(50)
                        mills += 50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime) * 100).toInt()
                        }
                    }
                }
            }catch(e: InterruptedException){
                Log.d("Song", "쓰레드가 죽었습니다. ${e.message}")
            }

        }
    }


    private fun changeFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(binding.mainFrm.id, fragment)
            .commitAllowingStateLoss()

    }
    fun setPlayerData(album: Album) {
        binding.mainMiniplayerTitleTv.text = album.title
        binding.mainMiniplayerSingerTv.text = album.singer
        // 그다음에 노래 재생 현황같은것도 넘겨줄수 있어야함
        binding.mainMiniplayerBtn.visibility = View.GONE
        binding.mainPauseBtn.visibility = View.VISIBLE
    }

    override fun onStart(){
        super.onStart()
        initPlayList()
        // initSong() // initPlayList의 비동기 콜백 내부로 이동
    }


    override fun onPause(){
        super.onPause()

        if (songs.isNotEmpty()) {
            songs[nowPos].second += ((binding.songProgressSb.progress * songs[nowPos].playTime)/100)/1000
            songs[nowPos].isPlaying = false
            setPlayerStatus(false)

            val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
            val editor = sharedPreferences.edit()

            editor.putInt("songId", songs[nowPos].id)
            editor.apply()

            // songDB.songDao().update(songs[nowPos]) // 242번 줄 주석 처리
            // Firebase에 현재 곡 상태 업데이트
            val songsRef = database.getReference("songs")
            songsRef.child(nowPos.toString()).setValue(songs[nowPos])
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}



//Album 데이터베이스 초기화
//    private fun inputDummyAlbums(){
//        val albumDB = AlbumDatabase.getInstance(this)!!
//        val albums = albumDB.albumDao().getAlbums()
//
//        if(albums.isNotEmpty()) return
//
//        // 이 부분에서 song데이터 베이스를 돌면서 해당하는 albumIdx의 song은 해당 앨범의 songs속성인 array에다가 추가하는 방식으로 구현했으면 됐는데
//        // 문제 읽을때는 뭔소린지 모르다가 다 짜고나니까 이해가 되어버렸어요
//        albumDB.albumDao().insert(
//            Album("방탄소년단 정규집", "방탄소년단", R.drawable.img_album_exp,
//                arrayListOf(songs[2], songs[4])
//            ))
//        albumDB.albumDao().insert(
//            Album("아이유, 모모랜드 정규집", "아이유", R.drawable.img_album_exp2,
//                arrayListOf(songs[0], songs[1], songs[5]))
//        )
//        albumDB.albumDao().insert(
//            Album("에스파 정규집", "에스파", R.drawable.img_album_exp3,
//                arrayListOf(songs[3]))
//        )
//
//
//        albumDB.albumDao().insert(
//            Album("태연 정규집", "태연", R.drawable.img_album_exp6)
//
//        )
//
//    }
//    private fun inputDummySongs(){
//        initPlayList()
//        if(songs.isNotEmpty()) return
//
//        songDB.songDao().insert(
//            Song(
//                "Lilac",
//                "아이유 (IU)",
//                0,
//                200,
//                false,
//                "music_lilac",
//                R.drawable.img_album_exp2,
//                false,
//                1,
//            )
//        )
//
//        songDB.songDao().insert(
//            Song(
//                "Flu",
//                "아이유 (IU)",
//                0,
//                200,
//                false,
//                "music_flu",
//                R.drawable.img_album_exp2,
//                false,
//                1,
//            )
//        )
//
//        songDB.songDao().insert(
//            Song(
//                "Butter",
//                "방탄소년단 (BTS)",
//                0,
//                190,
//                false,
//                "music_butter",
//                R.drawable.img_album_exp,
//                false,
//                0,
//            )
//        )
//
//        songDB.songDao().insert(
//            Song(
//                "Next Level",
//                "에스파 (AESPA)",
//                0,
//                210,
//                false,
//                "music_next",
//                R.drawable.img_album_exp3,
//                false,
//                3,
//            )
//        )
//
//
//        songDB.songDao().insert(
//            Song(
//                "Boy with Luv",
//                "방탄소년단",
//                0,
//                230,
//                false,
//                "music_lilac",
//                R.drawable.img_album_exp4,
//                false,
//                0,
//
//            )
//        )
//
//
//        songDB.songDao().insert(
//            Song(
//                "BBoom BBoom",
//                "모모랜드 (MOMOLAND)",
//                0,
//                240,
//                false,
//                "music_bboom",
//                R.drawable.img_album_exp5,
//                false,
//                1,
//            )
//        )
//
//        val _songs = songDB.songDao().getSongs()
//        Log.d("DB data", _songs.toString())
//    }