import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.twoshop.Model.Entities.PostEntity
import com.twoshop.R
import com.twoshop.ViewModel.EditPostViewModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


class EditPostFragment : Fragment() {
    private lateinit var post: PostEntity
    private lateinit var editPostViewModel: EditPostViewModel
    private lateinit var navController: NavController
    private lateinit var imageView: ImageView

    private lateinit var  aboutTextView: TextView
    private lateinit var  priceTextView: TextView
    private lateinit var  ownerTextView: TextView
    private lateinit var  phoneNumberTextView: TextView
    private lateinit var  locationTextView: TextView
    private lateinit var imageUrlRef : String
    private var imageUri: Uri? = null
    private lateinit var updatedImage: String
    private val imagePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                //upload the image to Firebase Storage
                imageView.setImageURI(imageUri)
                updatedImage = imageUri.toString()

                uploadImage()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.edit_post_fragment, container, false)

        // Get the PostEntity object from the arguments
        post = arguments?.getSerializable("post") as PostEntity
        // Find the views in the layout
        imageView = view.findViewById(R.id.edit_post_image)
        locationTextView =view.findViewById(R.id.locationText)
        aboutTextView = view.findViewById(R.id.edit_about_of_an_item)
        priceTextView = view.findViewById(R.id.edit_price_of_an_item)
        ownerTextView = view.findViewById(R.id.edit_owner_of_an_item)
        phoneNumberTextView = view.findViewById(R.id.edit_phone_of_an_item)

        // Set the values to the views
        Picasso.get().load(post.img).placeholder(R.drawable.place_holder_image)
            .into(imageView)
        aboutTextView.text = post.about
        priceTextView.text = post.price
        ownerTextView.text = post.owner
        locationTextView.text = post.location
        phoneNumberTextView.text = post.phone
        imageUrlRef= Uri.parse(post.img).toString()

        val navHostFragment: NavHostFragment = activity?.supportFragmentManager
            ?.findFragmentById(R.id.main_navhost_frag) as NavHostFragment
        navController = navHostFragment.navController

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationTextView =view.findViewById(R.id.edit_location_of_an_item)
        editPostViewModel = ViewModelProvider(this)[EditPostViewModel::class.java]
        val uploadImgBtn = view.findViewById<Button>(R.id.edit_upload_item_img)

        uploadImgBtn.setOnClickListener {

            imagePicker.launch("image/*")
        }
        // Find the submit button
        val submitButton: MaterialButton = view.findViewById(R.id.save_changes_post)
        submitButton.setOnClickListener {
            var newLocation = ""
            if(locationTextView.text.toString().isEmpty()){
                newLocation = post.location
            }else{
                newLocation = locationTextView.text.toString()
            }
            val editedPost = PostEntity(post.id, updatedImage , priceTextView.text.toString(), aboutTextView.text.toString(),
                phoneNumberTextView.text.toString(), newLocation, ownerTextView.text.toString(), post.uid)
            editPostViewModel.editPost(editedPost) { isSuccessful ->
                if (isSuccessful) {
                    navController.navigate(R.id.action_global_myUploadsFragment)
                }
            }
        }
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
                    imageUrlRef= imageUrl
                }.addOnFailureListener {e ->
                    //failed upload
                    Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { e ->
                //failed upload
                Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}