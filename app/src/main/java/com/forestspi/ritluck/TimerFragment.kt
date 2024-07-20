package com.forestspi.ritluck

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.Fragment
class TimerFragment : Fragment() {

    private lateinit var hoursPicker: NumberPicker
    private lateinit var minutesPicker: NumberPicker
    private lateinit var secondsPicker: NumberPicker
    private lateinit var startButton: Button
    private lateinit var stopButton: Button
    private lateinit var restartButton: Button
    private lateinit var timerCountdown: TextView
    private lateinit var header: TextView
    private lateinit var timerTitle: TextView
    private var countDownTimer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 0
    private var isTimerRunning = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer, container, false)

        hoursPicker = view.findViewById(R.id.hourPicker)
        minutesPicker = view.findViewById(R.id.minutePicker)
        secondsPicker = view.findViewById(R.id.secondPicker)
        startButton = view.findViewById(R.id.startTimerButton)
        stopButton = view.findViewById(R.id.stopTimerButton)
        restartButton = view.findViewById(R.id.restartTimerButton)
        timerCountdown = view.findViewById(R.id.timerCountdown)
        header = view.findViewById(R.id.header)
        timerTitle = view.findViewById(R.id.timerTitle)

        setupNumberPickers()
        setupButtonListeners()

        return view
    }

    private fun setupNumberPickers() {
        hoursPicker.minValue = 0
        hoursPicker.maxValue = 23
        minutesPicker.minValue = 0
        minutesPicker.maxValue = 59
        secondsPicker.minValue = 0
        secondsPicker.maxValue = 59
    }

    private fun setupButtonListeners() {
        startButton.setOnClickListener {
            val hours = hoursPicker.value
            val minutes = minutesPicker.value
            val seconds = secondsPicker.value
            timeLeftInMillis = (hours * 3600 + minutes * 60 + seconds) * 1000L

            if (timeLeftInMillis > 0) {
                startTimer(timeLeftInMillis)
                hidePickersAndLabels()
                timerTitle.visibility = View.VISIBLE
                updateButtonVisibility(isTimerRunning = true)
            }
        }

        stopButton.setOnClickListener {
            countDownTimer?.cancel()
            updateButtonVisibility(isTimerRunning = false)
        }

        restartButton.setOnClickListener {
            // Reset the timer and update the UI
            countDownTimer?.cancel()
            resetTimer()
            startButton.visibility = View.VISIBLE
            stopButton.visibility = View.GONE
            restartButton.visibility = View.GONE
            timerCountdown.text = "00:00:00" // Reset countdown display
            timerTitle.visibility = View.GONE
        }
    }

    private fun updateButtonVisibility(isTimerRunning: Boolean) {
        if (isTimerRunning) {
            startButton.visibility = View.GONE
            stopButton.visibility = View.VISIBLE
            restartButton.visibility = View.VISIBLE
        } else {
            startButton.visibility = if (timeLeftInMillis > 0) View.VISIBLE else View.GONE
            stopButton.visibility = View.GONE
            restartButton.visibility = View.GONE
        }
    }

    private fun hidePickersAndLabels() {
        hoursPicker.visibility = View.GONE
        minutesPicker.visibility = View.GONE
        secondsPicker.visibility = View.GONE
        header.visibility = View.GONE

        val hoursLabel = view?.findViewById<TextView>(R.id.hoursLabel)
        val minutesLabel = view?.findViewById<TextView>(R.id.minutesLabel)
        val secondsLabel = view?.findViewById<TextView>(R.id.secondsLabel)
        val dividerHoursMinutes = view?.findViewById<View>(R.id.dividerHoursMinutes)
        val dividerMinutesSeconds = view?.findViewById<View>(R.id.dividerMinutesSeconds)

        hoursLabel?.visibility = View.GONE
        minutesLabel?.visibility = View.GONE
        secondsLabel?.visibility = View.GONE
        dividerHoursMinutes?.visibility = View.GONE
        dividerMinutesSeconds?.visibility = View.GONE

        timerCountdown.visibility = View.VISIBLE
    }

    private fun resetTimer() {
        timeLeftInMillis = 0
        timerCountdown.text = "00:00:00"
        hoursPicker.visibility = View.VISIBLE
        minutesPicker.visibility = View.VISIBLE
        secondsPicker.visibility = View.VISIBLE
        header.visibility = View.VISIBLE

        val hoursLabel = view?.findViewById<TextView>(R.id.hoursLabel)
        val minutesLabel = view?.findViewById<TextView>(R.id.minutesLabel)
        val secondsLabel = view?.findViewById<TextView>(R.id.secondsLabel)
        val dividerHoursMinutes = view?.findViewById<View>(R.id.dividerHoursMinutes)
        val dividerMinutesSeconds = view?.findViewById<View>(R.id.dividerMinutesSeconds)

        hoursLabel?.visibility = View.VISIBLE
        minutesLabel?.visibility = View.VISIBLE
        secondsLabel?.visibility = View.VISIBLE
        dividerHoursMinutes?.visibility = View.VISIBLE
        dividerMinutesSeconds?.visibility = View.VISIBLE

        timerCountdown.visibility = View.GONE
    }

    private fun startTimer(milliseconds: Long) {
        countDownTimer = object : CountDownTimer(milliseconds, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                val seconds = (millisUntilFinished / 1000).toInt()
                val minutes = seconds / 60
                val hours = minutes / 60
                timerCountdown.text = String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
            }

            override fun onFinish() {
                timerCountdown.text = "00:00:00"
                updateButtonVisibility(isTimerRunning = false)
            }
        }.start()
    }
}
