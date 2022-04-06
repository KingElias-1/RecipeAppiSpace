package com.hgecapsi.recipeapp.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hgecapsi.recipeapp.application.FavDishApplication
import com.hgecapsi.recipeapp.databinding.FragmentFavoriteBinding
import com.hgecapsi.recipeapp.viewmodel.FavDishViewModel
import com.hgecapsi.recipeapp.viewmodel.FavDishViewModelFactory
import com.hgecapsi.recipeapp.views.activities.MainActivity


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


    // TODO Step 5: Override the onResume function to show the BottomNavigationView when the fragment is completely loaded.
    // START
    override fun onResume() {
        super.onResume()

        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }
    // END


    override fun onDestroy() {
        super.onDestroy()
        favoriteFragmentBinding = null
    }
}