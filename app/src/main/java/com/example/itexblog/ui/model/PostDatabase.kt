package com.example.itexblog.ui.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.itexblog.ui.model.commentmodel.CommentDao
import com.example.itexblog.ui.model.commentmodel.CommentsEntity

/**
 * Database to handle storage of data in their respective data class(table)
 */

@Database(entities = [PostEntity::class, CommentsEntity::class], version = 8, exportSchema = false)
abstract class PostDatabase:RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao

    companion object{
        private var instance: PostDatabase? = null
        @kotlin.jvm.Synchronized
        fun getInstance(context: Context): PostDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    PostDatabase::class.java, "post_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }

//        private val roomCallback = object: Callback(){
//            override fun onCreate(db: SupportSQLiteDatabase) {
//                super.onCreate(db)
//                PopulateDbAsyncTask(instance).execute()
//
//            }
//
//        }




    }
}
