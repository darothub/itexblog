package com.example.itexblog.ui


import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.itexblog.R
import com.example.itexblog.ui.adapters.PostAdapter
import com.example.itexblog.ui.model.PostEntity
import com.example.itexblog.ui.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.fragment_blog_activities.*
import kotlinx.android.synthetic.main.post_row.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class BlogActivitiesFragment : Fragment() {

    private var postViewModel: PostViewModel?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog_activities, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //attach nav-controller to the parent view-group
//        val navController = Navigation.findNavController(post_appbar)
//
//        NavigationUI.setupWithNavController(post_toolbar, navController)

//        recyclerView.layoutManager = LinearLayoutManager(context)
//        recyclerView.setHasFixedSize(true)
//        val adapter = PostAdapter()
//        recyclerView.adapter = adapter

        postViewModel= ViewModelProviders.of(this).get(PostViewModel::class.java)

        val sdf =SimpleDateFormat("dd/MM/yyyy hh:mm:ss" )
        val currentDate = sdf.format(Date())

        val post1 = PostEntity("The news",
                """I have been a fan of ManchesterUnited for decades
                    I have been a fan of ManchesterUnited for decades
                    I have been a fan of ManchesterUnited for decades
                """.trim().trimMargin(), null, currentDate
            )

        val post2 = PostEntity("The news",
            """I have been a fan of ManchesterUnited for years
                    I have been a fan of ManchesterUnited for years
                    I have been a fan of ManchesterUnited for years
                """.trim().trimMargin(), null, currentDate
        )


        addNewPost()


//        postViewModel!!.insert(post1, Application())
//
//        postViewModel!!.insert(post2, Application())
//        postViewModel!!.deleteAll(Application())
        postViewModel!!.getAllPosts()?.observe(this, object: Observer<List<PostEntity?>?> {
            override fun onChanged(postEntity: List<PostEntity?>?) {
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.setHasFixedSize(true)
                val adapter = PostAdapter(postEntity, object: PostAdapter.OnPostListener{
                    override fun onPostClick(postEntity: PostEntity?) {
                        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show()
                    }


                })
                recyclerView.adapter = adapter
                adapter.setPost(postEntity)
//                Toast.makeText(context, "$postEntity", Toast.LENGTH_LONG).show()
                Log.i("this class", "$postEntity")
            }

        })
    }

    fun addNewPost(){
        addFabBtn.setOnClickListener {

            val action = BlogActivitiesFragmentDirections.toAddPost()
            Navigation.findNavController(it).navigate(action)
        }
    }



}
