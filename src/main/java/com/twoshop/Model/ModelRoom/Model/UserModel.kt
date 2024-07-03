package com.twoshop.Model.ModelRoom.Model

import com.twoshop.Model.Entities.UserEntity
import com.twoshop.Model.ModelRoom.AppLocalDB

class UserModel {

    fun insert(user: UserEntity){
        val db = AppLocalDB.getInstance().userDao().insert(user)
    }
    fun getUserById(uid: String): UserEntity {
        return AppLocalDB.getInstance().userDao().getUserById(uid)
    }

    fun updateUser(user: UserEntity){
        return AppLocalDB.getInstance().userDao().updateUser(user)
    }


}