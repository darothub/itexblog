package com.example.itexblog.ui


import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.example.itexblog.ui.model.PostEntityWithCommentEntity
import com.example.itexblog.ui.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.fragment_blog_activities.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class BlogActivitiesFragment : Fragment() {

    private var postViewModel: PostViewModel?= null
    private var adapter:PostAdapter? = null
    var mActivity:MainActivity?=null

    var value:Boolean =false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog_activities, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)





        blog_activity_toolbar.setOnMenuItemClickListener{
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            val valueB = sharedPref!!.getBoolean("NewTheme", false)

            when(it.itemId){
                R.id.theme ->{

                    value = !valueB

                    sharedPref.edit()
                        ?.apply {
                            putBoolean("NewTheme", value)
                            apply()

                        }

                    Toast.makeText(context, "hey ${value}", Toast.LENGTH_LONG).show()
                    activity?.startActivity(Intent(context, MainActivity::class.java))
//
                    true
                }
                else -> false
            }
        }




        //View model to observe live data
        postViewModel= ViewModelProviders.of(this).get(PostViewModel::class.java)

        //Getting current day
        val sdf =SimpleDateFormat("dd/MM/yyyy hh:mm:ss" )
        val currentDate = sdf.format(Date())


        //On click listener to navigate to add post frag
        addFabBtn.setOnClickListener {

            addNewPost(it)
        }




//        postViewModel!!.insert(post1, Application())
//
//        postViewModel!!.insert(post2, Application())
//        postViewModel!!.deleteAll(Application())

        //Observing changes from the database via the view model
        postViewModel!!.getAllPosts()?.observe(this, object: Observer<List<PostEntity?>?> {
            override fun onChanged(postEntity: List<PostEntity?>?) {
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.setHasFixedSize(true)

                //An instance of the adapter
                adapter = PostAdapter(postEntity, object: PostAdapter.OnPostListener{
                    override fun onPostClick(postEntity: PostEntity?) {
                        view?.let{
                            readPost(postEntity, it)
                        }
                    }


                }, activity!!)
                //Connecting recycler view to adapter
                recyclerView.adapter = adapter
                adapter?.setPost(postEntity)
//                Toast.makeText(context, "$postEntity", Toast.LENGTH_LONG).show()
                Log.i("this class", "$postEntity")
            }

        })

        postViewModel!!.getPostWithComments(Application())?.observe(this, object :Observer<List<PostEntityWithCommentEntity>>{
            override fun onChanged(t: List<PostEntityWithCommentEntity>?) {
//                Toast.makeText(context, "hey: $t", Toast.LENGTH_SHORT).show()

            }

        })




        //Swipe listener for left swipe(to delete)
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
                        refresh()

                    }


                }.create().show()

            }

        }).attachToRecyclerView(recyclerView)

        //Swipe listener for right swipe(to read post)
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

    //Custom function to navigate to add post fragment
    fun addNewPost(view: View){

        val action = BlogActivitiesFragmentDirections.actionBlogActivitiesFragmentToAddPostFragment2()
        Navigation.findNavController(view).navigate(action)

    }
    //Custom function to navigate to read post fragment
    fun readPost(postEntity: PostEntity?, view: View){
        val action = BlogActivitiesFragmentDirections.actionBlogActivitiesFragmentToReadPostFragment()
        action.post = postEntity
        Navigation.findNavController(view).navigate(action)

    }

    private fun refresh(){
        findNavController().navigate(R.id.blogActivitiesFragment)
    }







}
