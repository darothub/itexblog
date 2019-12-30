package com.example.itexblog.ui.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

@Entity
class PostEntity(
    val title: String,
    val body: String,
    val image:Int?,
    val date: String?


): Serializable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var likes:Int? = 0



}


