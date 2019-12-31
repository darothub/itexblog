package com.example.itexblog.ui


import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI

import com.example.itexblog.R
import com.example.itexblog.ui.model.PostEntity
import com.example.itexblog.ui.viewmodel.PostViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_post.*
import kotlinx.android.synthetic.main.post_row.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddPostFragment : Fragment() {

    var imageUriLoader: Uri?=null

    private var postViewModel: PostViewModel?= null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_post, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val nav = Navigation.findNavController(add_post_appbar)
        NavigationUI.setupWithNavController(add_post_toolbar, nav)

        postViewModel= ViewModelProviders.of(this).get(PostViewModel::class.java)

        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss" )
        val currentDate = sdf.format(Date())

        addImageBtn.setOnClickListener {
            openGallery()
        }

        cameraBtn.setOnClickListener {
            openCamera()
        }

        submit_post_btn.setOnClickListener {
            val title = title.text.toString()
            val body = body.text.toString()
            val image = image_placeholder.drawable.toBitmap()
            val imageInt = image

            val saveRequest = savePost(Application(), title, body, imageUriLoader.toString())
            if(saveRequest){
                val action = AddPostFragmentDirections.actionGlobalBlogActivitiesFragment()
                Navigation.findNavController(it).navigate(action)
            }

//            val stringImageToUri = Uri.parse(currentPost.image.toString())


            Toast.makeText(context, imageUriLoader.toString(), Toast.LENGTH_SHORT).show()
            Log.i("Uri", imageUriLoader.toString())





        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        context?.let{
            loadImage(requestCode, image_placeholder, it, data)
        }

    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, 201)
    }

    private fun openCamera(){
        try{
            val CAMERA_REQUEST = 200
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST)
        }
        catch (e:Exception){
            Toast.makeText(context, "Please try again: ${e.message}", Toast.LENGTH_SHORT).show()
        }

    }

    private fun loadImage(requestCode: Int, imageView: ImageView, context: Context, data: Intent?){
        if(requestCode == 200){
            try{

                val image = data!!.extras?.get("data") as Bitmap


                imageView.setImageBitmap(image)
                imageView.visibility = View.VISIBLE

                imageUriLoader = getImageUriFromBitmap(context, image)
            }
            catch (e:Exception){
                Toast.makeText(context, "Please try againt: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        }

        else if(requestCode == 201){
            try{
                val imageUri = data!!.data
                imageUriLoader = imageUri
                Picasso.get().load(imageUri).into(imageView)

                imageView.visibility = View.VISIBLE
                imageView.setImageURI(imageUri)

            }
            catch (e:Exception){
                Toast.makeText(context, "Please try again: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        }
        else{
            Toast.makeText(context, "Invalid request", Toast.LENGTH_SHORT).show()
        }
    }

    private fun savePost(application: Application, title:String, body:String, image:String?=null): Boolean{

        val sdf =SimpleDateFormat("dd/MM/yyyy hh:mm:ss" )
        val currentDate = sdf.format(Date())

        if(title.isNotEmpty() && body.isNotEmpty()){
            try{
                val post =  PostEntity(title, body, image, currentDate)
                postViewModel!!.insert(post, application)
            }
            catch (e:Exception){
                Toast.makeText(context, "Please try again: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            return true

        }else{
            return false
        }


    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }


}
