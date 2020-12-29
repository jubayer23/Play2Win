package com.mindorks.retrofit.coroutines.ui.main.viewmodel


import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.metamask.repository.MainRepository
import com.example.play2win.LocalAuthRepository
import com.example.play2win.ProfileInfo
import com.mindorks.retrofit.coroutines.utils.Resource
import kotlinx.coroutines.Dispatchers


class LoadWalletViewModel(private val mainRepository: MainRepository, private val local : LocalAuthRepository) : ViewModel() {

    private val mutableRefreshBalance: MutableLiveData<Unit> = MutableLiveData()
    val refreshBalance: LiveData<Unit> = mutableRefreshBalance

    private val mutableProfileInfo: MutableLiveData<ProfileInfo> = MutableLiveData()
    val profileInfoLiveData: LiveData<ProfileInfo> = mutableProfileInfo

    var accoundId = ObservableField("")
    var privateKey = ObservableField("")
    var balance = ObservableField("")

    val errorAccountId = ObservableField<String>()
    val errorPrivateKey = ObservableField<String>()


    fun getBalance() = liveData(Dispatchers.IO) {
        if(accoundId.get()?.isEmpty()!!  && privateKey.get()?.isEmpty()!!) return@liveData
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getBalance(accoundId.get().toString(),privateKey.get().toString())))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun makeTransactionFive(account: String, privateKey: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.makeTransactionFive(account,privateKey)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun isFormValid(): Boolean {


        if(accoundId.get().toString().isEmpty()){
            errorAccountId.set("Cannot be empty!")
            return false
        }else {
            errorAccountId.set(null);
        }
        if(privateKey.get().toString().isEmpty()){
            errorPrivateKey.set("Cannot be empty!")
            return false
        }else{
            errorPrivateKey.set(null)
        }
        return true
    }


    fun loadProfile() {

        if(isLoggedIn()){
            val profile = getProfile()
            accoundId.set(profile?.account)
            privateKey.set(profile?.pk)
            balance.set(profile?.balance)
            mutableProfileInfo.value = profile
        }

    }

    fun clearFormError(){
        errorPrivateKey.set(null)
        errorAccountId.set(null)
    }



    fun saveProfile(balance: String) {

        val profileInfo =  ProfileInfo(accoundId.get().toString().trim().replace("\\s".toRegex(), ""), privateKey.get().toString().trim().replace("\\s".toRegex(), ""), balance)

        local.setCurrentProfile(profileInfo)
        mutableProfileInfo.value = profileInfo
        loadProfile()
    }

    fun getProfile() : ProfileInfo? {
        return local.getCurrentProfile()
    }

    fun isLoggedIn() : Boolean {
        return local.hasEverLogin()
    }

    fun refreshBalance() {
        mutableRefreshBalance.value = Unit
    }
}