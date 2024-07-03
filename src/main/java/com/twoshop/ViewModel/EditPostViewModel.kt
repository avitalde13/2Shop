package com.twoshop.ViewModel

import androidx.lifecycle.ViewModel
import com.twoshop.Model.Entities.PostEntity
import com.twoshop.Model.JoinedModel.JoinedPostModel

class EditPostViewModel: ViewModel() {
    val postsModel = JoinedPostModel()

    fun editPost(post : PostEntity, callback: (Boolean) -> Unit){
        postsModel.editPost(post, callback)
    }
}