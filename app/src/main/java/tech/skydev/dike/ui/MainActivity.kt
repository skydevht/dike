package tech.skydev.dike.ui

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import tech.skydev.dike.Injection
import tech.skydev.dike.R
import tech.skydev.dike.ui.article.ArticleFragment
import tech.skydev.dike.ui.article.ArticlePresenter
import tech.skydev.dike.ui.title.TitleFragment
import tech.skydev.dike.ui.title.TitlesPresenter
import tech.skydev.dike.ui.titledetails.TitleDetailsFragment
import tech.skydev.dike.ui.titledetails.TitleDetailsPresenter
import tech.skydev.dike.util.ActivityUtils
import tech.skydev.dike.widget.AboutDialog

class MainActivity : AppCompatActivity(), Navigation {


    private var mTitlesPresenter: TitlesPresenter? = null
    private lateinit var currentFragmentTag: String
    private val FRAG_TAG_KEY: String = "frag-tag"
    val aboutDialog = AboutDialog()

    lateinit var mAdView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        mAdView = findViewById(R.id.adView) as AdView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

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

        if (savedInstanceState == null) {
            currentFragmentTag = TitleFragment.TAG
            // Create the main fragment
            val titleFragment = TitleFragment.newInstance()
            ActivityUtils.addFragmentToActivity(
                    supportFragmentManager, titleFragment, R.id.contentFrame, false, TitleFragment.TAG)
            // Create the presenter
            mTitlesPresenter = TitlesPresenter(
                    Injection.provideConstitutionRepository(this@MainActivity), titleFragment)
        } else {
            currentFragmentTag = savedInstanceState.getString(FRAG_TAG_KEY, TitleFragment.TAG)
            // Get the main fragment if it is on the back stack
            val titleFragment: TitleFragment? = supportFragmentManager.findFragmentByTag(TitleFragment.TAG) as? TitleFragment
            titleFragment?.let {
                // Recreate the presenter because they are not save on closing
                TitlesPresenter(
                        Injection.provideConstitutionRepository(this@MainActivity), titleFragment)
            }
            val titledetailsFragment: TitleDetailsFragment? = supportFragmentManager.findFragmentByTag(TitleDetailsFragment.TAG) as? TitleDetailsFragment
            titledetailsFragment?.let {
                TitleDetailsPresenter(
                        Injection.provideConstitutionRepository(this@MainActivity), titledetailsFragment)
            }
            val articleFragment: ArticleFragment? = supportFragmentManager.findFragmentByTag(ArticleFragment.TAG) as? ArticleFragment
            articleFragment?.let {
                ArticlePresenter(
                        Injection.provideConstitutionRepository(this@MainActivity), articleFragment)
            }
            // To reshow the back button if it is not the main string (the previous are always on the back stack
            if (currentFragmentTag == TitleDetailsFragment.TAG || currentFragmentTag == ArticleFragment.TAG) bar.setDisplayHomeAsUpEnabled(true);
        }
    }


    override fun showTitleScreen(id: String) {
        val titleDetailsFragment = TitleDetailsFragment.newInstance(id)
        ActivityUtils.addFragmentToActivity(
                supportFragmentManager, titleDetailsFragment, R.id.contentFrame, true, TitleDetailsFragment.TAG)
        TitleDetailsPresenter(
                Injection.provideConstitutionRepository(this@MainActivity), titleDetailsFragment)
        currentFragmentTag = TitleDetailsFragment.TAG;

    }

    override fun showArticle(titleId: String, articleId: Int) {
        val articleFragment = ArticleFragment.newInstance(titleId, articleId)
        ActivityUtils.addFragmentToActivity(
                supportFragmentManager, articleFragment, R.id.contentFrame, true, ArticleFragment.TAG)
        ArticlePresenter(
                Injection.provideConstitutionRepository(this@MainActivity), articleFragment)
        currentFragmentTag = ArticleFragment.TAG
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
        } else if (id == R.id.action_about) {
            aboutDialog.show(supportFragmentManager, "about-dialog")
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString(FRAG_TAG_KEY, currentFragmentTag)
    }
}
