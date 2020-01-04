package com.example.itexblog.ui.model.commentmodel

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CommentDao {
    @Insert
    suspend fun insert(comments: CommentsEntity)

    @Update
    suspend fun update(comments: CommentsEntity)

    @Delete
    suspend fun delete(comments: CommentsEntity)

    @Query("DELETE FROM commentsentity")
    suspend fun deleteAllComments()

    @get:Query(
        "SELECT * FROM commentsentity"
    )
    val allComments: LiveData<List<CommentsEntity?>?>?
}