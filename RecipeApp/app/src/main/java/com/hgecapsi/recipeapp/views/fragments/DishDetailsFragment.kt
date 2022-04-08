package com.hgecapsi.recipeapp.views.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.hgecapsi.recipeapp.R
import com.hgecapsi.recipeapp.application.FavDishApplication
import com.hgecapsi.recipeapp.data.RecipeData
import com.hgecapsi.recipeapp.databinding.FragmentDishDetailsBinding
import com.hgecapsi.recipeapp.viewmodel.FavDishViewModel
import com.hgecapsi.recipeapp.viewmodel.FavDishViewModelFactory
import java.io.IOException
import java.util.*

class DishDetailsFragment : Fragment() {
    // TODO Step 7: Create a ViewBinding variable.
    // START
    private var detailsBinding: FragmentDishDetailsBinding? = null
    // END

    // TODO Step 5: Create an ViewModel instance to access the methods.
    // START
    /**
     * To create the ViewModel we used the viewModels delegate, passing in an instance of our FavDishViewModelFactory.
     * This is constructed based on the repository retrieved from the FavDishApplication.
     */
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication.FavDishApplication).repository)
    }
    // END
    // TODO Step 6: Create a global variable  for Dish Details and assign the args to it.
    // START
    private var mFavDishDetails: RecipeData? = null
    // END

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        detailsBinding = FragmentDishDetailsBinding.inflate(inflater,container,false)
        return detailsBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DishDetailsFragmentArgs by navArgs()

        // TODO Step 7: Initialize the FavDish global variable.
        // START
        mFavDishDetails = args.dishDetails
        // END

        args.let {

            try {
                // Load the dish image in the ImageView.
                Glide.with(requireActivity())
                    .load(it.dishDetails.image)
                    .centerCrop()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            // log exception
                            Log.e("TAG", "Error loading image", e)
                            return false // important to return false so the error placeholder can be placed

                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            // TODO Step 3: Generate the Palette and set the vibrantSwatch as the background of the view.
                            // START
                            resource?.let { it1 ->
                                Palette.from(it1.toBitmap())
                                    .generate { palette ->
                                        val intColor = palette?.vibrantSwatch?.rgb ?: 0
                                        detailsBinding!!.rlDishDetailMain.setBackgroundColor(intColor)
                                    }
                            }
                            return false
                            // END

                        }

                    })
                    .into(detailsBinding!!.ivDishImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            detailsBinding!!.tvTitle.text = it.dishDetails.title
            detailsBinding!!.tvType.text =
                it.dishDetails.type.capitalize(Locale.ROOT) // Used to make first letter capital
            detailsBinding!!.tvCategory.text = it.dishDetails.category
            detailsBinding!!.tvIngredients.text = it.dishDetails.ingredients
            detailsBinding!!.tvCookingDirection.text = it.dishDetails.directionToCook
            detailsBinding!!.tvCookingTime.text =
                resources.getString(R.string.lbl_estimate_cooking_time, it.dishDetails.cookingTime)

            // TODO Step 10: Set the favorite icon based on the value.
            // START
            if (args.dishDetails.favoriteDish) {
                detailsBinding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )
            } else {
                detailsBinding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_unselected
                    )
                )
            }
            // END

        }
        // END

        // TODO Step 4: Assign the event to the favorite button.
        // START
        detailsBinding!!.ivFavoriteDish.setOnClickListener {

            // TODO Step 6: Update the favorite dish variable based on the current selection. i.e If it true then make it false vice-versa.
            // START
            args.dishDetails.favoriteDish = !args.dishDetails.favoriteDish
            // END

            // TODO Step 7: Pass the updated values to ViewModel
            // START
            mFavDishViewModel.update(args.dishDetails)
            // END

            // TODO Step 8: Update the icons and display the toast message accordingly.
            // START
            if (args.dishDetails.favoriteDish) {
                detailsBinding!!.ivFavoriteDish.setImageDrawable(
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
            } else {
                detailsBinding!!.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_unselected
                    )
                )

                Toast.makeText(
                    requireActivity(),
                    resources.getString(R.string.msg_removed_from_favorite),
                    Toast.LENGTH_SHORT
                ).show()
            }
            // END
        }
        // END
    }
}