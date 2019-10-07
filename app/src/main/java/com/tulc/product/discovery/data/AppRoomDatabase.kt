package com.tulc.product.discovery.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tulc.product.discovery.models.RecentSearch
import com.tulc.product.discovery.utils.APP_DATABASE_NAME


@Database(entities = [RecentSearch::class], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun recentSearchDao(): RecentSearchDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    APP_DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
