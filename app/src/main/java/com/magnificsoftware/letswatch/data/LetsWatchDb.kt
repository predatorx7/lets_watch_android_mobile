package com.magnificsoftware.letswatch.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.magnificsoftware.letswatch.data_class.vo.ShowVO
import com.magnificsoftware.letswatch.data_class.vo.User


/**
 * SQLite Database for storing information
 */
@Database(entities = [ShowVO::class, User::class], version = 1, exportSchema = false)
abstract class LetsWatchDb : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao

    abstract fun userDao(): UserDao
}