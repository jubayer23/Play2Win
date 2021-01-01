package com.mindorks.retrofit.coroutines.data.api


import com.example.play2win.Account
import com.example.play2win.Balance
import retrofit2.http.*
import java.security.PrivateKey

interface ApiService {

    @GET("getSeed")
    suspend fun getSeed(): Balance


    //@FormUrlEncoded
    //@GET("getBalance")
    //suspend fun getBalance(@Field("address") address: String): Balance


    @POST("hederaApi/bal2")
    suspend fun getBalance(@Query("account") account: String, @Query("privateKey") privateKey: String): Balance

    @POST("hederaApi/five")
    suspend fun makeTransactionFive(@Query("account") account: String, @Query("pk") privateKey: String): Balance

    //@POST("hederaApi/getAccount")
   // suspend fun getAccount(@Query("seed") address: String): Balance

    @POST("hederaExtensionApi/getAccount")
    suspend fun getAccount(@Query("email") email: String, @Query("mobile") mobile: String): Account

    @POST("hederaExtensionApi/createAccount")
    suspend fun createAccount(@Query("username") username: String, @Query("email") email: String, @Query("mobile") mobile: String): Account
    //@GET("character/{id}")
    //    suspend fun getCharacter(@Path("id") id: Int): Response<Character>

    //@GET("character/{id}"
    //fun getAnimals(@Field("key") key: String): Single<List<Animal>>

    //@GET("character/{id}")
    //    suspend fun getCharacter(@Path("id") id: Int): Response<Character>
}