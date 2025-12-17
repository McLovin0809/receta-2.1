package com.example.receta_2.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://recetaback-main-bccz.onrender.com/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiUsuario: UsuarioApiService by lazy { retrofit.create(UsuarioApiService::class.java) }
    val apiRecetas: RecetaApiService by lazy { retrofit.create(RecetaApiService::class.java) }
    val apiCategorias: CategoriaApiService by lazy { retrofit.create(CategoriaApiService::class.java) }
    val apiSubcategorias: SubcategoriaApiService by lazy { retrofit.create(SubcategoriaApiService::class.java) }
    val apiImagenes: ImagenRecetaApiService by lazy { retrofit.create(ImagenRecetaApiService::class.java) }
}
