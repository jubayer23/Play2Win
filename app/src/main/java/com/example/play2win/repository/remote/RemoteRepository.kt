package com.example.metamask.repository

import com.mindorks.retrofit.coroutines.data.api.ApiHelper
import java.net.Inet4Address
import java.security.PrivateKey

class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getUsers() = apiHelper.getSeed()
    suspend fun getBalance(account: String, privateKey: String) = apiHelper.getBalance(account, privateKey)
    suspend fun makeTransactionFive(account: String, privateKey: String) = apiHelper.makeTransactionFive(account, privateKey)
    suspend fun getAccount(email: String, mobile : String) = apiHelper.getAccount(email, mobile)
    suspend fun createAccount(username :String, email: String, mobile : String) = apiHelper.createAccount(username, email, mobile)
}