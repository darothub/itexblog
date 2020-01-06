package com.example.itexblog.ui.model

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * An interface to access data object via the database
 * DAO(Data Access Object)
 */
@Dao
interface PostDao {

    @Insert
    suspend fun insert(post:PostEntity)

    @Update
    suspend fun update(post:PostEntity)

    @Delete
    suspend fun delete(post:PostEntity)

    @Query("DELETE FROM postentity")
    suspend fun deleteAllPosts()

    @Transaction
    @Query("SELECT * FROM postentity")
    fun getPostWithComments():LiveData<List<PostEntityWithCommentEntity>>?

    @get:Query(
        "SELECT * FROM postentity"
    )
    val allPosts: LiveData<List<PostEntity?>?>?
}
