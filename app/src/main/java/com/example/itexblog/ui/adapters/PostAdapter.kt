package com.example.itexblog.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.AdapterViewBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.itexblog.R
import com.example.itexblog.ui.model.PostEntity
import com.squareup.picasso.Picasso
import org.w3c.dom.Text

class PostAdapter:RecyclerView.Adapter<PostAdapter.PostHolder>() {


    private var posts:List<PostEntity> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_row, parent, false)

        return PostHolder(itemView)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun setPost(posts: List<PostEntity?>?){
        this.posts =posts as List<PostEntity>
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        val currentPost = posts[position]
        holder.title.text = currentPost.title
        holder.body.text = currentPost.body
        holder.post_date.text = currentPost.date
        holder.num_of_likes.text = currentPost.likes.toString()
        holder.post_image.visibility = View.GONE
        holder.divider2.visibility = View.GONE

        val image = currentPost.image

        if(currentPost.image != null){
            val stringImageToUri = Uri.parse(currentPost.image.toString())
            stringImageToUri.let {
                Picasso.get().load(it).into(holder.post_image)
            }
            holder.post_image.visibility = View.VISIBLE
            holder.divider2.visibility = View.VISIBLE
        }




    }



    class PostHolder(view: View) : RecyclerView.ViewHolder(view){
        var title = view.findViewById<TextView>(R.id.post_title)
        var body = view.findViewById<TextView>(R.id.post_body)
        var post_image = view.findViewById<ImageView>(R.id.post_image)
        var num_of_likes = view.findViewById<TextView>(R.id.num_of_likes)
        var post_date =view.findViewById<TextView>(R.id.post_date)
        var divider2 = view.findViewById<View>(R.id.divider2)
    }

}

