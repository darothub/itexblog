package com.example.itexblog.ui.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.itexblog.ui.model.commentmodel.CommentsEntity

data class PostEntityWithCommentEntity(
    @Embedded
    var postEntity: PostEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "post_id"
    )
    val commentsEntity: List<CommentsEntity>
) {
}