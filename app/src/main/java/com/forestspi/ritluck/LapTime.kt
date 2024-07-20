package com.forestspi.ritluck

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lap_times")
data class LapTime(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lapTime: Long
)
