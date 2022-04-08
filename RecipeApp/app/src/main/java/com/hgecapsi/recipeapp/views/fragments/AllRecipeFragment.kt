package com.hgecapsi.recipeapp.views.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hgecapsi.recipeapp.R
import com.hgecapsi.recipeapp.adapters.FavDishAdapter
import com.hgecapsi.recipeapp.application.FavDishApplication
import com.hgecapsi.recipeapp.data.RecipeData
import com.hgecapsi.recipeapp.databinding.FragmentAllRecipeBinding
import com.hgecapsi.recipeapp.viewmodel.FavDishViewModel
import com.hgecapsi.recipeapp.viewmodel.FavDishViewModelFactory
import com.hgecapsi.recipeapp.views.activities.AddDishActivity
import com.hgecapsi.recipeapp.views.activities.MainActivity


class AllRecipeFragment : Fragment() {
    private lateinit var allRecipeFragmentBinding: FragmentAllRecipeBinding

    private lateinit var mFavDishAdapter: FavDishAdapter

    private lateinit var recyclerView: RecyclerView

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
        allRecipeFragmentBinding = FragmentAllRecipeBinding.inflate(inflater,container,false)

        return allRecipeFragmentBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
// TODO Step 7: Initialize the RecyclerView and bind the adapter class
        // START
        // Set the LayoutManager that this RecyclerView will use.
        allRecipeFragmentBinding.rvDishesList.layoutManager = GridLayoutManager(requireActivity(), 2)
        // Adapter class is initialized and list is passed in the param.
        // TODO Step 2: Make this variable as global
        mFavDishAdapter = FavDishAdapter(this@AllRecipeFragment)
        // adapter instance is set to the recyclerview to inflate the items.
        allRecipeFragmentBinding.rvDishesList.adapter = mFavDishAdapter

        /**
         * Add an observer on the LiveData returned by getAllDishesList.
         * The onChanged() method fires when the observed data changes and the activity is in the foreground.
         */
        /**
         * Add an observer on the LiveData returned by getAllDishesList.
         * The onChanged() method fires when the observed data changes and the activity is in the foreground.
         */
        mFavDishViewModel.allDishesList.observe(viewLifecycleOwner) { dishes ->
            dishes.let {

                // TODO Step 9: Pass the dishes list to the adapter class.
                // START
                if (it.isNotEmpty()) {

                    allRecipeFragmentBinding.rvDishesList.visibility = View.VISIBLE
                    allRecipeFragmentBinding.tvNoDishesAddedYet.visibility = View.GONE

                    mFavDishAdapter.dishesList(it)
                } else {

                    allRecipeFragmentBinding.rvDishesList.visibility = View.GONE
                    allRecipeFragmentBinding.tvNoDishesAddedYet.visibility = View.VISIBLE
                }
                // END
            }
        }
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

// TODO Step 8: Create a function to navigate to the Dish Details Fragment.
    // START
    /**
     * A function to navigate to the Dish Details Fragment.
     */
    fun dishDetails(recipeData: RecipeData){

        // TODO Step 9: Call the hideBottomNavigationView function when user wants to navigate to the DishDetailsFragment.
        // START
        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.hideBottomNavigationView()
        }
        // END

        findNavController().navigate(
                AllRecipeFragmentDirections.actionAllRecipeFragmentToDishDetailsFragment(recipeData)
        )
//        findNavController().navigate(
//            R.id.action_allRecipeFragment_to_dishDetailsFragment,null
//        )

    }
    // END

    // TODO Step 4: Create a function to show an AlertDialog while delete the dish details.
    // START
    /**
     * Method is used to show the Alert Dialog while deleting the dish details.
     *
     * @param dish - Dish details that we want to delete.
     */
    fun deleteStudent(recipeData: RecipeData) {
        val builder = AlertDialog.Builder(requireActivity())
        //set title for alert dialog
        builder.setTitle(resources.getString(R.string.title_delete_dish))
        //set message for alert dialog
        builder.setMessage(resources.getString(R.string.msg_delete_dish_dialog, recipeData.title))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(resources.getString(R.string.lbl_yes)) { dialogInterface, _ ->
            mFavDishViewModel.delete(recipeData)
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.lbl_no)) { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }
    // END

    // TODO Step 10: Override the onResume method and call the function to show the BottomNavigationView when user is on the AllDishesFragment.
    // START
    override fun onResume() {
        super.onResume()

        if(requireActivity() is MainActivity){
            (activity as MainActivity?)!!.showBottomNavigationView()
        }
    }
    // END
}