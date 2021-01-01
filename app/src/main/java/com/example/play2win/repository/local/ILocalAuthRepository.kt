package com.example.play2win

import androidx.lifecycle.LiveData

interface ILocalAuthRepository {

    fun hasEverLogin(): Boolean

    fun getCurrentProfile(): ProfileInfo?

    fun setCurrentProfile(profileInfo: ProfileInfo)

    fun clearCurrentProfile()

    fun getCurrentUser(): UserProfile?

    fun setCurrentUser(userProfile: UserProfile)

    fun clearCurrentUser()

    fun observeAuthState(): LiveData<Boolean>
}