package com.example.myreels

import android.content.Context
  import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView

class ReelsAdapter(
    private val context: Context,
    private val reelsList: MutableList<ReelsModel>
) : RecyclerView.Adapter<ReelsAdapter.ViewHolder>() {

    private val videoViews = mutableListOf<VideoView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reels_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val reel = reelsList[position]

        // Set the video URI
        holder.videoView.setVideoURI(Uri.parse(reel.videoUrl))
        holder.reelsTitle.text = reel.title
        holder.reelsDecs.text = reel.decs

        videoViews.add(holder.videoView) // Add VideoView to the list

        // Set up video playback
        holder.videoView.setOnPreparedListener { mp ->
            mp.start()
            // Aspect ratio handling...
        }

        holder.videoView.setOnCompletionListener { mp ->
            mp.start() // Loop the video
        }
    }

    override fun getItemCount(): Int {
        return reelsList.size
    }

/*    fun pauseVideos() {
        for (videoView in videoViews) {
            videoView.pause()
        }
    }

    fun resumeVideos() {
        for (videoView in videoViews) {
            videoView.start()
        }
    }*/

       /* val reel = reelsList[position]

        // Set the video URI
        holder.videoView.setVideoURI(Uri.parse(reel.videoUrl))
        holder.reelsTitle.text = reel.title
        holder.reelsDecs.text = reel.decs

        // Set up video playback
        holder.videoView.setOnPreparedListener { mp ->
            mp.start()

            // Calculate aspect ratio and scale
            val videoRatio = mp.videoWidth.toFloat() / mp.videoHeight.toFloat()
            val screenRatio = holder.videoView.width.toFloat() / holder.videoView.height.toFloat()
            val scale = videoRatio / screenRatio

            if (scale >= 1) {
                holder.videoView.scaleX = scale
            } else {
                holder.videoView.scaleY = 1f / scale
            }
        }

        holder.videoView.setOnCompletionListener { mp ->
            Log.d("ReelsAdapter", "Video is prepared")
            mp.start() // Loop the video
        }
    }*/

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoView: VideoView = itemView.findViewById(R.id.videoView)
        val reelsTitle: TextView = itemView.findViewById(R.id.titleReel)
        val reelsDecs: TextView = itemView.findViewById(R.id.reelDecs)

        init {
            // Optional: Release media resources when view is recycled
            videoView.setOnPreparedListener(null)
            videoView.setOnCompletionListener(null)
        }
    }
}
