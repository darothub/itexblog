package com.example.itexblog.ui.model

import android.app.Application
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

    private val database = PostDatabase.getInstance(application)
    private val postDao:PostDao =database!!.postDao()
    var allPosts =postDao.allPosts
}