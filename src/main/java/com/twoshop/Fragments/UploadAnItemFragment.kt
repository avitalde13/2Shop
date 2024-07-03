package com.twoshop.Fragments
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.twoshop.Model.Entities.PostEntity
import com.twoshop.R
import com.twoshop.ViewModel.UploadPostViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class UploadAnItemFragment : Fragment() {
    private lateinit var uploadPostViewModel: UploadPostViewModel
    private lateinit var navController: NavController
    lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    // Get the FirebaseStorage instance
    private lateinit var storageRef: StorageReference
    private var imageUri: Uri? = null
    private lateinit var imageView: ImageView
    private lateinit var imageUrlRef: String
    private lateinit var ageEditText: EditText
    private lateinit var aboutEditText: EditText
    private lateinit var locationEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var ownerEditText: EditText

    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                //upload the image to Firebase Storage
                imageView.setImageURI(imageUri)
                uploadImage()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val navHostFragment: NavHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.main_navhost_frag) as NavHostFragment
        navController = navHostFragment.navController
        val view = inflater.inflate(R.layout.fragment_upload_an_item, container, false)


        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel instance
        uploadPostViewModel = ViewModelProvider(this)[UploadPostViewModel::class.java]

        // Initialize ImageView for displaying selected image
        imageView = view.findViewById(R.id.post_image)

        // Initialize Firebase Storage
        storageRef = FirebaseStorage.getInstance().reference

        // Initialize image picker
        val uploadImgBtn = view.findViewById<Button>(R.id.upload_item_img)
        uploadImgBtn.setOnClickListener {
            imagePicker.launch("image/*")
        }

        // Get the current user
        val user = Firebase.auth.currentUser


        ageEditText = view.findViewById(R.id.age_of_an_item)
        aboutEditText = view.findViewById(R.id.about_of_an_item)
        phoneEditText = view.findViewById(R.id.phone_of_an_Item)
        locationEditText = view.findViewById(R.id.location_of_an_item)
        ownerEditText = view.findViewById(R.id.owner_of_an_item)


        // Handle upload button click
        val uploadPostBtn = view.findViewById<Button>(R.id.upload_item)
        uploadPostBtn.setOnClickListener {
            val age = ageEditText.text.toString()
            val about = aboutEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val location = locationEditText.text.toString()
            val owner = ownerEditText.text.toString()


            if (validate(age, about, phone, location, owner)) {
                // Upload post to Firebase
                if (user != null) {
                    val uid = user.uid
                    val post = PostEntity("", imageUrlRef, age, about, phone, location, owner, uid)
                    uploadPostViewModel.uploadPost(post) { isSuccessful ->
                        if (isSuccessful) {
                            Toast.makeText(context, "Uploaded successfully", Toast.LENGTH_SHORT).show()
                            // Navigate to MyUploadsFragment
                            navController.navigate(R.id.action_global_myUploadsFragment)
                        } else {
                            Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                // Display error message if validation fails
                Toast.makeText(context, "Please fill in all the information correctly", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun validate(
       age: String, about: String, phone: String, location: String, owner: String

    ): Boolean {
//        val isKindValid = kind.isNotEmpty()
        val isAgeValid = age.isNotEmpty()
        val isAgeBiggerThen0 = age.toFloat()
        val isAboutValid = about.isNotEmpty()
        val isPhoneValid = phone.isNotEmpty()
        val isPhoneLongEnough = phone.length
        val isLocationValid = location.isNotEmpty()
        val isOwnerValid = owner.isNotEmpty()


        return isAgeValid && isAgeBiggerThen0 > 0 && isAboutValid && isPhoneValid && isPhoneLongEnough == 10 && isLocationValid && isOwnerValid
    }

    private fun uploadImage() {
        imageUri?.let {
            val storageReference = FirebaseStorage.getInstance()
                .getReference("imagePosts/${System.currentTimeMillis()}.jpg")
            storageReference.putFile(it).addOnSuccessListener { taskSnapshot ->
                // Image uploaded successfully
                Toast.makeText(context, "Image uploaded", Toast.LENGTH_SHORT).show()
                // Get the download URL of the uploaded image
                storageReference.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    imageUrlRef = imageUrl

                }.addOnFailureListener { e ->
                    // Handle failed upload
                    Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }

            }.addOnFailureListener { e ->
                // Handle failed upload
                Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}



