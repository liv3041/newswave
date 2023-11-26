package com.example.newswave

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

// Adapter class for RecyclerView to bind data to NewsViewHolder
class NewsAdapter(private val context: Context, private val articles: MutableList<Article>) :
    RecyclerView.Adapter<NewsViewHolder>() {

    // Create and return a new NewsViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        // Inflate the layout for an individual news item and create a new ViewHolder
        val view = LayoutInflater.from(context).inflate(R.layout.row_news, parent, false)
        return NewsViewHolder(view)
    }
    // Bind data to the ViewHolder at the given position
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        // Get the article at the current position and bind it to the ViewHolder
        val article = articles[position]
        holder.bind(article)
    }
    // Return the total number of items in the data set
    override fun getItemCount(): Int {
        return articles.size
    }

    // Add a new item to the data set and notify the adapter of the change
    fun addItem(article: Article) {
        articles.add(article)
        notifyDataSetChanged()}

}


