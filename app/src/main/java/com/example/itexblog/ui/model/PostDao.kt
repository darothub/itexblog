package com.example.itexblog.ui.model

import androidx.lifecycle.LiveData
import androidx.room.*

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

    @get:Query(
        "SELECT * FROM postentity"
    )
    val allPosts: LiveData<List<PostEntity?>?>?
}
