package com.example.play2win.ui.loadAccount

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.metamask.repository.MainRepository
import com.example.play2win.Account
import com.example.play2win.LocalAuthRepository
import com.example.play2win.UserProfile
import com.mindorks.retrofit.coroutines.utils.Resource
import kotlinx.coroutines.Dispatchers

class LoadAccountViewModel(private val mainRepository: MainRepository, private val local : LocalAuthRepository) : ViewModel() {

    var email = ObservableField("")
    var mobile = ObservableField("")

    val errorEmail = ObservableField<String>()
    val errorMobile = ObservableField<String>()



    fun getAccount() = liveData(Dispatchers.IO) {
        if(!isFormValid()) return@liveData
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getAccount(email.get().toString(),mobile.get().toString())))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun saveProfile(account: Account) {

        val userProfile = UserProfile(account.user_id, account.userName , email.get().toString(), mobile.get().toString(), account.accountId,account.publicKey, account.privateKey, "0.0")

        //  val profileInfo =  ProfileInfo(accoundId.get().toString().trim().replace("\\s".toRegex(), ""), privateKey.get().toString().trim().replace("\\s".toRegex(), ""), balance)

        local.setCurrentUser(userProfile)
        //mutableProfileInfo.value = profileInfo
        // loadProfile()
    }

    fun loadUser() {

        if(isLoggedIn()){
            val user = getCurrentUser()
            email.set(user?.email)
            mobile.set(user?.mobile)
        }

    }

    fun getCurrentUser() : UserProfile? {
        return local.getCurrentUser()
    }

    fun isLoggedIn() : Boolean {
        return local.hasEverLogin()
    }


    fun isFormValid(): Boolean {


        if(email.get().toString().isEmpty()){
            errorEmail.set("Cannot be empty!")
            return false
        }else {
            errorEmail.set(null);
        }
        if(mobile.get().toString().isEmpty()){
            errorMobile.set("Cannot be empty!")
            return false
        }else{
            errorMobile.set(null)
        }
        return true
    }

    fun clearFormError(){
        errorEmail.set(null)
        errorMobile.set(null)
    }
}