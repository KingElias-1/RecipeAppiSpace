package com.hgecapsi.recipeapp.views.fragments

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.hgecapsi.recipeapp.API.RandomDish
import com.hgecapsi.recipeapp.R
import com.hgecapsi.recipeapp.application.FavDishApplication
import com.hgecapsi.recipeapp.data.RecipeData
import com.hgecapsi.recipeapp.databinding.FragmentDiscoverBinding
import com.hgecapsi.recipeapp.utils.Constants
import com.hgecapsi.recipeapp.viewmodel.FavDishViewModel
import com.hgecapsi.recipeapp.viewmodel.FavDishViewModelFactory
import com.hgecapsi.recipeapp.viewmodel.RandomDishViewModel


class DiscoverFragment : Fragment() {
    // TODO Step 3: Create a global variable for Progress Dialog
    // START
    // A global variable for Progress Dialog
    private var discoverProgressDialog: Dialog? = null
    // END

    // START
    private var disccoverBinding: FragmentDiscoverBinding? = null

    // TODO Step 1: Create an instance of the ViewModel Class
    // START
    private lateinit var mRandomDishViewModel: RandomDishViewModel
    //END

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        disccoverBinding = FragmentDiscoverBinding.inflate(inflater, container,false)
        return disccoverBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize the ViewModel variable.
        mRandomDishViewModel =
            ViewModelProvider(this).get(RandomDishViewModel::class.java)

        // TODO Step 3: Call the function to get the response from API.
        // START
        mRandomDishViewModel.getRandomRecipeFromAPI()
        // END

        // TODO Step 5: Call the observer function.
        // START
        randomDishViewModelObserver()
        // END

        // TODO Step 2: Set the setOnRefreshListener of SwipeRefreshLayout as below and call the getRandomDishFromAPI function to get the new dish details on the same screen.
        // START
        /**
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        disccoverBinding!!.srlRandomDish.setOnRefreshListener {
            // This method performs the actual data-refresh operation.
            // The method calls setRefreshing(false) when it's finished.
            mRandomDishViewModel.getRandomRecipeFromAPI()
        }



    }

    // TODO Step 4: Create a function to get the data in the observer after the API is triggered.
    // START
    /**
     * A function to get the data in the observer after the API is triggered.
     */
    private fun randomDishViewModelObserver() {

        mRandomDishViewModel.randomDishResponse.observe(
            viewLifecycleOwner,
            Observer { randomDishResponse ->
                randomDishResponse?.let {
                    Log.i("RandomDish Response", "$randomDishResponse.recipes[0]")
                    // TODO Step 2: Call the function to populate the response in the UI.
                    // START
                    // TODO Step 9: Hide the Loading ProgressBar of SwipeRefreshLayout when there is an error from API.
                    // START
                    if (disccoverBinding!!.srlRandomDish.isRefreshing) {
                        disccoverBinding!!.srlRandomDish.isRefreshing = false
                    }
                    // END

                    setRandomDishResponseInUI(randomDishResponse.recipes[0])
                    // END

                }
            })

        mRandomDishViewModel.randomDishLoadingError.observe(
            viewLifecycleOwner,
            Observer { dataError ->
                dataError?.let {
                    Log.i("RandomDish API Error", "$dataError")
                    // TODO Step 9: Hide the Loading ProgressBar of SwipeRefreshLayout when there is an error from API.
                    // START
                    if (disccoverBinding!!.srlRandomDish.isRefreshing) {
                        disccoverBinding!!.srlRandomDish.isRefreshing = false
                    }
                    // END

                }
            })

        mRandomDishViewModel.loadRandomDish.observe(viewLifecycleOwner, Observer { loadRandomDish ->
            loadRandomDish?.let {
                Log.i("Random Dish Loading", "$loadRandomDish")
                // TODO Step 6: Show the progress dialog if the SwipeRefreshLayout is not visible and hide when the usage is completed.
                // START
                // Show the progress dialog if the SwipeRefreshLayout is not visible and hide when the usage is completed.
                if (loadRandomDish && !disccoverBinding!!.srlRandomDish.isRefreshing) {
                    showCustomProgressDialog() // Used to show the progress dialog
                } else {
                    hideProgressDialog()
                }
                // END

            }
        })
    }
    // END





