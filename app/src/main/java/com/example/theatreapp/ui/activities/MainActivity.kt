package com.example.theatreapp.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.theatreapp.R
import com.example.theatreapp.databinding.ActivityMainBinding
import com.example.theatreapp.ui.composables.LoginForm

class MainActivity :
    AppCompatActivity()
{
    private lateinit var binding : ActivityMainBinding
    private val TAG : String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        setContent {
//            LoginForm()
//        }
    }

    override fun onSupportNavigateUp(): Boolean = findNavController(R.id.navHostfragment).navigateUp()
}