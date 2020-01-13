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
        "SELECT * FROM commentsentity "
    )
    val allComments: LiveData<List<CommentsEntity?>?>?

    @Query("SELECT * FROM commentsentity")
    suspend fun getCommentList():List<CommentsEntity>

    @Query("SELECT * FROM commentsentity WHERE post_id = :id")
    suspend fun getCommentById(id:Int):List<CommentsEntity>

    @Query("SELECT * FROM commentsentity WHERE post_id = :id ORDER BY id DESC")
    fun getCommentByIdLive(id:Int):LiveData<List<CommentsEntity?>?>?

}