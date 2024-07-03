package com.twoshop.Model.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "posts")
data class PostEntity (
    @PrimaryKey()
    var id: String,
    var img: String,
    var price: String,
    var about: String,
    var phone: String,
    var location: String,
    var owner: String,
    var uid: String
) : Serializable {

    fun fromMap(map: Map<String?, Any?>) {
        img = map["image"].toString()
        price = map["price"].toString()
        about = map["about"].toString()
        phone = map["phone"].toString()
        location = map["location"].toString()
        owner = map["owner"].toString()
        uid = map["uid"].toString()

    }
}

