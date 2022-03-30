package com.hgecapsi.recipeapp.views.viewmodel

import androidx.lifecycle.*
import com.hgecapsi.recipeapp.views.models.data.RecipeData
import com.hgecapsi.recipeapp.views.models.database.DishRepository
import kotlinx.coroutines.launch

class FavDishViewModel(private val dishRepository: DishRepository):ViewModel() {

    /**
     * Launching a new coroutine to insert the data in a non-blocking way.
     */
    fun insert(recipeData: RecipeData) = viewModelScope.launch {
        // Call the repository function and pass the details.
        dishRepository.insertFavDishData(recipeData)
    }

    // TODO Step 3: Get all the dishes list from the database in the ViewModel to pass it to the UI.
    // START
    /** Using LiveData and caching what allDishes returns has several benefits:
     * We can put an observer on the data (instead of polling for changes) and only
     * update the UI when the data actually changes.
     * Repository is completely separated from the UI through the ViewModel.
     */
    val allDishesList: LiveData<List<RecipeData>> = dishRepository.allDishesList.asLiveData()
    // END

    // TODO Step 3: Get the list of favorite dishes that we can populate in the UI.
    // START
    // Get the list of favorite dishes that we can populate in the UI.
    /** Using LiveData and caching what favoriteDishes returns has several benefits:
     * We can put an observer on the data (instead of polling for changes) and only
     * update the UI when the data actually changes.
     * Repository is completely separated from the UI through the ViewModel.
     */
    val favoriteDishes: LiveData<List<RecipeData>> = dishRepository.favoriteDishes.asLiveData()
    // END

    // TODO Step 3: Create a function to Update and pass the required params.
// START
    /**
     * Launching a new coroutine to update the data in a non-blocking way
     */
    fun update(recipeData: RecipeData) = viewModelScope.launch {
        dishRepository.updateFavDishData(recipeData)
    }
// END
}

/**
 * To create the ViewModel we implement a ViewModelProvider.Factory that gets as a parameter the dependencies
 * needed to create FavDishViewModel: the FavDishRepository.
 * By using viewModels and ViewModelProvider.Factory then the framework will take care of the lifecycle of the ViewModel.
 * It will survive configuration changes and even if the Activity is recreated,
 * you'll always get the right instance of the FavDishViewModel class.
 */
class FavDishViewModelFactory(private val dishRepository: DishRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavDishViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavDishViewModel(dishRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}