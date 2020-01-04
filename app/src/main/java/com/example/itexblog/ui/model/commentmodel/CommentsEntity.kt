package com.example.itexblog.ui.model.commentmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.itexblog.ui.model.PostEntity
import java.io.Serializable

@Entity(foreignKeys = arrayOf(ForeignKey(entity = PostEntity::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("comment_id"),
    onDelete = ForeignKey.CASCADE)))
class CommentsEntity(
    var message:String,
    @ColumnInfo(name = "comment_id")
    var commentId: String
): Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
