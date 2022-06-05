package utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import utils.Utils.BASE_URL

class RetrofitInstance {

    companion object {
        @Volatile
        private var INSTANCE: Retrofit? = null

        @Synchronized
        fun getINSTANCE(): Retrofit? {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                return INSTANCE
            }
            return INSTANCE

        }
    }

}