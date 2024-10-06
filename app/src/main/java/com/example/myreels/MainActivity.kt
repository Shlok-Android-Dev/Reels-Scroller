package com.example.myreels

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.myreels.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ReelsAdapter
    private lateinit var reelsList: MutableList<ReelsModel>
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the reels list
        reelsList = mutableListOf()

        // Add video to the list
        reelsList.add(ReelsModel(videoUrl = "android.resource://${packageName}/${R.raw.a}",title = "Title",decs = "Description"))
        reelsList.add(ReelsModel(videoUrl = "android.resource://${packageName}/${R.raw.b}",title = "Title",decs = "Description"))
        reelsList.add(ReelsModel(videoUrl = "android.resource://${packageName}/${R.raw.c}",title = "Title",decs = "Description"))
        reelsList.add(ReelsModel(videoUrl = "android.resource://${packageName}/${R.raw.d}",title = "Title",decs = "Description"))
        reelsList.add(ReelsModel(videoUrl = "android.resource://${packageName}/${R.raw.e}",title = "Title",decs = "Description"))
        reelsList.add(ReelsModel(videoUrl = "android.resource://${packageName}/${R.raw.f}",title = "Title",decs = "Description"))

        // Initialize the adapter
        adapter = ReelsAdapter(this, reelsList)
        binding.viewPager.adapter = adapter
    }
}
