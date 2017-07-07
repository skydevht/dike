package tech.skydev.dike.ui

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListAdapter
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import tech.skydev.dike.R
import tech.skydev.dike.db.SearchTable

class SearchResultsActivity : AppCompatActivity() {

    var db = SearchTable(this)
    var listAdapter: ListAdapter? = null
    lateinit var listView: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)
        listView = findViewById(R.id.list) as ListView
        handleIntent(getIntent());
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent);
    }

    fun handleIntent(intent: Intent) {

        if (Intent.ACTION_SEARCH == intent.getAction()) {
            val query = intent.getStringExtra(SearchManager.QUERY);
            val c = db.getWordMatches(query, null)
            val adapter = SimpleCursorAdapter(this,
                    R.layout.row_section_content,
                    c,
                    arrayOf(db.COL_ART_ID),
                    IntArray(1, {R.id.content_title}))
            listAdapter = adapter
            listView.adapter = adapter
        }
    }

}
