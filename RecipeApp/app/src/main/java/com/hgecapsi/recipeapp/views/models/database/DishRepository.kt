package com.hgecapsi.recipeapp.views.models.database

import androidx.annotation.WorkerThread
import com.hgecapsi.recipeapp.views.models.data.RecipeData
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

    // TODO Step 2: Create a variable for the dishes list to access it from ViewModel.
    // START
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allDishesList: Flow<List<RecipeData>> = dishDao.getAllDishesList()
    // END
}