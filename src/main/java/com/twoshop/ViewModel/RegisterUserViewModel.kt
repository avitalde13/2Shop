package com.twoshop.ViewModel

import androidx.lifecycle.ViewModel
import com.twoshop.Model.Entities.UserEntity
import com.twoshop.Model.JoinedModel.JoinedUserModel

class RegisterUserViewModel: ViewModel() {
    val UserModel = JoinedUserModel()

    fun register(user : UserEntity, password: String,  callback: (Boolean) -> Unit){
        UserModel.register(user, password, callback)

    }
}