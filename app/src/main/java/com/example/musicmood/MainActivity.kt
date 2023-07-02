package com.example.musicmood
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore.Audio.Media
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    // variables
    var startTime = 0.0
    var finalTime = 0.0
    var forwardTime = 10000
    var backwardTime = 10000
    var oneTimeOnly = false

    // Handler
    var handler: Handler = Handler()

    // Media Player
    var mediaPlayer = MediaPlayer()
    lateinit var time_txt: TextView
    lateinit var seekBar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val play_btn: Button = findViewById(R.id.play_button)
        val stop_btn: Button = findViewById(R.id.pause_button)
        val forward_btn: Button = findViewById(R.id.next_button)
        val back_btn: Button = findViewById(R.id.back_button)
        val title_txt: TextView = findViewById(R.id.title_song)
        time_txt = findViewById(R.id.timer)
        seekBar = findViewById(R.id.seek_bar)

//      Media Player Lines...
        mediaPlayer=MediaPlayer()
        mediaPlayer.setDataSource(resources.openRawResourceFd(R.raw.musify))
        mediaPlayer.prepare()
        seekBar.isClickable=false

        // Functionalities of Buttons
        play_btn.setOnClickListener(){
            mediaPlayer.start()
            finalTime=mediaPlayer.duration.toDouble()
            startTime=mediaPlayer.currentPosition.toDouble()

            if (oneTimeOnly){
                seekBar.max=finalTime.toInt()
                oneTimeOnly=true
            }
            time_txt.text=startTime.toString()
            seekBar.setProgress(startTime.toInt())
            handler.postDelayed(updateSongTime,100)
        }

        // Configuration of music title
        title_txt.text= ""+resources.getResourceEntryName(R.raw.musify)

        // Stop Button (Pause)
        stop_btn.setOnClickListener {
            mediaPlayer.pause()
        }

        // Forward Button
        forward_btn.setOnClickListener {
            var temp=startTime
            if((temp+forwardTime)<=finalTime){
                startTime=startTime+forwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }
            else{
                Toast.makeText(this,"Sorry!Can't Jump There",Toast.LENGTH_LONG).show()
            }
        }
        back_btn.setOnClickListener(){
            var temp=startTime
            if ((temp-backwardTime)>0){
                startTime=startTime-backwardTime
                mediaPlayer.seekTo(startTime.toInt())
            }
            else{
                Toast.makeText(this,"Sorry!Can't Jump There",Toast.LENGTH_LONG).show()
            }

        }

    }
    // Create the runnable of UpdateSongTime
    val updateSongTime:Runnable = object : Runnable{
        override fun run() {
            startTime=mediaPlayer.currentPosition.toDouble()
            time_txt.text=" "+String.format(
                "%d min , %d sec", TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()
                -TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(startTime.toLong())
                ))
            )
            seekBar.progress=startTime.toInt()
            handler.postDelayed(this,100)
        }
    }

}
