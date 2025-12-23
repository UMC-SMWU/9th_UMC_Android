package com.example.week9

import android.R.attr.text
import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.week9.databinding.ActivitySongBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import java.util.Timer
import kotlin.concurrent.timer

class SongActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySongBinding
    lateinit var timer : Timer
    var nowPos = 0

    private var mediaPlayer: MediaPlayer? = null

    private var songs = ArrayList<Song>()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initClickListener()
    }


    private fun initClickListener(){
        // 일시정지, 재생버튼 토글
        binding.songMiniplayerIv.setOnClickListener { setPlayerStatus(true) }
        binding.songPauseIv.setOnClickListener { setPlayerStatus(false) }

        binding.songDownIb.setOnClickListener{ finish() }

        // 다음곡 버튼, 이전곡 버튼 터치 시 곡 처음부터 시작
        binding.songPreviousIv.setOnClickListener { moveSong(-1) }

        binding.songNextIv.setOnClickListener { moveSong(1) }

        binding.songLikeIv.setOnClickListener { setLike(songs[nowPos].isLike) }
    }
    private fun setPlayerStatus(isPlaying : Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){
            binding.songMiniplayerIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else{
            binding.songMiniplayerIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }
    private fun setLike(isLike: Boolean) {
        val songId = songs[nowPos].id
        val newIsLike = !isLike

        database.getReference("songs").child(songId).child("isLike").setValue(newIsLike)
            .addOnSuccessListener {
                songs[nowPos].isLike = newIsLike
                if (newIsLike) {
                    binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
                    Toast.makeText(this, "좋아요 한 곡에 담겼습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
                    Toast.makeText(this, "좋아요가 취소되었습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Log.e("SongActivity", "좋아요 상태 업데이트 실패", it)
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
    private fun restartSong(){
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
        initSong()
        setPlayer(songs[nowPos])
        setPlayerStatus(true)
    }

    private fun initPlayList() {
        val songsRef = database.getReference("songs")
        songsRef.get().addOnSuccessListener { dataSnapshot ->
            songs.clear()
            for (songSnapshot in dataSnapshot.children) {
                val song = songSnapshot.getValue(Song::class.java)
                song?.let { songs.add(it) }
            }
            Log.d("SongActivity", "Firebase로부터 노래 로드 성공: ${songs.size}개")
            initSong()
        }.addOnFailureListener {
            Log.e("SongActivity", "Firebase로부터 노래 로드 실패", it)
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
        if (songId.isEmpty()) return 0
        for (i in 0 until songs.size) {
            if (songs[i].id == songId) return i
        }
        return 0
    }
    private fun setPlayer(song : Song){
        binding.songMusicTitleTv.text = song.title
        binding.songSingerNameTv.text = song.singer
        binding.songStartTimeTv.text = String.format("%02d:%02d", song.second/60, song.second%60)
        binding.songEndTimeTv.text = String.format("%02d:%02d", song.playTime/60, song.playTime%60)
        binding.songAlbumIv.setImageResource(song.coverImg ?: R.drawable.img_album_exp2)
        binding.songProgressSb.progress = (song.second/song.playTime) * 1000
        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        if (music == 0) {
            Log.e("SongActivity", "Resource not found: ${song.music}")
        } else {
            mediaPlayer = MediaPlayer.create(this, music)
            mediaPlayer?.seekTo(song.second * 1000)
        }

        if(song.isLike){
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songLikeIv.setImageResource(R.drawable.ic_my_like_off)
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
    override fun onPause(){
        super.onPause()

        if (songs.isEmpty()) return

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
                Log.d("SongActivity", "onPause: 재생 상태 업데이트 성공")
            }
            .addOnFailureListener {
                Log.e("SongActivity", "onPause: 재생 상태 업데이트 실패", it)
            }
    }

    override fun onDestroy(){
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null
    }

}