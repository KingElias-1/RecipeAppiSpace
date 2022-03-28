package com.hgecapsi.recipeapp.views.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.hgecapsi.recipeapp.R
import com.hgecapsi.recipeapp.views.activities.AddDishActivity
import com.hgecapsi.recipeapp.views.application.FavDishApplication
import com.hgecapsi.recipeapp.views.viewmodel.FavDishViewModel
import com.hgecapsi.recipeapp.views.viewmodel.FavDishViewModelFactory


class AllRecipeFragment : Fragment() {

    /**
     * To create the ViewModel we used the viewModels delegate, passing in an instance of our FavDishViewModelFactory.
     * This is constructed based on the repository retrieved from the FavDishApplication.
     */
    private val mFavDishViewModel: FavDishViewModel by viewModels {
        FavDishViewModelFactory((requireActivity().application as FavDishApplication.FavDishApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)//to add option menu at top of fragment (three dot menu bar drop list or single options)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner, Observer { data ->
            if (data.isNotEmpty()){
                Log.i("MYDATA", data.toString())
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.all_recipe_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    //used for option menu items actions
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addDish -> {
                startActivity(Intent(requireActivity(), AddDishActivity::class.java))//using require activity instead of this for context cos of fragment
            }
        }
        return super.onOptionsItemSelected(item)
    }
}