package com.twoshop.ViewModel

import androidx.lifecycle.ViewModel
import com.twoshop.Model.Entities.PostEntity
import com.twoshop.Model.JoinedModel.JoinedPostModel

class UploadPostViewModel : ViewModel() {
    val postsModel = JoinedPostModel()
    fun uploadPost(post : PostEntity, callback: (Boolean) -> Unit){
        postsModel.uploadPost(post, callback)
    }
}