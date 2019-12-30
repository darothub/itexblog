package com.example.itexblog.ui.utils

import android.app.Application
import com.example.itexblog.ui.model.PostDao
import com.example.itexblog.ui.model.PostEntity
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
}
