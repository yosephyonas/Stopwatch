package com.forestspi.ritluck

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LapTimesAdapter(private val lapTimes: List<Long>) : RecyclerView.Adapter<LapTimesAdapter.LapTimeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LapTimeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lap_time, parent, false)
        return LapTimeViewHolder(view)
    }

    override fun onBindViewHolder(holder: LapTimeViewHolder, position: Int) {
        holder.bind(lapTimes[position], position + 1)
    }

    override fun getItemCount(): Int {
        return lapTimes.size
    }

    class LapTimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lapNumberTextView: TextView = itemView.findViewById(R.id.lapNumberTextView)
        private val lapTimeTextView: TextView = itemView.findViewById(R.id.lapTimeTextView)

        fun bind(lapTimeInMillis: Long, lapNumber: Int) {
            lapNumberTextView.text = "Lap $lapNumber"
            lapTimeTextView.text = formatTime(lapTimeInMillis)
        }

        private fun formatTime(timeInMillis: Long): String {

            val seconds = (timeInMillis / 1000) % 60
            val minutes = (timeInMillis / (1000 * 60)) % 60
            val hours = (timeInMillis / (1000 * 60 * 60)) % 24
            return String.format("%02d:%02d.%02d", hours, minutes, seconds)
        }
    }
}
