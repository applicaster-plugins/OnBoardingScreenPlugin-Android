package com.applicaster.onboarding.screen.data.api

import com.applicaster.onboarding.screen.model.OnBoardingItem
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface OBApiService {

    @GET("{url}")
    fun fetchOBF(@Path("url") url: String):
            Observable<OnBoardingItem>

    companion object {
        fun create(): OBApiService {

            val retrofit = Retrofit.Builder()
                    .client(OkHttpClient())
                    .addCallAdapterFactory(
                            RxJava2CallAdapterFactory.create())
                    .addConverterFactory(
                            GsonConverterFactory.create())
                    .build()

            return retrofit.create(OBApiService::class.java)
        }
    }
}