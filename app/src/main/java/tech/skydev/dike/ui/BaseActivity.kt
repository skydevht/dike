package tech.skydev.dike.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import tech.skydev.dike.R

/**
 * Created by hash on 6/28/17.
 */
open class BaseActivity: AppCompatActivity() {

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home ?:false) {
            onBackPressed()
            return true
        }else if (id == R.id.action_about) {
            val alert = AlertDialog.Builder(this)
                    .setTitle(R.string.action_about)
                    .setView(layoutInflater.inflate(R.layout.dialog_about, null))
                    .setPositiveButton("Fermer", null)
                    .create()
            alert.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val item = menu.findItem(R.id.app_bar_search)
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = item.getActionView() as SearchView
        searchView.setSearchableInfo(manager.getSearchableInfo(this.componentName))
        return super.onCreateOptionsMenu(menu)
    }

}