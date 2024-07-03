package com.twoshop.Model.ModelRoom.Model

import com.twoshop.Model.Entities.PostEntity
import com.twoshop.Model.ModelRoom.AppLocalDB


class PostModel {
    fun getAllPosts(): List<PostEntity> {
        return AppLocalDB.getInstance().postDao().getAllPosts()
    }

    fun insertPost(post: PostEntity) {
         return AppLocalDB.getInstance().postDao().insertPost(post)
    }
     fun deletePost(post: PostEntity){
         return AppLocalDB.getInstance().postDao().deletePost(post)
     }
    fun getPostsByUid(uid: String) : List<PostEntity> {
        return AppLocalDB.getInstance().postDao().getPostsByUserId(uid)
    }
    fun updatePost(post: PostEntity){
        return AppLocalDB.getInstance().postDao().updatePost(post)
    }

}