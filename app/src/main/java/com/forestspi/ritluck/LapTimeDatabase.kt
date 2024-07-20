package com.forestspi.ritluck

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LapTime::class], version = 1, exportSchema = false)
abstract class LapTimeDatabase : RoomDatabase() {
    abstract fun lapTimeDao(): LapTimeDao

    companion object {
        @Volatile
        private var INSTANCE: LapTimeDatabase? = null

        fun getDatabase(context: Context): LapTimeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LapTimeDatabase::class.java,
                    "lap_time_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
