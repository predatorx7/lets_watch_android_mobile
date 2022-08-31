package com.magnificsoftware.letswatch.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.magnificsoftware.letswatch.data_class.vo.User

/**
 * Interface for database access for User related operations.
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT * FROM user WHERE login = :login")
    fun findByLogin(login: String): LiveData<User>

    @Query("SELECT * FROM user WHERE email = :email")
    fun findByEmail(email: String): User?

    @Query("SELECT * FROM user ORDER BY creationDateTime DESC LIMIT 1")
    fun getLatestUser(): User?

    @Query("DELETE FROM user")
    fun removeAll()
}
