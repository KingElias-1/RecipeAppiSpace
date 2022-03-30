package com.hgecapsi.recipeapp.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hgecapsi.recipeapp.R
import com.hgecapsi.recipeapp.databinding.ActivityMainBinding
import com.hgecapsi.recipeapp.views.fragments.AllRecipeFragment
import com.hgecapsi.recipeapp.views.fragments.DiscoverFragment
import com.hgecapsi.recipeapp.views.fragments.FavoriteFragment

class MainActivity : AppCompatActivity() {
    private lateinit var mainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        mainBinding.bottomNav.setOnItemSelectedListener {
            it.isChecked = true

            when(it.itemId){
                R.id.allRecipeFragment -> replaceFragment(AllRecipeFragment(), it.title.toString())
                R.id.favoriteFragment -> replaceFragment(FavoriteFragment(), it.title.toString())
                R.id.discoverFragment -> replaceFragment(DiscoverFragment(), it.title.toString())
            }
            true
        }
    }


    private fun replaceFragment(fragment: Fragment, title:String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        //using a fragment manager and transaction to swith fragments
        fragmentTransaction.replace(R.id.nav_host_fragment,fragment)
        //the fragment is placed in the frame layout in the activity_main Layout
        fragmentTransaction.commit()
        setTitle(title)
    }
}