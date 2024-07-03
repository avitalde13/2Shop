package com.twoshop.ViewModel

import androidx.lifecycle.ViewModel
import com.twoshop.Model.Entities.UserEntity
import com.twoshop.Model.JoinedModel.JoinedUserModel

class ProfileViewModel: ViewModel() {
    val usersModel = JoinedUserModel()

    fun getUserByUid(uid :String,  callback: (UserEntity)-> Unit){
        return usersModel.getUserByUid(uid, callback)
    }
}