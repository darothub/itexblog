package com.example.itexblog.ui


import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.itexblog.R
import com.example.itexblog.ui.model.PostDatabase
import com.example.itexblog.ui.model.PostEntity
import com.example.itexblog.ui.model.commentmodel.CommentsEntity
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
    private var imageFile:File? = null
    private var currentDate:String? = null
    private var addComment:Boolean?=null
    private var dataIntent:Intent?=null

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
            addComment = AddPostFragmentArgs.fromBundle(it).addComment
        }

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val changeTheme = sharedPref?.getBoolean("NewTheme", false)
        if(changeTheme!!){
            submit_post_btn.setBackgroundColor(resources.getColor(R.color.secondChoice) )
            update_post_btn.setBackgroundColor(resources.getColor(R.color.secondChoice) )
            add_comment_btn.setBackgroundColor(resources.getColor(R.color.secondChoice) )
        }

        //Attaching navigation to the app bar and toolbar
        val nav = Navigation.findNavController(add_post_appbar)
        NavigationUI.setupWithNavController(add_post_toolbar, nav)

        // Listening to onclick on menu item(s)
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


        add_post_progress.max = 200
        body.doOnTextChanged { text, start, count, after ->
            if(count != 200){
                add_post_progress.progress = text?.length!!
                text_length_advice.visibility = View.VISIBLE

                Toast.makeText(context, "${text.length}", Toast.LENGTH_SHORT).show()

            }
        }

        //PostViewModel to observe new changes
        postViewModel= ViewModelProviders.of(this).get(PostViewModel::class.java)

        //Getting date
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss" )
        currentDate = sdf.format(Date())


        //Checking if the content is an old post for update
        if(incomingPost != null && addComment == false){
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
                image_placeholder.visibility = View.GONE



            }


            //On-click listerner for update request
            update_post_btn.setOnClickListener {view ->

                val title = title.text.toString()
                val body = body.text.toString()
                val id = incomingPost?.id

                val updateRequestResult = updatePost(Application(), title, body,
                    if(imageUriLoader.toString() != "null") imageUriLoader.toString()
                    else if(removeImage(image_placeholder)) "null" else incomingPost?.image,
                    id)

                //When update is successful
                if(updateRequestResult){
                    val action = AddPostFragmentDirections.actionGlobalBlogActivitiesFragment()
                    Navigation.findNavController(view).navigate(action)
                    Toast.makeText(context, "post with ${incomingPost?.title } and ${incomingPost?.id } is updated", Toast.LENGTH_SHORT).show()
                }


            }
        }
        else if(addComment == true){
            add_comment_btn.visibility = View.VISIBLE
            submit_post_btn.visibility = View.GONE
            image_placeholder.visibility = View.GONE
            addImageBtn.visibility = View.GONE
            title.visibility = View.GONE
            body.setHint(resources.getString(R.string.comment))
            body.requestFocus()
            cameraBtn.visibility = View.GONE


            add_comment_btn.setOnClickListener {
                val message = body.text.toString()
                val addCommentRequest = saveComment(Application(), message, incomingPost!!)

                if(addCommentRequest){
                    val action = AddPostFragmentDirections.toReadComments()
                    action.post = incomingPost

                    Navigation.findNavController(it).navigate(action)
                }
            }
        }


        //On-click listener to open gallery
        addImageBtn.setOnClickListener {
            openGallery()
        }

        // On-click listener to open camera
        cameraBtn.setOnClickListener {
            openCamera()
        }

        //On-click listener to submit new post
        submit_post_btn.setOnClickListener {
            val title = title.text.toString()
            val body = body.text.toString()


            val saveRequest = savePost(Application(), title, body, imageUriLoader.toString())
//            Toast.makeText(context, "${imageUriLoader.toString()} is updated", Toast.LENGTH_SHORT).show()

            //when post request is successful
            if(saveRequest){
                val action = AddPostFragmentDirections.actionGlobalBlogActivitiesFragment()
                Navigation.findNavController(it).navigate(action)
            }
            else{
                Toast.makeText(context, "Empty field error", Toast.LENGTH_LONG).show()
            }

//            Toast.makeText(context, imageUriLoader.toString(), Toast.LENGTH_SHORT).show()
//            Log.i("Uri", imageUriLoader.toString())



        }



    }


    //Activity for gallery or camera request
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        dataIntent = data
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE), 202)
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 200)
        }
        else{
            context?.let{
                loadImage(requestCode, image_placeholder, it, data)
            }
        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val CAMERA_REQUEST = 201
        val GALLERY_REQUEST = 200

        when(requestCode){
            202 -> {
                if(grantResults.size > 0 && permissions[0].equals(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        context?.let{
                            loadImage(CAMERA_REQUEST, image_placeholder, it, dataIntent)
                            loadImage(GALLERY_REQUEST, image_placeholder, it, dataIntent)
                        }
                    }
                }
            }

        }
    }

    //Custom function to open gallery
    private fun openGallery(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//        intent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE);


        if(intent.resolveActivity(activity!!.packageManager) != null){
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(intent, 200)
        }


    }

    //Custom function to open camera
    private fun openCamera(){
        try{
            val CAMERA_REQUEST = 201
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST)
        }
        catch (e:Exception){
            Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show()
        }

    }

    //Custom function to react to request from camera and picture result
    private fun loadImage(requestCode: Int, imageView: ImageView, context: Context, data: Intent?){
        try{

            if(requestCode == 201){

                //Convert result to bitmap
                val image = data!!.extras?.get("data") as Bitmap

                //Show result in the image_placeholder
                imageView.setImageBitmap(image)
                imageView.visibility = View.VISIBLE

                //Extract Uri version from Bitmap
                imageUriLoader = getImageUriFromBitmap(context, image)


            }

            //When image is from the gallery
            else if(requestCode == 200){

                //Extract Uri version from data
                val imageUri = data!!.data


//                //Get absolute path and store it
//                imagePath = FileUtils.getPath(context, imageUri)
                imageUriLoader = imageUri




                imageView.visibility = View.VISIBLE
//                Picasso.get().load(imageUri).into(imageView)
                imageView.setImageURI(imageUri)


            }
            else{
                Toast.makeText(context, "Invalid request", Toast.LENGTH_SHORT).show()
            }

        }
        catch (e:Exception){

            Log.i("Picture/Camera", "Please try again")
//            Toast.makeText(context, "Please try again: ${e.message}", Toast.LENGTH_SHORT).show()
        }
        //When image is from the camera

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
                Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show()
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


    private fun updatePost(context: Context?, title: String, body: String, image: String?, id: Int?):Boolean{

        return if(title.isNotEmpty() && body.isNotEmpty()){
            try{
                CoroutineScope(Main).launch {
                    //                    postViewModel!!.update(postEntity, application)
                    val updatedPost = PostEntity(title, body, image, currentDate)
                    if (id != null) {
                        updatedPost.id = id
                    }
                    PostDatabase.getInstance(context!!)?.postDao()?.update(updatedPost)
                }

            } catch (e:Exception){
                Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show()
            }
            true

        }else{
            Toast.makeText(context, "Empty field error", Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun removeImage(imageView: ImageView):Boolean{

        return try{
            imageView.setImageURI(null)
            removedImage = true
            true
        } catch(e:Exception) {

            Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show()
            removedImage = false
            false
        }


    }

    private fun saveComment(application: Application, message:String, postEntity: PostEntity): Boolean{

        val sdf =SimpleDateFormat("dd/MM/yyyy hh:mm:ss" )
        val currentDate = sdf.format(Date())

        if(message.isNotEmpty()){
            try{
                val comment =  CommentsEntity(message, postEntity.id, currentDate)
                postViewModel!!.insertComment(comment, application)
            }
            catch (e:Exception){
                Toast.makeText(context, "Please try again: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            return true

        }else{
            Toast.makeText(context, "Empty field error", Toast.LENGTH_LONG).show()
            return false
        }


    }


}