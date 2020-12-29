package com.mindorks.retrofit.coroutines.data.api

class ApiHelper(private val apiService: ApiService) {

    suspend fun getSeed() = apiService.getSeed()
    suspend fun getBalance( account: String, privateKey : String) = apiService.getBalance(account, privateKey)
    suspend fun getAccount( seed: String) = apiService.getAccount(seed)
    suspend fun makeTransactionFive( account: String, privateKey : String) = apiService.makeTransactionFive(account, privateKey)
}