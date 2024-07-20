package com.forestspi.ritluck

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LapTimeDao {
    @Insert
    suspend fun insertLapTime(lapTime: LapTime)

    @Query("SELECT * FROM lap_times ORDER BY id DESC")
    suspend fun getAllLapTimes(): List<LapTime>

    @Query("DELETE FROM lap_times")
    suspend fun deleteAllLapTimes()
}
