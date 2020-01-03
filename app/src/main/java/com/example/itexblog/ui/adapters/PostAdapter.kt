package com.example.itexblog.ui.adapters

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
import kotlinx.coroutines.launch


class PostAdapter(private var posts:List<PostEntity?>?, private var listener:OnPostListener):RecyclerView.Adapter<PostAdapter.PostHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_row, parent, false)

        return PostHolder(itemView)
    }

    override fun getItemCount(): Int {
        return posts!!.size
    }

    fun setPost(posts: List<PostEntity?>?){
        this.posts =posts as List<PostEntity>
        notifyDataSetChanged()
    }

    fun getPostAt(position: Int):PostEntity?{
        return posts?.get(position)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        posts?.let{
            val currentPost = it[position]
            holder.bind(it[position], listener)
        }

//        holder.title.text = currentPost.title
//        holder.body.text = currentPost.body
//        holder.post_date.text = currentPost.date
//        holder.num_of_likes.text = currentPost.likes.toString()


//
//        val image = currentPost.image
//
//        if(image != "null"){
//            val stringImageToUri = Uri.parse(currentPost.image.toString())
//            stringImageToUri.let {
//                Picasso.get().load(it).into(holder.post_image)
//            }
//            holder.post_image.visibility = View.VISIBLE
//            holder.divider.visibility = View.VISIBLE
//        }
//        holder.card.setOnClickListener {
//            val action = Navigation1.findNavController(it)
//        }

    }




    class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var stringImageToUri:Uri?=null

        var title = itemView.findViewById<TextView>(R.id.post_title)
        var body = itemView.findViewById<TextView>(R.id.post_body)
        var post_image = itemView.findViewById<ImageView>(R.id.post_image)
        var num_of_likes = itemView.findViewById<TextView>(R.id.num_of_likes)
        var post_date =itemView.findViewById<TextView>(R.id.post_date)
        var divider = itemView.findViewById<View>(R.id.divider)
        var deleteBtn = itemView.findViewById<View>(R.id.delete_btn)



        fun bind(postEntity: PostEntity?, listener: OnPostListener){
            val postViewModel: PostViewModel?= null
            title.setText(postEntity?.title)
            body.setText(postEntity?.body)
            post_date.setText(postEntity?.date)
            postEntity?.likes?.let{
                num_of_likes.setText(it.toString())
            }


            if(postEntity?.image != "null"){
                stringImageToUri = Uri.parse(postEntity?.image)

                //calling File Utils here is the reason for the crash
//                val imagePath = FileUtils.getPath(post_image.context, stringImageToUri)
//                val imageFile = File(imagePath)
//This code is not being called?

//                Picasso.get().load(imageFile).into(post_image)


                Picasso.get().load(stringImageToUri).into(post_image)
                post_image.visibility = View.VISIBLE
                divider.visibility = View.VISIBLE
            }
            Log.i("imageUri", "$stringImageToUri")

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


    }

    interface OnPostListener{
        fun onPostClick(postEntity: PostEntity?)
    }




}

