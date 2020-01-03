package com.example.itexblog.ui


import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.itexblog.R
import com.example.itexblog.ui.model.PostDatabase
import com.example.itexblog.ui.model.PostEntity
import com.example.itexblog.ui.utils.FileUtils
import com.example.itexblog.ui.viewmodel.PostViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_add_post.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class AddPostFragment : Fragment() {

    private var imageUriLoader: Uri?=null
    private var incomingPost:PostEntity?=null
    private var removedImage:Boolean?=null
    private var imagePath:String?=null

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

        arguments?.let{
            incomingPost =  AddPostFragmentArgs.fromBundle(it).post

        }


        val nav = Navigation.findNavController(add_post_appbar)
        NavigationUI.setupWithNavController(add_post_toolbar, nav)

        postViewModel= ViewModelProviders.of(this).get(PostViewModel::class.java)

        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss" )
        val currentDate = sdf.format(Date())


        if(incomingPost != null){
            title.setText(incomingPost?.title)
            body.setText(incomingPost?.body)
            update_post_btn.visibility = View.VISIBLE
            submit_post_btn.visibility = View.GONE
            if(incomingPost?.image !="null"){
                val imageUri = Uri.parse(incomingPost?.image)
                Picasso.get().load(imageUri).into(image_placeholder)
                image_placeholder.visibility = View.VISIBLE
            }
            else{

            }


            update_post_btn.setOnClickListener {view ->

                incomingPost?.let {
                    it.title = title.text.toString()
                    it.body = body.text.toString()
                    it.date = currentDate
                    removedImage?.let{imageRemoved ->
                        if(imageRemoved && image_placeholder.visibility==View.GONE){
                            it.image = "null"
                        }
                        else if(imageUriLoader == null && image_placeholder.visibility==View.VISIBLE){
                            it.image = it.image
                        }
                        else{
                            it.image = imageUriLoader.toString()

                        }
                    }
                }
                val result = updatePost(context, incomingPost!!)
                if(result){
                    val action = AddPostFragmentDirections.actionGlobalBlogActivitiesFragment()
                    Navigation.findNavController(view).navigate(action)
                    Toast.makeText(context, "${imageUriLoader.toString()} is updated", Toast.LENGTH_SHORT).show()
                }


            }
        }


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

        add_post_toolbar.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.remove_image ->{
                    val result = removeImage(image_placeholder)
                    if(result){
                        image_placeholder.visibility = View.GONE
                        Toast.makeText(context, "Image removed", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            true
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        context?.let{
            loadImage(requestCode, image_placeholder, it, data)
        }

    }

    private fun openGallery(){



        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        if(intent.resolveActivity(activity!!.packageManager) != null){
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(intent, 201)
        }


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
                imagePath = FileUtils.getPath(context, imageUri)
                imageUriLoader = imageUri
//                Picasso.get().load(imageUri).into(imageView)
                //Checking if picasso is able to load full path




                val imageFile = File(imagePath)

                Picasso.get().load(imageFile).into(imageView)

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

    private fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri{
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }


    private fun updatePost(context: Context?, postEntity: PostEntity):Boolean{
        val title = postEntity.title
        val body = postEntity.body
        return if(title.isNotEmpty() && body.isNotEmpty()){
            try{
                CoroutineScope(Main).launch {
                    //                    postViewModel!!.update(postEntity, application)
                    PostDatabase.getInstance(context!!)?.postDao()?.update(postEntity)
                }

            } catch (e:Exception){
                Toast.makeText(context, "Please try again: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            true

        }else{
            false
        }
    }

    private fun removeImage(imageView: ImageView):Boolean{

        return try{
            imageView.setImageURI(null)
            removedImage = true
            true
        } catch(e:Exception) {

            Toast.makeText(context, "Please try again: ${e.message}", Toast.LENGTH_SHORT).show()
            removedImage = false
            false
        }





    }


}