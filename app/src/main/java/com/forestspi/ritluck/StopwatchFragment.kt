package com.forestspi.ritluck

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StopwatchFragment : Fragment() {

    private lateinit var stopwatchProgress: CircularProgressView
    private lateinit var stopwatchTime: TextView
    private lateinit var lapTimesRecyclerView: RecyclerView
    private lateinit var startStopButton: ImageButton
    private lateinit var resetButton: ImageButton
    private lateinit var lapButton: ImageButton
    private lateinit var bestLapTime: TextView

    private var isRunning = false
    private var startTime = 0L
    private var elapsedTime = 0L
    private var bestTime = Long.MAX_VALUE
    private var laps = mutableListOf<Long>()
    private val handler = Handler()
    private val runnable: Runnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                elapsedTime = System.currentTimeMillis() - startTime
                updateStopwatch()
                handler.postDelayed(this, 50)
            }
        }
    }

    // Maximum time in milliseconds (1 hour)
    private val MAX_TIME_IN_MILLIS = 3600000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stopwatch, container, false)

        // Cast to CircularProgressView instead of ProgressBar
        stopwatchProgress = view.findViewById(R.id.stopwatchProgress)
        stopwatchTime = view.findViewById(R.id.stopwatchTime)
        lapTimesRecyclerView = view.findViewById(R.id.lapTimesRecyclerView)
        startStopButton = view.findViewById(R.id.startStopButton)
        resetButton = view.findViewById(R.id.resetButton)
        lapButton = view.findViewById(R.id.lapButton)
        bestLapTime = view.findViewById(R.id.bestLapTime)

        // Configure RecyclerView for lap times
        lapTimesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        lapTimesRecyclerView.adapter = LapTimesAdapter(laps)

        // Initially hide the progress indicator
        stopwatchProgress.visibility = View.VISIBLE

        startStopButton.setOnClickListener {
            if (isRunning) {
                stopStopwatch()
            } else {
                startStopwatch()
            }
        }

        resetButton.setOnClickListener {
            resetStopwatch()
        }

        lapButton.setOnClickListener {
            if (isRunning) {
                recordLap()
            }
        }

        // Initialize the progress animation
        startProgressAnimation()

        return view
    }

    private fun startProgressAnimation() {
        val animator = ObjectAnimator.ofInt(stopwatchProgress, "progress", 0, 100)
        animator.duration = 60000 // 60 seconds for a full rotation
        animator.repeatCount = ObjectAnimator.INFINITE
        animator.start()
    }

    private fun startStopwatch() {
        isRunning = true
        startTime = System.currentTimeMillis() - elapsedTime
        handler.post(runnable)
        animatePlayToPause()
        // Show progress indicator
        stopwatchProgress.visibility = View.VISIBLE
    }

    private fun stopStopwatch() {
        isRunning = false
        handler.removeCallbacks(runnable)
        animatePauseToPlay()
        if (elapsedTime < bestTime) {
            bestTime = elapsedTime
            bestLapTime.text = "Best Time: ${formatTime(bestTime)}"
        }
    }

    private fun resetStopwatch() {
        isRunning = false
        handler.removeCallbacks(runnable)
        elapsedTime = 0
        laps.clear()
        lapTimesRecyclerView.adapter?.notifyDataSetChanged()
        updateStopwatch()
        animatePauseToPlay()
        bestTime = Long.MAX_VALUE
        bestLapTime.text = "Best Time: --:--:--.--"
        // Hide progress indicator on reset
        stopwatchProgress.visibility = View.INVISIBLE
    }

    private fun recordLap() {
        laps.add(elapsedTime)
        lapTimesRecyclerView.adapter?.notifyItemInserted(laps.size - 1)
        lapTimesRecyclerView.scrollToPosition(laps.size - 1)

        // Ensure the RecyclerView has children before accessing them
        if (laps.size > 0 && laps.size <= lapTimesRecyclerView.childCount) {
            animateLapView(lapTimesRecyclerView.getChildAt(laps.size - 1))
        }
        updateLapsDisplay()
    }

    private fun updateLapsDisplay() {
        lapTimesRecyclerView.adapter?.notifyDataSetChanged()
    }

    private fun updateStopwatch() {
        val time = formatTime(elapsedTime)
        stopwatchTime.text = time

        // Calculate progress in percentage
        val progress = (elapsedTime.toFloat() / MAX_TIME_IN_MILLIS) * 100f

        // Update the progress of the circular ProgressBar with animation
        ObjectAnimator.ofInt(stopwatchProgress, "progress", progress.toInt())
            .apply {
                duration = 300 // Adjust duration as needed
                start()
            }
    }

    private fun animatePlayToPause() {
        ObjectAnimator.ofFloat(startStopButton, "rotation", 0f, 90f)
            .apply {
                duration = 250 // Adjust duration as needed
                interpolator = AccelerateInterpolator() // Use an interpolator for smooth animation
                start()
            }
        // Change icon resource after half of the animation duration
        startStopButton.postDelayed({
            startStopButton.setImageResource(R.drawable.ic_pause)
        }, 125)
    }

    private fun animatePauseToPlay() {
        ObjectAnimator.ofFloat(startStopButton, "rotation", 90f, 0f)
            .apply {
                duration = 250 // Adjust duration as needed
                interpolator = AccelerateInterpolator() // Use an interpolator for smooth animation
                start()
            }
        // Change icon resource after half of the animation duration
        startStopButton.postDelayed({
            startStopButton.setImageResource(R.drawable.ic_play)
        }, 125)
    }

    private fun animateLapView(view: View) {
        view.alpha = 0f
        view.translationY = 50f
        view.animate().alpha(1f).translationY(0f).setDuration(500).start()
    }

    private fun formatTime(timeInMillis: Long): String {
        val milliseconds = (timeInMillis % 1000) / 10
        val seconds = (timeInMillis / 1000) % 60
        val minutes = (timeInMillis / (1000 * 60)) % 60
        val hours = (timeInMillis / (1000 * 60 * 60)) % 24
        return String.format("%02d:%02d.%02d", hours, minutes, seconds)
    }
}
