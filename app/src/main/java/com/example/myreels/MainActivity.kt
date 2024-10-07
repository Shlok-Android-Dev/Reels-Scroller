package com.example.myreels

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.myreels.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var reelsList: MutableList<ReelsModel>
    private lateinit var binding: ActivityMainBinding
    private lateinit var reelsAdapter: ReelsAdapter
    private var previousIndex = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        reelsList = mutableListOf(
            ReelsModel(videoUrl = "android.resource://${packageName}/${R.raw.a}", title = "sample", decs = getString(R.string.longString)),
            ReelsModel(videoUrl = "android.resource://${packageName}/${R.raw.b}", title = "video", decs = getString(R.string.longString)),
            ReelsModel(videoUrl = "android.resource://${packageName}/${R.raw.c}", title = "video", decs = getString(R.string.longString)),
            ReelsModel(videoUrl = "android.resource://${packageName}/${R.raw.d}", title = "video", decs = getString(R.string.longString)),
            ReelsModel(videoUrl = "android.resource://${packageName}/${R.raw.e}", title = "video", decs = getString(R.string.longString)),
            ReelsModel(videoUrl = "android.resource://${packageName}/${R.raw.f}", title = "video", decs = getString(R.string.longString))
            // Add more videos
        )

        reelsAdapter = ReelsAdapter(this@MainActivity, reelsList)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = reelsAdapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (previousIndex != -1 && previousIndex != position) {
                    reelsAdapter.pauseVideoAt(previousIndex)
                    reelsAdapter.setCurrentlyPlayingIndex(-1) // Reset currently playing index

                }
                reelsAdapter.resumeVideoAt(position)
                reelsAdapter.setCurrentlyPlayingIndex(position)
                previousIndex = position
            }
        })
    }

    override fun onStop() {
        super.onStop()
        reelsAdapter.releasePlayers()
    }

    override fun onDestroy() {
        super.onDestroy()
        reelsAdapter.releasePlayers()
    }
}