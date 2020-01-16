package com.example.itexblog.ui.utils

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.itexblog.ui.model.PostDao
import com.example.itexblog.ui.model.PostEntity
import com.example.itexblog.ui.model.commentmodel.CommentDao
import com.example.itexblog.ui.model.commentmodel.CommentsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoroutineTaskSingleton (application: Application) {

    companion object {
        @Volatile
        private var INSTANCE: CoroutineTaskSingleton? = null
        fun getInstance(application: Application) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: CoroutineTaskSingleton(application).also {
                    INSTANCE = it
                }
            }
    }



    fun insertTask(postDao:PostDao, post: PostEntity){

        CoroutineScope(Dispatchers.IO).launch {
            postDao.insert(post)
        }

    }

    fun updateTask(postDao:PostDao, post: PostEntity){
        CoroutineScope(Dispatchers.IO).launch {
            postDao.update(post)
        }
    }
    fun deleteTask(postDao:PostDao, post: PostEntity){
        CoroutineScope(Dispatchers.IO).launch {
            postDao.delete(post)
        }
    }

    fun deleteAllTask(postDao:PostDao){
        CoroutineScope(Dispatchers.IO).launch {
            postDao.deleteAllPosts()
        }
    }

    fun getPostWithComments(postDao:PostDao){
        CoroutineScope(Dispatchers.IO).launch {
            postDao.getPostWithComments()
        }
    }

    fun insertCommentTask(commentDao: CommentDao, commentsEntity: CommentsEntity){

        CoroutineScope(Dispatchers.IO).launch {
            commentDao.insert(commentsEntity)
        }

    }

    fun updateCommentTask(commentDao: CommentDao, commentsEntity: CommentsEntity){

        CoroutineScope(Dispatchers.IO).launch {
            commentDao.update(commentsEntity)
        }

    }
    fun deleteCommentTask(commentDao: CommentDao, commentsEntity: CommentsEntity){

        CoroutineScope(Dispatchers.IO).launch {
            commentDao.delete(commentsEntity)
        }

    }

    fun deleteAllCommentTask(commentDao: CommentDao){

        CoroutineScope(Dispatchers.IO).launch {
            commentDao.deleteAllComments()
        }

    }

    fun getAllCommentsByIdLive(commentDao: CommentDao, id:Int): LiveData<List<CommentsEntity?>?>?{
        var comments:LiveData<List<CommentsEntity?>?>?=null
        CoroutineScope(Dispatchers.IO).launch {
            comments = commentDao.getCommentByIdLive(id)
        }
        return comments
    }
}
