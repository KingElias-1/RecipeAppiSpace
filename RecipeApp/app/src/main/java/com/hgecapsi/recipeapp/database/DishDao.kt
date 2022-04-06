package com.hgecapsi.recipeapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hgecapsi.recipeapp.data.RecipeData
import kotlinx.coroutines.flow.Flow

@Dao
interface DishDao {
    /**
     * All queries must be executed on a separate thread.
     * They cannot be executed from Main Thread or it will cause a crash.
     *
     * Room has Kotlin coroutines support.
     * This allows your queries to be annotated with the
     * suspend modifier and then called from a coroutine
     * or from another suspension function.
     */

    /**
     * A function to insert favorite dish details to the local database using Room.
     *
     * @param favDish - Here we will pass the entity class that we have created.
     */
    @Insert
    suspend fun insertFavDishDetails(recipeData: RecipeData)

    // TODO Step 1: Create a function to get the list of dishes from database using @Query.
    // START
    /**
     * When data changes, you usually want to take some action,
     * such as displaying the updated data in the UI.
     * This means you have to observe the data so when it changes, you can react.
     *
     * To observe data changes we will use Flow from kotlinx-coroutines.
     * Use a return value of type Flow in your method description,
     * and Room generates all necessary code to update the Flow when the database is updated.
     *
     * A Flow is an async sequence of values
     * Flow produces values one at a time (instead of all at once)
     * that can generate values from async operations
     * like network requests, database calls, or other async code.
     * It supports coroutines throughout its API, so you can transform a flow using coroutines as well!
     */

    // TODO Step 1: Create a suspend function to update the dish details.
    // START
    /**
     * A function to update favorite dish details to the local database using Room.
     *
     * @param favDish - Here we will pass the entity class that we have created with updated details along with "id".
     */
    @Update
    suspend fun updateFavDishDetails(recipeData: RecipeData)
    // END

    // TODO Step 1: Create a function to get the list of favorite dishes from the database.
    // START
    /**
     * SQLite does not have a boolean data type. Room maps it to an INTEGER column, mapping true to 1 and false to 0.
     */
    @Query("SELECT * FROM FAV_DISHES_TABLE WHERE favorite_dish = 1")
    fun getFavoriteDishesList(): Flow<List<RecipeData>>
    // END

    @Query("SELECT * FROM FAV_DISHES_TABLE ORDER BY ID")
    fun getAllDishesList(): Flow<List<RecipeData>>
    // END
}