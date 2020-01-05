package com.example.itexblog.ui.model.commentmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class CommentsEntity(
    var message:String,
    @ColumnInfo(name = "post_id")
    var postEntityId: Int,
    var date: String?

): Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


//@Entity(foreignKeys = arrayOf(ForeignKey(entity = PostEntity::class,
//    parentColumns = arrayOf("id"),
//    childColumns = arrayOf("comment_id"),
//    onDelete = ForeignKey.CASCADE)))
//class CommentsEntity(
//    var message:String,
//    @ColumnInfo(name = "comment_id")
//    var commentId: Int,
//    var date: String?
//
//): Serializable {
//    @PrimaryKey(autoGenerate = true)
//    var id: Int = 0
//}