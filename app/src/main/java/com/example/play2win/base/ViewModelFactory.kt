package com.mindorks.retrofit.coroutines.ui.base

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.metamask.repository.MainRepository
import com.example.play2win.LocalAuthRepository
import com.mindorks.retrofit.coroutines.data.api.ApiHelper
import com.mindorks.retrofit.coroutines.ui.main.viewmodel.LoadWalletViewModel

class ViewModelFactory(private val apiHelper: ApiHelper, private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoadWalletViewModel::class.java)) {
            return LoadWalletViewModel(MainRepository(apiHelper),LocalAuthRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}

