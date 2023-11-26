package com.example.newswave

import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

// Main activity class
class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar:ProgressBar
    lateinit var newsAdapter: NewsAdapter
    private val TAG = "MainActivity"
    private val articles: MutableList<Article> = mutableListOf()

    // onCreate method, called when the activity is first created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view from the XML layout file
        setContentView(R.layout.activity_main)
        // Initialize Firebase Cloud Messaging (FCM) token and show a toast
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            Log.d(TAG, "message")
            Toast.makeText(baseContext, "NewsWave", Toast.LENGTH_SHORT).show()
        })
        // Initialize UI components and fetch data
        init()
        setRecyclerView()
        FetchDataTask().execute("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json")

    }
    // Initialize UI components
    private fun init(){
        recyclerView = findViewById(R.id.rv_list)
        progressBar = findViewById(R.id.progress_bar)
        newsAdapter = NewsAdapter(this, articles)

    }
    // Set up the RecyclerView
    private fun setRecyclerView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        if (newsAdapter != null) {
            recyclerView.adapter = newsAdapter
        }
    }

    // AsyncTask to fetch data from the specified URL in the background
    private inner class FetchDataTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String): String? {
            val urlString = params[0]
            return try {
                // Fetch data from the URL
                val jsonData = fetchData(urlString)
                Log.d(TAG, "Fetched data: $jsonData")
                jsonData
            } catch (e: IOException) {
                // Handle errors related to fetching data
                Log.e(TAG, "Error fetching data", e)
                null
            }
        }
        // onPostExecute method, called after the background task is completed
        override fun onPostExecute(result: String?) {
            if (result != null) {
                parseJson(result)
            }
        }
    }
    // Function to fetch data from a specified URL
    @Throws(IOException::class)
    private fun fetchData(urlString: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection

        try {
            val inputStream = connection.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }

            return stringBuilder.toString()
        } finally {
            connection.disconnect()
        }
    }

    // Function to parse JSON data and update the UI
private fun parseJson(jsonData: String) {
    try {
        val jsonObject = JSONObject(jsonData)
        if (jsonObject.getString("status") == "ok") {
            val jsonArray = jsonObject.getJSONArray("articles")

            // Loop through JSON array and create Article objects
            for (i in 0 until jsonArray.length()) {
                val itemObj = jsonArray.getJSONObject(i)

                val sourceObj = itemObj.optJSONObject("source")
                val source = if (sourceObj != null) {
                    Source(sourceObj.optString("name", ""), sourceObj.optString("id", ""))
                } else {
                    Source("", "")
                }

                val author = itemObj.optString("author", "")
                val title = itemObj.optString("title", "")
                val description = itemObj.optString("description", "")
                val url = itemObj.optString("url", "")
                val urlToImage = itemObj.optString("urlToImage", "") // Corrected key
                val publishedAt = itemObj.optString("publishedAt", "")
                val content = itemObj.optString("content", "")

                // Create Article object and add it to the adapter
                val article = Article(source, author, title, description, url, urlToImage, publishedAt, content)
                newsAdapter.addItem(article)
            }

            // Notify the adapter that the data set has changed
            newsAdapter.notifyDataSetChanged()
        }
    } catch (e: JSONException) {
        // Handle errors related to JSON parsing
        Log.e(TAG, "Error parsing JSON", e)
    }
}


}