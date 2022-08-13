package com.app.remindme.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.remindme.data.model.EventsModel

@Database(entities = [EventsModel::class], version = 1, exportSchema = false)
abstract class DatabaseBuilder : RoomDatabase() {
    abstract fun userDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseBuilder? = null

        fun getDatabase(context: Context): DatabaseBuilder {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseBuilder::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}