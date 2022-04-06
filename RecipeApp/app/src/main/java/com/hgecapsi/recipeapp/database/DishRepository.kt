package com.hgecapsi.recipeapp.database

import androidx.annotation.WorkerThread
import com.hgecapsi.recipeapp.data.RecipeData
import kotlinx.coroutines.flow.Flow

class DishRepository(private val dishDao: DishDao) {
    /**
     * By default Room runs suspend queries off the main thread, therefore, we don't need to
     * implement anything else to ensure we're not doing long running database work
     * off the main thread.
     */
    @WorkerThread
    suspend fun insertFavDishData(recipeData: RecipeData) {
        dishDao.insertFavDishDetails(recipeData)
    }

    // TODO Step 2: Create a suspend function on workerThread to Update the details that can be called from the ViewModel class.
    // START
    @WorkerThread
    suspend fun updateFavDishData(recipeData: RecipeData) {
        dishDao.updateFavDishDetails(recipeData)
    }
    // END

    // TODO Step 2: Get the list of favorite dishes from the DAO and pass it to the ViewModel.
    // START
    val favoriteDishes: Flow<List<RecipeData>> = dishDao.getFavoriteDishesList()
    // END

    // TODO Step 2: Create a variable for the dishes list to access it from ViewModel.
    // START
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allDishesList: Flow<List<RecipeData>> = dishDao.getAllDishesList()
    // END


}