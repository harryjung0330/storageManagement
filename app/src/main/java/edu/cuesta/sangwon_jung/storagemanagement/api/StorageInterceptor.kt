package edu.cuesta.sangwon_jung.storagemanagement.api

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class StorageInterceptor(val token:String): Interceptor
{
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()

        val newUrl: HttpUrl = originalRequest.url().newBuilder()
            .build()

        val newRequest: Request = originalRequest.newBuilder()
            .url(newUrl)
            .addHeader("Authorization", token)
            .build()

        return chain.proceed(newRequest)
    }
}