package com.twoshop.ViewModel

import androidx.lifecycle.ViewModel
import com.twoshop.Model.Entities.UserEntity
import com.twoshop.Model.JoinedModel.JoinedUserModel

class EditProfileViewModel: ViewModel() {
    val userModel = JoinedUserModel()
    fun editProfile(user : UserEntity, password : String, callback: (Boolean) -> Unit){
        userModel.editProfile(user,password,callback)

    }
}