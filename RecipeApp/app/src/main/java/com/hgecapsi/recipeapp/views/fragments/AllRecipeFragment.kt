package com.hgecapsi.recipeapp.views.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.hgecapsi.recipeapp.R
import com.hgecapsi.recipeapp.views.activities.AddDishActivity


class AllRecipeFragment : Fragment() {

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