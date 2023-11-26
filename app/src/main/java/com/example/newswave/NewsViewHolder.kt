package com.example.newswave

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


// ViewHolder class for NewsAdapter to represent individual news items in RecyclerView
class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // Views within the ViewHolder
    private val txtTitle: TextView = itemView.findViewById(R.id.txt_title)
    private val txtSourceName: TextView = itemView.findViewById(R.id.txt_source_name)
    private val txtDate: TextView = itemView.findViewById(R.id.txt_date)
    private val imgThumbnail: ImageView = itemView.findViewById(R.id.img_thumbnail)
    // Adapter and data variables
    var adapter:NewsAdapter? = null
    private val articles: MutableList<Article> = mutableListOf()
    private var articleUrl: String? = null

    // Initialization block for setting up click listener
init{
    itemView.setOnClickListener {
        articleUrl?.let { url ->
            // Open the article URL in a web browser when item is clicked
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            itemView.context.startActivity(intent)
        }
    }
}
    // Bind method to populate the ViewHolder with data from the Article
    fun bind(article: Article) {
        setTxtSourceName(article.getSource()?.getName())
        setTxtTitle(article.getTitle())
        setTxtDate(article.getPublishedAt())
        setImgThumbnail(itemView.context, article.getUrlToImage())
        articleUrl = article.getUrl()

    }


    // Setter methods for individual view components
    private fun setTxtTitle(txtTitle: String?) {
        this.txtTitle.text = txtTitle
    }

    private fun setTxtSourceName(txtSourceName: String?) {
        this.txtSourceName.text = txtSourceName
    }

    private fun setTxtDate(txtDate: String?) {
        this.txtDate.text = txtDate
    }

    private fun setImgThumbnail(context: Context?, url: String?) {
        Glide.with(context!!)
            .load(url)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .apply(RequestOptions().override(100, 100))
            .into(imgThumbnail)
    }
}
