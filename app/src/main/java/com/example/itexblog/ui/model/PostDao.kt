package com.example.itexblog.ui.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PostDao {

    @Insert
    fun insert(post:PostEntity)

    @Update
    fun update(post:PostEntity)

    @Delete
    fun delete(post:PostEntity)

    @Query("DELETE FROM postentity")
    fun deleteAllPosts()

    @get:Query(
        "SELECT * FROM postentity"
    )
    val allPosts: LiveData<List<PostEntity?>?>?
}
