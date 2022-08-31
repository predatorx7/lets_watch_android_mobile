package com.magnificsoftware.letswatch.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.magnificsoftware.letswatch.data_class.vo.ShowVO

@Dao
interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(show: ShowVO)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(show: List<ShowVO>)

    @Query("DELETE FROM showVO WHERE id  == :id")
    fun remove(id: String)

    @Query("SELECT * FROM showVO WHERE userId = :userId")
    fun getSavedShows(userId: String): LiveData<List<ShowVO>>

    @Query("SELECT EXISTS (SELECT 1 FROM showVO WHERE showId = :showId AND userId = :userId)")
    fun exists(showId: Int, userId: String): Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM showVO WHERE id = :id)")
    fun exists(id: String): Boolean
}
