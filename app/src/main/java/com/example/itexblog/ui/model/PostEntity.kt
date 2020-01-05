package com.example.itexblog.ui.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class PostEntity(
    var title: String,
    var body: String,
    var image: String? = null,
    var date: String?


): Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var likes:Int? = 0

    var comments:Int?=0



}