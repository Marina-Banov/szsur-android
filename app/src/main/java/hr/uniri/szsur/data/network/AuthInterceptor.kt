package hr.uniri.szsur.data.network

import hr.uniri.szsur.data.repository.UserRepository
import okhttp3.Interceptor
import okhttp3.Response


class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("Authorization", "Bearer ${UserRepository.token}")
        return chain.proceed(requestBuilder.build())
    }
}
