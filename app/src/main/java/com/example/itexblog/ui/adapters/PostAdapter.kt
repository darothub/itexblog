package com.example.itexblog.ui.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.itexblog.R
import com.example.itexblog.ui.model.PostDatabase
import com.example.itexblog.ui.model.PostEntity
import com.example.itexblog.ui.viewmodel.PostViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch


class PostAdapter(private var posts:List<PostEntity?>?, private var listener:OnPostListener):RecyclerView.Adapter<PostAdapter.PostHolder>() {
    //To inflate post row in the recycler view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_row, parent, false)

        return PostHolder(itemView)
    }

    override fun getItemCount(): Int {
        return posts!!.size
    }

    // To set current post list
    fun setPost(posts: List<PostEntity?>?){
        this.posts = posts as List<PostEntity>
        notifyDataSetChanged()
    }

    //To get position of post
    fun getPostAt(position: Int):PostEntity?{
        return posts?.get(position)
    }

    //To bind xml items in post row
    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        posts?.let{
            val currentPost = it[position]
            holder.bind(it[position], listener)
        }


    }


    class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var stringImageToUri:Uri?=null

        var title = itemView.findViewById<TextView>(R.id.post_title)
        var body = itemView.findViewById<TextView>(R.id.post_body)
        var post_image = itemView.findViewById<ImageView>(R.id.post_image)
        var num_of_likes = itemView.findViewById<TextView>(R.id.num_of_likes)
        var post_date =itemView.findViewById<TextView>(R.id.post_date)
        var divider = itemView.findViewById<View>(R.id.divider2)
        var deleteBtn = itemView.findViewById<View>(R.id.delete_btn)
        var likeBtn = itemView.findViewById<ImageView>(R.id.like_btn)
        var postComments = itemView.findViewById<TextView>(R.id.post_comments)



        //Function to bind each post with a listener for click
        fun bind(postEntity: PostEntity?, listener: OnPostListener){
            val postViewModel: PostViewModel?= null
            title.setText(postEntity?.title)
            body.setText(postEntity?.body)
            post_date.setText(postEntity?.date)
            postEntity?.likes?.let{
                num_of_likes.setText(it.toString())
            }
            CoroutineScope(Main).launch {

                var numOfComments = CommentAdapter.CommentHolder(itemView).getCommentsList(postComments.context, postEntity!!)

                Log.i("Commentnum", "$numOfComments")

                if(numOfComments == null){
                    numOfComments = postEntity?.comments
                }else{
                    postEntity?.comments = numOfComments

                }
                postComments.setText("$numOfComments Comments")
            }



            //when post image is not null
            if(postEntity?.image != "null"){
                stringImageToUri = Uri.parse(postEntity?.image)

//                post_image.setImageURI(Uri.parse(postEntity?.image))
                Picasso.get().load(stringImageToUri).into(post_image)
                post_image.visibility = View.VISIBLE
                divider.visibility = View.VISIBLE
            }
            Log.i("imageUri", "$stringImageToUri")

            likeBtn.setOnClickListener{
                likesCount(postEntity, it.context)
                return@setOnClickListener

            }

            deleteBtn.setOnClickListener {view ->

                postEntity?.let{
                    CoroutineScope(IO).launch {
                        PostDatabase.getInstance(view.context)?.postDao()?.delete(it)
                    }
                    Toast.makeText(view.context, "${postEntity.title} deleted", Toast.LENGTH_SHORT).show()
                }

            }

            itemView.setOnClickListener {
                listener.onPostClick(postEntity)
            }

        }

        //Custom to handle like button on click
        fun likesCount(postEntity: PostEntity?, context: Context){
            postEntity!!.likes = postEntity.likes?.plus(1)
            Toast.makeText(context, "${postEntity.likes}", Toast.LENGTH_SHORT).show()
            CoroutineScope(IO).launch {
                PostDatabase.getInstance(context)?.postDao()?.update(postEntity)
            }

        }




    }



    interface OnPostListener{
        fun onPostClick(postEntity: PostEntity?)
    }




}

