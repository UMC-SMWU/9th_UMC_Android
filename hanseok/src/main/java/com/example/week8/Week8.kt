package com.example.week8

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.week8.databinding.ActivityWeek7Binding
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.gson.Gson

import kotlin.jvm.java

class Week8 : AppCompatActivity() {
    private lateinit var binding: ActivityWeek7Binding
    private var mediaPlayer: MediaPlayer? = null
    lateinit var timer : Timer
    var nowPos = 0
    private var song:Song = Song()
    var songs = arrayListOf<Song>()
    private val database = FirebaseDatabase.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_FLO)
        binding = ActivityWeek7Binding.inflate(layoutInflater)
        setContentView(binding.root)

        initDummyDataAndPlayList()
        initClickListener()

        Log.d("Song",song.title + song.singer)

        binding.mainPlayerCl.setOnClickListener {
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putString("songId", songs[nowPos].id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        binding.mainBnv.setOnItemSelectedListener { menuItem ->
            changeFragment(
                when (menuItem.itemId) {
                    R.id.homeFragment -> HomeFragment()
                    else -> LockerFragment()
                }
            )
            true
        }

        supportFragmentManager.beginTransaction()
            .replace(binding.mainFrm.id, HomeFragment())
            .commit()

    }

    private fun initDummyDataAndPlayList() {
        val spf = getSharedPreferences("database", MODE_PRIVATE)
        val isFirstLoading = spf.getBoolean("isFirstLoading", true)

        if (isFirstLoading) {
            inputDummyDataToFirebase {
                initPlayList()
                spf.edit().putBoolean("isFirstLoading", false).apply()
                Log.d("Week8", "Firebase 더미 데이터 생성 완료 및 플레이리스트 초기화 시작")
            }
        } else {
            initPlayList()
        }
    }

    private fun inputDummyDataToFirebase(onComplete: () -> Unit) {
        val rootRef = database.reference
        val updates: MutableMap<String, Any?> = HashMap()

        val dummyAlbums = arrayListOf(
            Album(id = "temp_0", title = "아이유 앨범", singer = "아이유 (IU)", coverImg = R.drawable.img_album_exp2, albumIdx = 0),
            Album(id = "temp_1", title = "에스파 앨범", singer = "에스파 (AESPA)", coverImg = R.drawable.img_album_exp3, albumIdx = 1),
            Album(id = "temp_2", title = "방탄소년단 앨범", singer = "방탄소년단 (BTS)", coverImg = R.drawable.img_album_exp4, albumIdx = 2),
            Album(id = "temp_3", title = "태연 앨범", singer = "태연 (TaeYeon)", coverImg = R.drawable.img_album_exp5, albumIdx = 3)
        )

        dummyAlbums.forEach { album ->

            val albumId = rootRef.child("albums").push().key!!
            album.id = albumId


            updates["/albums/$albumId"] = album

            val songsForThisAlbum = getSongsForAlbum(album.albumIdx)
            songsForThisAlbum.forEach { song ->
                val songId = rootRef.child("songs").push().key!!
                song.id = songId
                song.albumIdx = album.albumIdx
                updates["/songs/$songId"] = song
            }
        }

        rootRef.updateChildren(updates)
            .addOnSuccessListener {
                Log.d("Week8", "Firebase 더미 데이터 생성 성공!")
                onComplete()
            }
            .addOnFailureListener {
                Log.e("Week8", "Firebase 더미 데이터 생성 실패", it)
                onComplete()
            }
    }
    private fun getSongsForAlbum(albumIdx: Int): ArrayList<Song> {
        return when(albumIdx) {
            0 -> arrayListOf(
                Song(title = "LILAC", singer = "아이유 (IU)", music = "music_lilac", playTime = 214, coverImg = R.drawable.img_album_exp2),
                Song(title = "FLU", singer = "아이유 (IU)", music = "music_flu", playTime = 188, coverImg = R.drawable.img_album_exp2),
                )
            1 -> arrayListOf(
                Song(title = "Next Level", singer = "에스파 (AESPA)", music = "music_next", playTime = 221, coverImg = R.drawable.img_album_exp3)
            )
            2 -> arrayListOf(
                Song(title = "Butter", singer = "방탄소년단 (BTS)", music = "music_butter", playTime = 164, coverImg = R.drawable.img_album_exp4)
            )
            3 -> arrayListOf(
                Song(title = "Weekend", singer = "태연 (TaeYeon)", playTime = 233, coverImg = R.drawable.img_album_exp5)
            )
            else -> ArrayList()
        }
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
            if (songs.isNotEmpty()) {
                initSong()
            }
        }.addOnFailureListener {
            Log.e("Firebase", "Songs 로드 실패", it)
        }
    }

    private fun initSong(){
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId: String = spf.getString("songId", "") ?: ""
        nowPos = getPlayingSongPosition(songId)

        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: String): Int {
        if (songId == "") return 0
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) return i
        }
        return -1
    }

    private fun setPlayer(song : Song){
        binding.mainMiniplayerTitleTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer

        if (song.playTime > 0) {
            binding.songProgressSb.progress = (song.second * 1000 / song.playTime)
        } else {
            binding.songProgressSb.progress = 0
        }
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
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getString("songId", "")

        if (songId != null && songId.isNotEmpty()) {
            database.getReference("songs").child(songId).get().addOnSuccessListener { snapshot ->
                val currentSong = snapshot.getValue(Song::class.java)
                if (currentSong != null) {
                    nowPos = getPlayingSongPosition(songId)
                    songs[nowPos] = currentSong
                    setPlayer(songs[nowPos])
                }
            }
        }
    }


    override fun onPause(){
        super.onPause()

        if (songs.isNotEmpty()) {

            val song = songs[nowPos]
            if (song.playTime > 0) {
                song.second = (binding.songProgressSb.progress * song.playTime) / 1000
            } else {
                song.second = 0
            }
            song.isPlaying = false

            setPlayerStatus(false)

            val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("songId", song.id)
            editor.apply()

            val updates = mapOf<String, Any>(
                "second" to song.second,
                "playing" to song.isPlaying
            )
            database.getReference("songs").child(song.id).updateChildren(updates)
                .addOnSuccessListener {
                    Log.d("Week8", "onPause: 재생 상태 업데이트 성공")
                }
                .addOnFailureListener {
                    Log.e("Week8", "onPause: 재생 상태 업데이트 실패", it)
                }
        }
    }

    override fun onDestroy(){
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}