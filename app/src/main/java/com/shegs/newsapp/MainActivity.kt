package com.shegs.newsapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shegs.newsapp.adapter.RecyclerAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://newsapi.org/"

class MainActivity : AppCompatActivity() {

    lateinit var countDownTimer: CountDownTimer

    private var titlesList = mutableListOf<String>()
    private var descList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()
    private var linksList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeAPIRequest()
    }

    private fun fadeInFromBlack(){
        val v_blackScreen = findViewById<View>(R.id.v_blackScreen)
        v_blackScreen.animate().apply {
            alpha(0f)
            duration = 3000
        }.start()
    }

    @SuppressLint("ResourceType")
    private fun setUpRecyclerView(){
        val rvRecyclerView = findViewById<RecyclerView>(R.id.rvRecyclerView)
        rvRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        rvRecyclerView.adapter = RecyclerAdapter(titlesList, descList, imagesList, linksList)
    }

    private fun addToList(title: String, description: String, image: String, link: String) {
        titlesList.add(title)
        descList.add(description)
        imagesList.add(image)
        linksList.add(link)
    }

    private fun makeAPIRequest(){

        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getNews()

                for (article in response.articles){
                    Log.i("MainActivity", "Result = $article")
                    addToList(article.title, article.description, article.urlToImage, article.url)
                }
                withContext(Dispatchers.Main){
                    setUpRecyclerView()
                    fadeInFromBlack()
                }

            }catch (e:Exception){
                Log.e("MainActivity", e.toString())
            }
        }
    }
}