package com.hgecapsi.recipeapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hgecapsi.recipeapp.data.RecipeData

@Database(entities = [RecipeData::class], version = 1)
abstract class DatabaseInstance: RoomDatabase() {

    //abstract fun recipeData(): RecipeData
    abstract fun dishDao(): DishDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: DatabaseInstance? = null

        fun getDatabaseInstance(context: Context): DatabaseInstance {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseInstance::class.java,
                    "fav_dish_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}