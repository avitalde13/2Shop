package com.twoshop.Adapters

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.RecyclerView
import com.twoshop.Model.Entities.PostEntity
import com.twoshop.R
import com.twoshop.ViewModel.MyUploadsViewModel
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso

class MyUploadsAdapter(private val viewModel: MyUploadsViewModel, activity: FragmentActivity?) :
    RecyclerView.Adapter<MyUploadsAdapter.MyUploadsViewHolder>() {

    private var posts = emptyList<PostEntity>()

    val navHostFragment: NavHostFragment = activity?.supportFragmentManager
        ?.findFragmentById(R.id.main_navhost_frag) as NavHostFragment
    val navController = navHostFragment.navController

    inner class MyUploadsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val deleteButton: MaterialButton = itemView.findViewById(com.twoshop.R.id.delete_btn)
        private val editButton: MaterialButton = itemView.findViewById(com.twoshop.R.id.edit_btn)

        private val image: ImageView = itemView.findViewById(com.twoshop.R.id.my_post_image)
        private val itemTextView: TextView = itemView.findViewById(com.twoshop.R.id.my_post_item)
        private val aboutTextView: TextView = itemView.findViewById(com.twoshop.R.id.my_post_about)
        private val locationTextView: TextView =
            itemView.findViewById(com.twoshop.R.id.my_post_location)
        private val phoneTextView: TextView = itemView.findViewById(com.twoshop.R.id.my_post_phone)
        private val ownerTextView: TextView = itemView.findViewById(com.twoshop.R.id.my_post_owner)

        fun bind(post: PostEntity) {
            Picasso.get().load(post.img).placeholder(R.drawable.place_holder_image)
                .into(image)


            itemTextView.text = post.price
            aboutTextView.text = post.about
            locationTextView.text = post.location
            phoneTextView.text = post.phone
            ownerTextView.text = post.owner

            // Set click listener for delete button
            deleteButton.setOnClickListener {
                var postForDeletion = PostEntity(
                    post.id, post.img, post.price, post.about,
                    post.phone, post.location, post.owner, post.uid
                )
                // Call the ViewModel's deletePost function with the post object
                viewModel.deletePost(postForDeletion)
            }
            //set up listener for edit button
            editButton.setOnClickListener {
                val bundle = Bundle()
                bundle.putSerializable("post", post)
                navController.navigate(R.id.action_global_editPostFragment, bundle)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyUploadsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(com.twoshop.R.layout.my_post_card, parent, false)
        return MyUploadsViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyUploadsViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }
    override fun getItemCount(): Int = posts.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newPosts: List<PostEntity>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}