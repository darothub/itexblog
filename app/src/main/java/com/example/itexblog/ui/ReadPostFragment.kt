package com.example.itexblog.ui


import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.itexblog.R
import com.example.itexblog.ui.adapters.PostAdapter
import com.example.itexblog.ui.model.PostDatabase
import com.example.itexblog.ui.model.PostEntity
import com.example.itexblog.ui.utils.FileUtils
import com.example.itexblog.ui.viewmodel.PostViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_read_post.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

/**
 * A simple [Fragment] subclass.
 */
class ReadPostFragment : Fragment() {
    var i =0

    private var postViewModel: PostViewModel?= null
    var incomingPost:PostEntity?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_read_post, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        //Receives arguments from other fragment
        arguments?.let{
            incomingPost =  ReadPostFragmentArgs.fromBundle(it).post
        }

        read_title.text = incomingPost?.title
        read_date.text = incomingPost?.date
        read_body.setText(incomingPost?.body)
        read_num_of_likes.setText(incomingPost?.likes.toString())


        //When incoming post has an image
        if(incomingPost?.image != "null") {
            val stringImageToUri = Uri.parse(incomingPost?.image)
            val imagePath = FileUtils.getPath(context, stringImageToUri)
            val imageFile = File(imagePath)

            Picasso.get().load(stringImageToUri).into(read_image)
            read_image.visibility = View.VISIBLE
            read_divider.visibility = View.VISIBLE
        }

        //On click listener for editing
        read_edit_btn.setOnClickListener {
            toEdit(it, incomingPost)
        }
        //On click listener for deleting
        read_delete_btn.setOnClickListener {
            AlertDialog.Builder(it.context).apply {
                setTitle("Are you sure?")
                setMessage("You cannot undo this operation")
                setPositiveButton("Yes"){_, _ ->
                    if(toDelete(context, incomingPost)){
                        Toast.makeText(context, "$incomingPost Deleted", Toast.LENGTH_SHORT).show()
                        toBlogPost(it)
                    }
                }
                setNegativeButton("No"){_, _ ->

                }


            }.create().show()


        }
        //On click listener for liking
        read_like_btn.setOnClickListener {
            PostAdapter.PostHolder(view!!).likesCount(incomingPost, context!!)
            read_num_of_likes.setText(incomingPost?.likes.toString())
        }
        //On click listener for double tap to like post
        reader_card_container.setOnClickListener {
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
                //update post
                PostAdapter.PostHolder(view!!).likesCount(incomingPost, context!!)
                read_num_of_likes.setText(incomingPost?.likes.toString())


            }
            return@setOnClickListener
        }



    }


    //Custom function to navigate to add post fragment for editing
    private fun toEdit(view:View, postEntity: PostEntity?){
        val action = ReadPostFragmentDirections.actionReadPostFragmentToAddPostFragment()
        action.post = postEntity
        Navigation.findNavController(view).navigate(action)

    }

    private fun toBlogPost(view: View){
        val action = ReadPostFragmentDirections.toBlogPost()
        Navigation.findNavController(view).navigate(action)
    }
    private fun toDelete(context: Context, postEntity: PostEntity?):Boolean{
        return try{
            postEntity?.let{
                CoroutineScope(Dispatchers.IO).launch {
                    PostDatabase.getInstance(context)?.postDao()?.delete(it)
                }
                Toast.makeText(context, "${postEntity.title} deleted", Toast.LENGTH_SHORT).show()
            }
            true
        } catch(e:Exception) {

            Toast.makeText(context, "Please try again: ${e.message}", Toast.LENGTH_SHORT).show()

            false
        }

    }


}


