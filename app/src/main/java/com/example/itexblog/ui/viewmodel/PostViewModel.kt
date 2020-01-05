package com.example.itexblog.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.itexblog.ui.model.PostEntity
import com.example.itexblog.ui.model.PostEntityWithCommentEntity
import com.example.itexblog.ui.model.PostRepository
import com.example.itexblog.ui.model.commentmodel.CommentsEntity

class PostViewModel(application: Application): AndroidViewModel(application) {

    private var repository = PostRepository(application)
    private var allPosts = repository.allPosts

    fun insert(post:PostEntity, application: Application){
        repository.insert(post, application)
    }

    fun update(post:PostEntity, application: Application){
        repository.update(post, application)
    }

    fun delete(post:PostEntity, application: Application){
        repository.delete(post, application)
    }

    fun deleteAll(application: Application){
        repository.deleteAll(application)
    }

    fun getAllPosts():LiveData<List<PostEntity?>?>?{
        return allPosts
    }


    private var allComments = repository.allComments

    fun insertComment(commentsEntity: CommentsEntity, application: Application){
        repository.insertComment(commentsEntity, application)
    }

    fun updateComment(commentsEntity: CommentsEntity, application: Application){
        repository.updateComment(commentsEntity, application)
    }

    fun deleteComment(commentsEntity: CommentsEntity, application: Application){
        repository.deleteComment(commentsEntity, application)
    }

    fun deleteAllComments(application: Application){
        repository.deleteAllComments(application)
    }

    fun getAllComments(): LiveData<List<CommentsEntity?>?>?{
        return allComments
    }
    fun getAllCommentsByIdLive(application: Application, id:Int):LiveData<List<CommentsEntity?>?>?{
        return repository.getCommentsByIdLive(application, id)
    }
    fun getPostWithComments(application: Application):LiveData<List<PostEntityWithCommentEntity>>?{
        return repository.getPostWithComments(application)
    }

}
