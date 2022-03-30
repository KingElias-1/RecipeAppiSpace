package com.hgecapsi.recipeapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.hgecapsi.recipeapp.databinding.FragmentFavoriteBinding
import com.hgecapsi.recipeapp.views.adapters.FavDishAdapter
import com.hgecapsi.recipeapp.views.application.FavDishApplication
import com.hgecapsi.recipeapp.views.viewmodel.FavDishViewModel
import com.hgecapsi.recipeapp.views.viewmodel.FavDishViewModelFactory


class FavoriteFragment : Fragment() {

    //create an instance of viewBinding
    private var favoriteFragmentBinding: FragmentFavoriteBinding? = null

    // TODO Step  5: Create an instance of ViewModel to access the methods that are necessary to populate the UI.
    // START
    /**
     * To create the ViewModel we used the viewModels delegate, passing in an instance of our FavDishViewModelFactory.
     * This is constructed based on the repository retrieved from the FavDishApplication.
     */
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication.FavDishApplication).repository)
    }
    // END

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //val rootview = inflater.inflate(R.layout.fragment_favorite, container, false)
        favoriteFragmentBinding = FragmentFavoriteBinding.inflate(inflater,container,false)


        return favoriteFragmentBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * Add an observer on the LiveData returned by getFavoriteDishesList.
         * The onChanged() method fires when the observed data changes and the activity is in the foreground.
         */
        mFavDishViewModel.favoriteDishes.observe(viewLifecycleOwner) { dishes ->
            dishes.let {

                // TODO Step 5: Remove the Logs and display the list of Favorite Dishes using RecyclerView. Here we will not create a separate adapter class we cas use the same that we have created for AllDishes.
                // START

                // Set the LayoutManager that this RecyclerView will use.
                favoriteFragmentBinding!!.rvFavoriteDishesList.layoutManager =
                    GridLayoutManager(requireActivity(), 2)
                // Adapter class is initialized and list is passed in the param.
                val adapter = FavDishAdapter(this@FavoriteFragment)
                // adapter instance is set to the recyclerview to inflate the items.
                favoriteFragmentBinding!!.rvFavoriteDishesList.adapter = adapter

                if (it.isNotEmpty()) {
                    favoriteFragmentBinding!!.rvFavoriteDishesList.visibility = View.VISIBLE
                    favoriteFragmentBinding!!.tvNoFavoriteDishesAvailable.visibility = View.GONE

                    adapter.dishesList(it)
                } else {
                    favoriteFragmentBinding!!.rvFavoriteDishesList.visibility = View.GONE
                    favoriteFragmentBinding!!.tvNoFavoriteDishesAvailable.visibility = View.VISIBLE
                }
                // END

            }
        }
    }
}