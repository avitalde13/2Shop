package com.twoshop.Model.ModelRoom

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.twoshop.TwoShopApplication
import com.twoshop.Model.ModelRoom.Dao.PostDao
import com.twoshop.Model.ModelRoom.Dao.UserDao
import com.twoshop.Model.Entities.PostEntity
import com.twoshop.Model.Entities.UserEntity

@Database(entities = [UserEntity::class, PostEntity::class], version = 4, exportSchema = false)
abstract class AppLocalDB : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao

    companion object {
        // Define a singleton instance of the database
        @Volatile private var instance: AppLocalDB? = null;
        private const val DB_NAME = "GET_PET"

        fun getInstance(): AppLocalDB {
            return instance?: synchronized(this) {
                instance?: Room.databaseBuilder(
                    TwoShopApplication.getInstance().applicationContext,
                    AppLocalDB::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
        }
    }
}
