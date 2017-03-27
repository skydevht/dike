package tech.skydev.dike.ui

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import tech.skydev.dike.Injection
import tech.skydev.dike.R
import tech.skydev.dike.ui.title.TitleFragment
import tech.skydev.dike.ui.title.TitlesPresenter
import tech.skydev.dike.ui.titledetails.TitleDetailsFragment
import tech.skydev.dike.ui.titledetails.TitleDetailsPresenter
import tech.skydev.dike.util.ActivityUtils

class MainActivity : AppCompatActivity(), Navigation {


    private var mTitlesPresenter: TitlesPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val bar: ActionBar = supportActionBar!!
        bar.title = "Constitution Haitienne"
        bar.subtitle = "1987 - Amendée"
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                bar.setDisplayHomeAsUpEnabled(true);
            } else {
                bar.setDisplayHomeAsUpEnabled(false)
            }
        }

        var titleFragment: TitleFragment? = supportFragmentManager.findFragmentById(R.id.contentFrame) as? TitleFragment
        if (titleFragment == null) {
            // Create the fragment
            titleFragment = TitleFragment.newInstance()
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, titleFragment, R.id.contentFrame, false)
        }

        // Create the presenter
        mTitlesPresenter = TitlesPresenter(
                Injection.provideConstitutionRepository(applicationContext), titleFragment)
    }

    override fun showTitleScreen(id: String) {
        val titleDetailsFragment: TitleDetailsFragment = TitleDetailsFragment.newInstance()
        ActivityUtils.addFragmentToActivity(
                supportFragmentManager, titleDetailsFragment, R.id.contentFrame, true)
        val titleDetailsPresenter = TitleDetailsPresenter(
                Injection.provideConstitutionRepository(applicationContext), titleDetailsFragment, id)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == android.R.id.home) {
            onBackPressed()
        }
        else if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
