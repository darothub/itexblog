package com.example.itexblog.ui


import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.itexblog.R
import com.example.itexblog.ui.adapters.PostAdapter
import com.example.itexblog.ui.model.PostEntity
import com.example.itexblog.ui.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.fragment_blog_activities.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class BlogActivitiesFragment : Fragment() {
    var i =0

    private var postViewModel: PostViewModel?= null
    private var adapter:PostAdapter? = null

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

//        val post1 = PostEntity("The news",
//                """I have been a fan of ManchesterUnited for decades
//                    I have been a fan of ManchesterUnited for decades
//                    I have been a fan of ManchesterUnited for decades
//                """.trim().trimMargin(), null, currentDate
//            )
//
//        val post2 = PostEntity("The news",
//            """I have been a fan of ManchesterUnited for years
//                    I have been a fan of ManchesterUnited for years
//                    I have been a fan of ManchesterUnited for years
//                """.trim().trimMargin(), null, currentDate
//        )

        addFabBtn.setOnClickListener {

            addNewPost(it)
        }




//        postViewModel!!.insert(post1, Application())
//
//        postViewModel!!.insert(post2, Application())
//        postViewModel!!.deleteAll(Application())
        postViewModel!!.getAllPosts()?.observe(this, object: Observer<List<PostEntity?>?> {
            override fun onChanged(postEntity: List<PostEntity?>?) {
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.setHasFixedSize(true)

                adapter = PostAdapter(postEntity, object: PostAdapter.OnPostListener{
                    override fun onPostClick(postEntity: PostEntity?) {
                        view?.let{
//                            readPost(postEntity, it)
                        }
                        i=i.plus(1)
                        val handler = Handler()
                        val runn = Runnable {
                             i = 0
                        }
                        if(i == 1){
                            Toast.makeText(context, "Single Clicked", Toast.LENGTH_SHORT).show()
                            handler.postDelayed(runn, 400)
                        }
                        else if(i == 2){
                            Toast.makeText(context, "Double Clicked", Toast.LENGTH_SHORT).show()
                            PostAdapter.PostHolder(view!!).likesCount(postEntity, context!!)

                        }
                    }


                })
                recyclerView.adapter = adapter
                adapter?.setPost(postEntity)
//                Toast.makeText(context, "$postEntity", Toast.LENGTH_LONG).show()
                Log.i("this class", "$postEntity")
            }

        })

        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AlertDialog.Builder(context).apply {
                    setTitle("Are you sure?")
                    setMessage("You cannot undo this operation")
                    setPositiveButton("Yes"){_, _ ->
                        postViewModel?.delete(adapter?.getPostAt(viewHolder.adapterPosition)!!, Application())
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                    }
                    setNegativeButton("No"){_, _ ->
                        findNavController().navigate(R.id.blogActivitiesFragment)

                    }


                }.create().show()

            }

        }).attachToRecyclerView(recyclerView)

        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                readPost(adapter?.getPostAt(viewHolder.adapterPosition)!!, view!!)
                Toast.makeText(context, "Read", Toast.LENGTH_SHORT).show()
            }

        }).attachToRecyclerView(recyclerView)
    }

    fun addNewPost(view: View){

        val action = BlogActivitiesFragmentDirections.actionBlogActivitiesFragmentToAddPostFragment2()
        Navigation.findNavController(view).navigate(action)

    }

    fun readPost(postEntity: PostEntity?, view: View){


            val action = BlogActivitiesFragmentDirections.actionBlogActivitiesFragmentToReadPostFragment()
            action.post = postEntity
            Navigation.findNavController(view).navigate(action)

    }



}
