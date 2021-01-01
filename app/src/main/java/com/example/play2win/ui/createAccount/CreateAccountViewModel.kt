package com.example.play2win.ui.createAccount

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.metamask.repository.MainRepository
import com.example.play2win.Account
import com.example.play2win.LocalAuthRepository
import com.example.play2win.ProfileInfo
import com.example.play2win.UserProfile
import com.mindorks.retrofit.coroutines.utils.Resource
import kotlinx.coroutines.Dispatchers

class CreateAccountViewModel(private val mainRepository: MainRepository, private val local : LocalAuthRepository) : ViewModel() {

    var username = ObservableField("")
    var email = ObservableField("")
    var mobile = ObservableField("")

    val errorUsername = ObservableField<String>()
    val errorEmail = ObservableField<String>()
    val errorMobile = ObservableField<String>()



    fun createAcount() = liveData(Dispatchers.IO) {
        if(!isFormValid()) return@liveData
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.createAccount(username.get().toString(), email.get().toString(),mobile.get().toString())))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun saveProfile(account: Account) {

        val userProfile = UserProfile(account.user_id, username.get().toString(), email.get().toString(), mobile.get().toString(), account.accountId,account.publicKey, account.privateKey, "0.0")

      //  val profileInfo =  ProfileInfo(accoundId.get().toString().trim().replace("\\s".toRegex(), ""), privateKey.get().toString().trim().replace("\\s".toRegex(), ""), balance)

        local.setCurrentUser(userProfile)
        //mutableProfileInfo.value = profileInfo
       // loadProfile()
    }


    fun isFormValid(): Boolean {

        if(username.get().toString().isEmpty()){
            errorUsername.set("Cannot be empty!")
            return false
        }else {
            errorUsername.set(null);
        }
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