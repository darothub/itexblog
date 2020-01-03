package com.example.itexblog.ui


import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.itexblog.R
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


        arguments?.let{
            incomingPost =  ReadPostFragmentArgs.fromBundle(it).post
        }

        read_title.text = incomingPost?.title
        read_date.text = incomingPost?.date
        read_body.setText(incomingPost?.body)

        if(incomingPost?.image != "null") {
            val stringImageToUri = Uri.parse(incomingPost?.image)

            //Hi Attish this part handles incoming argument from another fragments.

            // for instance I am sending the clicked post data here for edit

            // I think you probably wanna do something in the adapterhere
            val imagePath = FileUtils.getPath(context, stringImageToUri)
            val imageFile = File(imagePath)

            Picasso.get().load(stringImageToUri).into(read_image)
            read_image.visibility = View.VISIBLE
            read_divider.visibility = View.VISIBLE
        }

        read_edit_btn.setOnClickListener {
            toEdit(it, incomingPost)
        }
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
        val gesture = GestureDetector(context, object :GestureDetector.SimpleOnGestureListener(){
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                incomingPost!!.likes = incomingPost!!.likes?.plus(1)
                Toast.makeText(context, "${incomingPost?.likes}", Toast.LENGTH_SHORT).show()
                postViewModel!!.update(incomingPost!!, Application())
                return super.onDoubleTap(e)

            }
        })

        reader_card_container.setOnTouchListener { v, event ->
            Toast.makeText(context, "tapped", Toast.LENGTH_SHORT).show()
            gesture.onTouchEvent(event)
        }


    }


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


