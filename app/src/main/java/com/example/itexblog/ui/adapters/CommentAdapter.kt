package com.example.itexblog.ui.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.itexblog.R
import com.example.itexblog.ui.model.PostDatabase
import com.example.itexblog.ui.model.PostEntity
import com.example.itexblog.ui.model.commentmodel.CommentsEntity

class CommentAdapter(var comments:List<CommentsEntity?>?, private var listener:OnCommentListener):
    RecyclerView.Adapter<CommentAdapter.CommentHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.CommentHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.comment_row, parent, false)

        return CommentAdapter.CommentHolder(itemView)

    }

    override fun getItemCount(): Int {
        return comments!!.size
    }


    fun setComment(comments: List<CommentsEntity?>?){
        this.comments =comments as List<CommentsEntity>
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CommentHolder, position: Int) {
        comments?.let{
            val currentComment = it[position]
            holder.bind(it[position], listener)
        }
    }

    class CommentHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var stringImageToUri: Uri?=null
        var postDatabase:PostDatabase?= null
        var commentsList:List<CommentsEntity>?= null

        var message = itemView.findViewById<TextView>(R.id.comment_body)
        var num_of_likes = itemView.findViewById<TextView>(R.id.comment_num_of_likes)
        var comment_date =itemView.findViewById<TextView>(R.id.comment_date)
        var divider = itemView.findViewById<View>(R.id.comment_divider)
        var deleteBtn = itemView.findViewById<View>(R.id.comment_delete_btn)
        var likeBtn = itemView.findViewById<ImageView>(R.id.comment_like_btn)



        //Function to bind each post with a listener for click
        fun bind(comment: CommentsEntity?, listener: CommentAdapter.OnCommentListener){
            message.setText(comment?.message)
            comment_date.setText(comment?.date)

        }

        //Custom to handle like button on click
        fun likesCount(comment: CommentsEntity?, context: Context){


        }

        suspend fun getCommentsList( context: Context, postEntity: PostEntity): Int? {

            commentsList = PostDatabase.getInstance(context)?.commentDao()?.getCommentById(postEntity.id)


            Log.i("Comment adapater", "${commentsList?.size}")

            return commentsList?.let{
                it.size
            }
        }

    }

    interface OnCommentListener{
        fun onCommentClick(comment: CommentsEntity?)
    }


}