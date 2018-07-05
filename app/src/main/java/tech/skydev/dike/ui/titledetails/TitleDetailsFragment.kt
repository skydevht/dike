package tech.skydev.dike.ui.titledetails

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tech.skydev.dike.R
import tech.skydev.dike.base.BaseFragment
import tech.skydev.dike.data.model.Article
import tech.skydev.dike.data.model.Titre
import tech.skydev.dike.ui.MainActivity
import tech.skydev.dike.ui.Navigation
import tech.skydev.dike.ui.titledetails.adapter.TitleDetailsAdapter
import tech.skydev.dike.ui.titledetails.adapter.TitleSideAdapter
import tech.skydev.dike.util.ConversionUtil
import tech.skydev.dike.widget.GridSpacingItemDecoration

/**
 * A placeholder fragment containing a simple view.
 */
class TitleDetailsFragment : BaseFragment(), TitleDetailsContract.View {


    var mAdapter: TitleDetailsAdapter = TitleDetailsAdapter(null)
    var mSideAdapter: TitleSideAdapter = object : TitleSideAdapter(ArrayList<Titre>(0)) {
        override fun onCellClick(model: Titre, pos: Int) {
            if (getItemViewType(pos) == PREAMBULE_VIEW_TYPE) {
                val navigation: Navigation = activity as MainActivity
                navigation.showArticle(model.id!!, 1)
            }
            else {
                mPresenter?.loadTitle(model.id!!)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mTitleId = if (savedInstanceState == null) arguments!!.getString(TITLE_ID_KEY)!! else savedInstanceState.getString(TITLE_ID_KEY)

        mAdapter.mListener = object : TitleDetailsAdapter.ClickListener() {
            override fun onArticleClicked(article: Article) {
                val navigation: Navigation = activity as MainActivity
                navigation.showArticle(mTitleId, article.id)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_titledetails, container, false)
        initMainRecycler(rootView)
        val sideRecycler = rootView.findViewById(R.id.section_list) as RecyclerView
        sideRecycler.layoutManager = LinearLayoutManager(context)
        sideRecycler.adapter = mSideAdapter
        return rootView
    }

    private fun initMainRecycler(rootView: View?) {
        val recycler: RecyclerView = rootView?.findViewById(R.id.titledetails_list) as RecyclerView
        val colSpan = if (screenOrientation == Configuration.ORIENTATION_PORTRAIT) 3 else 4
        val layoutManager: GridLayoutManager = GridLayoutManager(context, colSpan)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {

                return when (mAdapter.getItemViewType(position)) {
                    mAdapter.TITLE_VIEW_TYPE, mAdapter.CHAPTER_VIEW_TYPE,
                    mAdapter.SECTION_VIEW_TYPE -> colSpan
                    else -> 1
                }
            }

        }
        layoutManager.orientation = GridLayoutManager.VERTICAL
        recycler.layoutManager = layoutManager
        recycler.adapter = mAdapter

        recycler.addItemDecoration(GridSpacingItemDecoration(ConversionUtil.dpToPx(context!!, 8), true))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter!!.loadTitle(mTitleId)
        mPresenter!!.loadTitles()
    }


    var mPresenter: TitleDetailsContract.Presenter? = null


    override fun setPresenter(presenter: TitleDetailsContract.Presenter) {
        this.mPresenter = presenter;
    }

    override fun showTitle(titre: Titre) {
        mAdapter.replaceItems(titre)
        val bar = (activity as AppCompatActivity).supportActionBar
        bar?.title = "Titre " + titre.id
        bar?.subtitle = titre.name
        mSideAdapter.selectedId = titre.id
        mTitleId = titre.id!!;
        mSideAdapter.notifyDataSetChanged()
    }

    lateinit var mTitleId: String

    override fun showTitles(titres: ArrayList<Titre>) {
        mSideAdapter.replaceItems(titres)
        mSideAdapter.selectedId = mTitleId
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putString(TITLE_ID_KEY, mTitleId)
    }

    companion object {
        val TAG: String = "titledetails-fragment"
        val TITLE_ID_KEY: String = "titleId"

        fun newInstance(id: String): TitleDetailsFragment {
            val args: Bundle = Bundle()
            args.putString("titleId", id)
            val titleDetailsFragment = TitleDetailsFragment()
            titleDetailsFragment.arguments = args
            return titleDetailsFragment
        }
    }
}
