package com.example.play2win.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.NavigationUI
import com.example.play2win.R

import com.example.play2win.databinding.ActivityMainBinding

import androidx.navigation.findNavController
import com.mindorks.retrofit.coroutines.data.api.ApiHelper
import com.mindorks.retrofit.coroutines.data.api.RetrofitBuilder
import com.mindorks.retrofit.coroutines.ui.base.ViewModelFactory
import com.mindorks.retrofit.coroutines.ui.main.viewmodel.LoadWalletViewModel

class MainActivity : AppCompatActivity() {

    private val navController by lazy { findNavController(R.id.myNavHostFragment) }
    private var viewModel: LoadWalletViewModel? = null

    var isOnGame : Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        NavigationUI.setupActionBarWithNavController(this, navController!!);

        setupViewModel()

        viewModel?.loadUser()
    }

    override fun onResume() {
        super.onResume()
        viewModel?.refreshBalance()
    }

    private fun setupViewModel() {
        try {
            val viewModelProvider = ViewModelProvider(
                navController.getViewModelStoreOwner(R.id.navigation),
                ViewModelFactory(ApiHelper(RetrofitBuilder.apiService), this)
            )
            viewModel = viewModelProvider.get(LoadWalletViewModel::class.java)

        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

    }

    override fun onBackPressed() {
        if(!isOnGame){
            if(!navController!!.navigateUp()){
                super.onBackPressed()
            }
            else if(navController.currentDestination?.id == R.id.welcomeFragment){
                Log.d("DEBUG", "its welcome")
                viewModel?.refreshBalance()
            }
        }else{
            Toast.makeText(this, "You cannot exit during the game!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        //Log.d("DEBUG", "its here")
        return navController!!.navigateUp()
    }


}