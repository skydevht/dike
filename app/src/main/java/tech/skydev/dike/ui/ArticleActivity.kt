package tech.skydev.dike.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.mukesh.MarkdownView
import tech.skydev.dike.R

class ArticleActivity : AppCompatActivity() {

    lateinit var articleView : MarkdownView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article)
        articleView = findViewById(R.id.article_view) as MarkdownView
        articleView.settings.builtInZoomControls = true
        articleView.settings.displayZoomControls = false
    }

    override fun onStart() {
        super.onStart()
        articleView.loadMarkdownFromAssets("constitution-1987-amende/text/preambule.md")
    }
}
