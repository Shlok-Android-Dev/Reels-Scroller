package com.example.myreels

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.sanjayprajapat.showmoretextview.ShowMoreTextView
import com.sanjayprajapat.showmoretextview.enums.TextState
import com.sanjayprajapat.showmoretextview.listener.StateChangeListener

class ReelsAdapter(
    private val context: Context,
    private val reelsList: MutableList<ReelsModel>
) : RecyclerView.Adapter<ReelsAdapter.VideoViewHolder>() {

    private val exoPlayerList = mutableListOf<ExoPlayer>()
    private var currentlyPlayingIndex = -1 // Track the currently playing video index
    private val audioManager: AudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.reels_item, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.titleTextView.text = reelsList[position].title
        holder.decsTextView.text = reelsList[position].decs

        // Initialize ExoPlayer if not already done
        if (position >= exoPlayerList.size) {
            val exoPlayer = ExoPlayer.Builder(holder.itemView.context).build()
            holder.playerView.player = exoPlayer
            exoPlayerList.add(exoPlayer)

            val mediaItem = MediaItem.fromUri(reelsList[position].videoUrl)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()

            exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

            // Set listener to handle playback end
            exoPlayer.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        exoPlayer.seekTo(0) // Go back to the start
                        exoPlayer.playWhenReady = false // Do not autoplay after ending
                    }
                }
            })
        } else {
            holder.playerView.player = exoPlayerList[position]
        }

        // Control playback based on whether this item is the currently playing video
        holder.playerView.player?.playWhenReady = currentlyPlayingIndex == position
    }

    override fun getItemCount(): Int = reelsList.size


    @SuppressLint("ClickableViewAccessibility")
    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playerView: PlayerView = itemView.findViewById(R.id.playerView)
        val titleTextView: TextView = itemView.findViewById(R.id.titleReel)
        val decsTextView: ShowMoreTextView = itemView.findViewById(R.id.reelDecs)
        val playPauseOverlay: ImageView = itemView.findViewById(R.id.play_pause_overlay)

        init {
            // Set tap listener
            playerView.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    togglePlayPause()
                }
                true
            }
        }
        private fun togglePlayPause() {
            val exoPlayer = playerView.player
            if (exoPlayer != null) {
                if (exoPlayer.isPlaying) {
                    exoPlayer.playWhenReady = false
                    showOverlay(R.drawable.ic_play)
                } else {
                    exoPlayer.playWhenReady = true
                    showOverlay(R.drawable.ic_pause)
                }
            }
        }

        private fun showOverlay(iconResId: Int) {
            playPauseOverlay.setImageResource(iconResId)
            playPauseOverlay.visibility = View.VISIBLE

            // Fade out the overlay after a delay
            playPauseOverlay.postDelayed({
                playPauseOverlay.visibility = View.GONE
            }, 1000) // Show for 1 second
        }
    }

    fun pauseVideoAt(index: Int) {
        if (index in exoPlayerList.indices) {
            exoPlayerList[index].playWhenReady = false
            abandonAudioFocus()
        }
    }

    fun resumeVideoAt(index: Int) {
        if (index in exoPlayerList.indices) {
            requestAudioFocus()
            exoPlayerList[index].playWhenReady = true
        }
    }

    private fun requestAudioFocus() {
        audioManager.requestAudioFocus(
            null,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )
    }

    private fun abandonAudioFocus() {
        audioManager.abandonAudioFocus(null)
    }

    fun releasePlayers() {
        for (player in exoPlayerList) {
            player.release()
        }
        exoPlayerList.clear()
    }

    fun setCurrentlyPlayingIndex(index: Int) {
        currentlyPlayingIndex = index
    }
}
