package com.example.itexblog.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.itexblog.ui.model.PostEntity
import com.example.itexblog.ui.model.PostRepository

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

}
