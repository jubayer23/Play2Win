package com.example.play2win

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.play2win.utils.mutableLiveDataWithValue

class LocalAuthRepository constructor(context: Context)
    : LocalBaseRepository(context.getSharedPreferences("authInfo", Context.MODE_PRIVATE)), ILocalAuthRepository {

    private val mutableAuthState = mutableLiveDataWithValue(false)

    override fun hasEverLogin(): Boolean =
        getData("hasEverLogin", Boolean::class) ?: false

    override fun getCurrentProfile(): ProfileInfo? =
        getData("profileInfo", ProfileInfo::class)

    override fun setCurrentProfile(profileInfo: ProfileInfo) {
        putData("profileInfo", profileInfo)
        putData("hasEverLogin", true)
        mutableAuthState.postValue(true)
    }

    override fun clearCurrentProfile() {
        putData("profileInfo", null)
        mutableAuthState.postValue(false)
    }


    override fun getCurrentUser(): UserProfile? =
        getData("userProfile", UserProfile::class)

    override fun setCurrentUser(userProfile: UserProfile) {
        putData("userProfile", userProfile)
        putData("hasEverLogin", true)
        mutableAuthState.postValue(true)
    }

    override fun clearCurrentUser() {
        putData("userProfile", null)
        mutableAuthState.postValue(false)
    }

    override fun observeAuthState(): LiveData<Boolean> =
        mutableAuthState
}