    /**
     * A method to populate the API response in the UI.
     *
     * @param recipe - Data model class of the API response with filled data.
     */
    private fun setRandomDishResponseInUI(recipe: RandomDish.Recipe) {

        // Load the dish image in the ImageView.
        Glide.with(requireActivity())
            .load(recipe.image)
            .centerCrop()
            .into(disccoverBinding!!.ivDishImage)

        disccoverBinding!!.tvTitle.text = recipe.title

        // Default Dish Type
        var dishType: String = "other"

        if (recipe.dishTypes.isNotEmpty()) {
            dishType = recipe.dishTypes[0]
            disccoverBinding!!.tvType.text = dishType
        }

        // There is not category params present in the response so we will define it as Other.
        disccoverBinding!!.tvCategory.text = "Other"

        var ingredients = ""
        for (value in recipe.extendedIngredients) {

            if (ingredients.isEmpty()) {
                ingredients = value.original
            } else {
                ingredients = ingredients + ", \n" + value.original
            }
        }

        disccoverBinding!!.tvIngredients.text = ingredients

        // The instruction or you can say the Cooking direction text is in the HTML format so we will you the fromHtml to populate it in the TextView.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            disccoverBinding!!.tvCookingDirection.text = Html.fromHtml(
                recipe.instructions,
                Html.FROM_HTML_MODE_COMPACT
            )
        } else {
            @Suppress("DEPRECATION")
            disccoverBinding!!.tvCookingDirection.text = Html.fromHtml(recipe.instructions)
        }

        disccoverBinding!!.tvCookingTime.text =
            resources.getString(
                R.string.lbl_estimate_cooking_time,
                recipe.readyInMinutes.toString()
            )

        disccoverBinding!!.ivFavoriteDish.setImageDrawable(
            ContextCompat.getDrawable(
                requireActivity(),
                R.drawable.ic_favorite_unselected
            )
        )

        var addedToFavorite = false

        disccoverBinding!!.ivFavoriteDish.setOnClickListener {

            if (addedToFavorite) {
                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_already_added_to_favorites),
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                val randomDishDetails = RecipeData(
                    recipe.image,
                    Constants.DISH_IMAGE_SOURCE_ONLINE,
                    recipe.title,
                    dishType,
                    "Other",
                    ingredients,
                    recipe.readyInMinutes.toString(),
                    recipe.instructions,
                    true
                )

                val mFavDishViewModel: FavDishViewModel by viewModels {
                    FavDishViewModelFactory((requireActivity().application as FavDishApplication.FavDishApplication).repository)
                }

                mFavDishViewModel.insert(randomDishDetails)

                addedToFavorite = true

                disccoverBinding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )

                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_added_to_favorites),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }



    // TODO Step 5: Override the onDestroy method and make the ViewBinding null when it is called.
    // START
    override fun onDestroy() {
        super.onDestroy()
        disccoverBinding = null
    }
    // END

// TODO Step 4: Create a function to show the Custom Progress Dialog.
    // START
    /**
     * A function is used to show the Custom Progress Dialog.
     */
    private fun showCustomProgressDialog() {
        discoverProgressDialog = Dialog(requireActivity())

        discoverProgressDialog?.let {
            /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
            it.setContentView(R.layout.dialog_custom_progress)

            //Start the dialog and display it on screen.
            it.show()
        }
    }
    // END

    // TODO Step 5: Create a function to hide the custom progress dialog.
    // START
    /**
     * This function is used to dismiss the progress dialog if it is visible to user.
     */
    private fun hideProgressDialog() {
        discoverProgressDialog?.let {
            it.dismiss()
        }
    }
    // END
}