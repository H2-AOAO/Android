package kr.sesac.aoao.android.common

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConnection {
    companion object{
        private const val BASE_URL = "http://43.201.1.193:8080/"
        private var INSTANCE: Retrofit? = null

        fun getInstance(): Retrofit{
            if (INSTANCE == null){
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return INSTANCE!!
        }
    }
}
