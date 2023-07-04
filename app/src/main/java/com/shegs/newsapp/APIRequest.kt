package com.shegs.newsapp

import com.shegs.newsapp.api.NewsApiJSON
import retrofit2.http.GET

interface APIRequest {

    @GET("/v2/top-headlines?country=us&apiKey=dd1bd83fccf4451b897680e1a99d3893")

    suspend fun getNews(): NewsApiJSON
}