package com.hgecapsi.recipeapp.views.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.hgecapsi.recipeapp.R
import com.hgecapsi.recipeapp.databinding.ActivityMainBinding
import com.hgecapsi.recipeapp.utils.Constants

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    // TODO Step 4: Make the navController variable as global variable.
    // START
    private lateinit var mNavController: NavController
    // END

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mNavController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.allRecipeFragment, R.id.favoriteFragment, R.id.discoverFragment
        ))
        setupActionBarWithNavController(mNavController, appBarConfiguration)
        mainBinding.bottomNav.setupWithNavController(mNavController)
        // TODO Step 19: Handle the Notification when user clicks on it.
        // START
        if (intent.hasExtra(Constants.NOTIFICATION_ID)) {
            val notificationId = intent.getIntExtra(Constants.NOTIFICATION_ID, 0)
            Log.i("Notification Id", "$notificationId")

            // The Random Dish Fragment is selected when user is redirect in the app via Notification.
            mainBinding.bottomNav.selectedItemId = R.id.discoverFragment
        }
        // END

    }

    // TODO Step 5: Override the onSupportNavigateUp method.
    // START
    override fun onSupportNavigateUp(): Boolean {

        // TODO Step 6: Add the navigate up code and pass the required params. This will navigate the user from DishDetailsFragment to AllDishesFragment when user clicks on the home back button.
        // START
        return NavigationUI.navigateUp(mNavController, null)
        // END
    }
    // END

    // TODO Step 5: Add the Visibility parameter to NavigationBottomView in both the hide and show functions.
    // START
    /**
     * A function to hide the BottomNavigationView with animation.
     */
    fun hideBottomNavigationView() {
        mainBinding.bottomNav.clearAnimation()
        mainBinding.bottomNav.animate().translationY(mainBinding.bottomNav.height.toFloat()).duration = 300
        mainBinding.bottomNav.visibility = View.GONE
    }

    /**
     * A function to show the BottomNavigationView with Animation.
     */
    fun showBottomNavigationView() {
        mainBinding.bottomNav.clearAnimation()
        mainBinding.bottomNav.animate().translationY(0f).duration = 300
        mainBinding.bottomNav.visibility = View.VISIBLE
    }
    // END



}