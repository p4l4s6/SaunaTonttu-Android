package tech.cognix.sauntatonttu.network

import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.cognix.sauntatonttu.utils.Constants
import tech.cognix.sauntatonttu.utils.SharedPreferencesApp
import java.util.concurrent.TimeUnit

object ApiClient {

    fun FragmentActivity.apiClient(): Retrofit {
        val gson = GsonBuilder()
            .setLenient()
            .create()

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder().addInterceptor {
            val requestBuilder = it.request().newBuilder()
            val token = SharedPreferencesApp.getInstance(this).getToken()
            if (token != null)
                requestBuilder.addHeader("Authorization", token)
            requestBuilder.addHeader("deviceType", "Android ${Build.VERSION.RELEASE}")
            requestBuilder.addHeader("OSVersion", System.getProperty("os.version") ?: "")
            val request = requestBuilder.build()
            return@addInterceptor it.proceed(request)
        }

        client.callTimeout(120, TimeUnit.SECONDS)
        client.readTimeout(120, TimeUnit.SECONDS)
        client.addInterceptor(interceptor)

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    }

}