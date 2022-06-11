package utils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import utils.Utils.BASE_URL
import java.time.Duration

class RetrofitInstance {

    companion object {
        @Volatile
        private var INSTANCE: Retrofit? = null

        @Synchronized
        fun getINSTANCE(): Retrofit? {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder().baseUrl(BASE_URL)
                    .client(
                        OkHttpClient.Builder()
                            .callTimeout(Duration.ofMinutes(3L))
                            .connectTimeout(Duration.ofMinutes(3L))
                            .readTimeout(Duration.ofMinutes(3L)).build()
                    )
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                return INSTANCE
            }
            return INSTANCE

        }
    }

}