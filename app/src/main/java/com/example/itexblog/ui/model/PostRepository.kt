package com.example.itexblog.ui.model

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.itexblog.ui.model.commentmodel.CommentDao
import com.example.itexblog.ui.model.commentmodel.CommentsEntity
import com.example.itexblog.ui.utils.CoroutineTaskSingleton

class PostRepository(application: Application) {

    fun insert(post: PostEntity?, application: Application){
        CoroutineTaskSingleton.getInstance(application).insertTask(postDao, post!!)
    }

    fun update(post: PostEntity?, application: Application){
        CoroutineTaskSingleton.getInstance(application).updateTask(postDao, post!!)
    }

    fun delete(post: PostEntity?, application: Application){
        CoroutineTaskSingleton.getInstance(application).deleteTask(postDao, post!!)
    }

    fun deleteAll(application: Application){
        CoroutineTaskSingleton.getInstance(application).deleteAllTask(postDao)
    }


    fun insertComment(comment: CommentsEntity?, application: Application){
        CoroutineTaskSingleton.getInstance(application).insertCommentTask(commentDao, comment!!)
    }

    fun updateComment(comment: CommentsEntity?, application: Application){
        CoroutineTaskSingleton.getInstance(application).updateCommentTask(commentDao, comment!!)
    }

    fun deleteComment(comment: CommentsEntity?, application: Application){
        CoroutineTaskSingleton.getInstance(application).deleteCommentTask(commentDao, comment!!)
    }

    fun deleteAllComments(application: Application){
        CoroutineTaskSingleton.getInstance(application).deleteAllCommentTask(commentDao)
    }

    fun getCommentsByIdLive(application: Application, id:Int): LiveData<List<CommentsEntity?>?>?{
        return PostDatabase.getInstance(application)?.commentDao()?.getCommentByIdLive(id)
    }

    fun getPostWithComments(application: Application):LiveData<List<PostEntityWithCommentEntity>>?{
        return PostDatabase.getInstance(application)?.postDao()?.getPostWithComments()
    }

    private val database = PostDatabase.getInstance(application)
    
    private val commentDao: CommentDao =database!!.commentDao()
    var allComments =commentDao.allComments

    private val postDao:PostDao =database!!.postDao()
    var allPosts =postDao.allPosts
}