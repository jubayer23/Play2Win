package com.mindorks.retrofit.coroutines.ui.main.viewmodel


import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.metamask.repository.MainRepository
import com.example.play2win.LocalAuthRepository
import com.example.play2win.ProfileInfo
import com.example.play2win.UserProfile
import com.mindorks.retrofit.coroutines.utils.Resource
import kotlinx.coroutines.Dispatchers


class LoadWalletViewModel(private val mainRepository: MainRepository, private val local : LocalAuthRepository) : ViewModel() {

    private val mutableRefreshBalance: MutableLiveData<Unit> = MutableLiveData()
    val refreshBalance: LiveData<Unit> = mutableRefreshBalance

    private val mutableErrorListener: MutableLiveData<String> = MutableLiveData()
    val errorListenerLiveData: LiveData<String> = mutableErrorListener

    private val mutableUserProfile: MutableLiveData<UserProfile> = MutableLiveData()
    val userProfileLiveData: LiveData<UserProfile> = mutableUserProfile

    var accoundId = ObservableField("")
    var privateKey = ObservableField("")
    var balance = ObservableField("")

    val errorAccountId = ObservableField<String>()
    val errorPrivateKey = ObservableField<String>()


    fun getBalance() = liveData(Dispatchers.IO) {
        if(!isLoggedIn()) return@liveData
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.getBalance(accoundId.get().toString(),privateKey.get().toString())))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun makeTransactionFive() = liveData(Dispatchers.IO) {
        if(!isLoggedIn()){
            mutableErrorListener.postValue("Please load your account first.")
            return@liveData
        }
        if(!isBalanceAvailable()){
            mutableErrorListener.postValue("Not enough balance! Please buy HBar to play this quiz")
            return@liveData
        }
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mainRepository.makeTransactionFive(accoundId.get().toString(),privateKey.get().toString())))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }


    fun loadUser() {
        if(isLoggedIn()){
            val user = getCurrentUser()
            accoundId.set(user?.account)
            privateKey.set(user?.privateKey)
            mutableUserProfile.value = user
        }
    }

    fun saveBalance(b: String) {
        val user =  getCurrentUser()
        user?.balance = b
        balance.set(b)
        user?.let { local.setCurrentUser(it) }
        loadUser()
    }

    private fun getCurrentUser() : UserProfile? {
        return local.getCurrentUser()
    }

    fun isLoggedIn() : Boolean {
        return local.hasEverLogin()
    }

    fun refreshBalance() {
        mutableRefreshBalance.value = Unit
    }

    private fun isBalanceAvailable() : Boolean{

        return  balance.get().toString().toDouble() > 10
    }
}