package com.mindorks.retrofit.coroutines.data.api

class ApiHelper(private val apiService: ApiService) {

    suspend fun getSeed() = apiService.getSeed()
    suspend fun getBalance( account: String, privateKey : String) = apiService.getBalance(account, privateKey)
    suspend fun getAccount( email: String, mobile: String) = apiService.getAccount(email,mobile)
    suspend fun createAccount(username:String, email: String, mobile: String) = apiService.createAccount(username, email,mobile)
    suspend fun makeTransactionFive( account: String, privateKey : String) = apiService.makeTransactionFive(account, privateKey)
}