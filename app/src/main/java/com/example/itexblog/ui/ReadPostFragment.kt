package com.example.itexblog.ui


import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation

import com.example.itexblog.R
import com.example.itexblog.ui.model.PostEntity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_blog_activities.*
import kotlinx.android.synthetic.main.fragment_read_post.*

/**
 * A simple [Fragment] subclass.
 */
class ReadPostFragment : Fragment() {

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
            Picasso.get().load(stringImageToUri).into(read_image)
            read_image.visibility = View.VISIBLE
            read_divider.visibility = View.VISIBLE
        }

        read_edit_btn.setOnClickListener {
            toEdit(it, incomingPost)
        }

    }

    private fun toEdit(view:View, postEntity: PostEntity?){
        val action = ReadPostFragmentDirections.actionReadPostFragmentToAddPostFragment()
        action.post = postEntity
        Navigation.findNavController(view).navigate(action)

    }


}


