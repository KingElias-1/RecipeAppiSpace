package com.hgecapsi.recipeapp.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.hgecapsi.recipeapp.R
import com.hgecapsi.recipeapp.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)

        splashScreen.setKeepOnScreenCondition { true }

        setContentView(binding.root)

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}