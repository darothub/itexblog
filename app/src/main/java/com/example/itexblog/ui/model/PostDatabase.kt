package com.example.itexblog.ui.model

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [PostEntity::class], version = 1, exportSchema = false)
abstract class PostDatabase:RoomDatabase() {

    abstract fun postDao(): PostDao

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
