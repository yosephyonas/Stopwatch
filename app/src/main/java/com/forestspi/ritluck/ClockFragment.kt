package com.forestspi.ritluck

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class ClockFragment : Fragment() {

    private lateinit var clockView: ClockView
    private lateinit var localTimeTextView: TextView
    private lateinit var sydneyTimeTextView: TextView
    private lateinit var londonTimeTextView: TextView

    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            updateTimes()
            handler.postDelayed(this, 1000)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_clock, container, false)

        clockView = view.findViewById(R.id.clockView)
        localTimeTextView = view.findViewById(R.id.localTimeTextView)

        handler.post(updateTimeRunnable)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateTimeRunnable)
    }

    private fun updateTimes() {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        localTimeTextView.text = dateFormat.format(currentTime)


        clockView.invalidate()
    }
}
