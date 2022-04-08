package com.hgecapsi.recipeapp.adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hgecapsi.recipeapp.R
import com.hgecapsi.recipeapp.data.RecipeData
import com.hgecapsi.recipeapp.databinding.ItemDishLayoutBinding
import com.hgecapsi.recipeapp.utils.Constants
import com.hgecapsi.recipeapp.views.activities.AddDishActivity
import com.hgecapsi.recipeapp.views.fragments.AllRecipeFragment
import com.hgecapsi.recipeapp.views.fragments.FavoriteFragment

class FavDishAdapter(private val fragment: Fragment): RecyclerView.Adapter<FavDishAdapter.ViewHolder>() {
    private var dishes: List<RecipeData> = listOf()

    //adapter method
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemDishLayoutBinding =
            ItemDishLayoutBinding.inflate(LayoutInflater.from(fragment.context), parent, false)
        return ViewHolder(binding)
    }

    //adapter method
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dish = dishes[position]

        // Load the dish image in the ImageView.
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)

        holder.tvTitle.text = dish.title

        // TODO Step 9: Assign the click event to the itemview and perform the required action.
        // START
        holder.itemView.setOnClickListener {
            if (fragment is AllRecipeFragment) {
                fragment.dishDetails(dish)
            }else if (fragment is FavoriteFragment) {
                fragment.dishDetails(dish)
            }

        }
        // END
        // TODO Step 7: We want the menu icon should be visible only in the AllDishesFragment not in the FavoriteDishesFragment so add the below to achieve it.
        // START
        if (fragment is AllRecipeFragment) {
            holder.ibMore.visibility = View.VISIBLE
        } else if (fragment is FavoriteFragment) {
            holder.ibMore.visibility = View.GONE
        }
        // END

        holder.ibMore.setOnClickListener{
            //gonna make a pop-up menu
            val popup = PopupMenu(fragment.context, holder.ibMore)
            //Inflating the Popup using xml file
            popup.menuInflater.inflate(R.menu.menu_adapter, popup.menu)

            // TODO Step 8: Assign the click event to the menu items as below and print the Log or You can display the Toast message for now.
            // START
            popup.setOnMenuItemClickListener {
                if (it.itemId == R.id.action_edit_dish) {
                    //  Log.i("You have clicked on", "Edit Option of ${dish.title}")
                    // TODO Step 2: Replace the Log with below code to pass the dish details to AddUpdateDishActivity.
                    // START
                    val intent =
                        Intent(fragment.requireActivity(), AddDishActivity::class.java)
                    intent.putExtra(Constants.EXTRA_DISH_DETAILS, dish)
                    fragment.requireActivity().startActivity(intent)
                    // END

                } else if (it.itemId == R.id.action_delete_dish) {
                    Log.i("You have clicked on", "Delete Option of ${dish.title}")
                    // TODO Step 6: Remove the log and call the function that we have created to delete.
                    // START
                    if (fragment is AllRecipeFragment) {
                        fragment.deleteStudent(dish)
                    }
                    // END

                }
                true
            }
            // END

            popup.show() //showing popup menu
        }
    }

    //adapter method
    override fun getItemCount(): Int {
        return dishes.size
    }

    // TODO Step 8: Create a function that will have the updated list of dishes that we will bind it to the adapter class.
    // START
    fun dishesList(list: List<RecipeData>) {
        dishes = list
        notifyDataSetChanged()
    }
    // END

    //this was created cos of view holder in implementation <FavDishAdapter.ViewHolder>(
    class ViewHolder(view: ItemDishLayoutBinding):RecyclerView.ViewHolder(view.root){//using view for view binding in the item dish laayout
// Holds the TextView that will add each item to
    val ivDishImage = view.ivDishImage
        val tvTitle = view.tvDishTitle
        // TODO Step 5: Create a variable for more menu icon.
        // START
        val ibMore = view.ibMore
        // END
    }


}