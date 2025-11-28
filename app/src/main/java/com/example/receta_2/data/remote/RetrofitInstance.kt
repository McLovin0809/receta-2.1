package com.example.receta_2.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://recetaback-main.onrender.com/"

    // Cliente HTTP con logging
    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    // Instancia única de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    // Servicios disponibles
    val apiUsuario: UsuarioApiService by lazy {
        retrofit.create(UsuarioApiService::class.java)
    }

    val apiRecetas: RecetaApiService by lazy {
        retrofit.create(RecetaApiService::class.java)
    }

    val apiCategorias: CategoriaApiService by lazy {
        retrofit.create(CategoriaApiService::class.java)
    }

    val apiSubcategorias: SubcategoriaApiService by lazy {
        retrofit.create(SubcategoriaApiService::class.java)
    }

    val apiImagenes: ImagenRecetaApiService by lazy {
        retrofit.create(ImagenRecetaApiService::class.java)
    }
}
