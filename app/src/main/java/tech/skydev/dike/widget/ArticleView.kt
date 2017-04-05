package tech.skydev.dike.widget

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.view.GestureDetectorCompat
import android.text.Html
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.TextView
import tech.skydev.dike.R
import tech.skydev.dike.data.model.Article
import timber.log.Timber

/**
 * Created by Hash Skyd on 4/4/2017.
 */
class ArticleView : FrameLayout {

    val gestureScanner = GestureDetectorCompat(context, MyGestureListener())
    var article: Article? = null
        set(value) {
            mArticleNameTV.text = "Article ${value?.order}"
            mArticleTextTV.text = Html.fromHtml(value?.text)
        }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.article_view, this, true);
        mArticleNameTV = findViewById(R.id.article_name) as TextView
        mArticleTextTV = findViewById(R.id.article_text) as TextView
    }



    lateinit var mArticleNameTV: TextView

    lateinit var mArticleTextTV: TextView

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        Timber.d("Touched")
        if (gestureScanner.onTouchEvent(event)) return true
        return super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        Timber.d("Touched (intercepted)")
        if (gestureScanner.onTouchEvent(event)) return true
        return super.onInterceptTouchEvent(event)
    }

    class MyGestureListener: GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            Timber.d("Down");
            return true
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            Timber.d("Fling");
            return true
        }
    }

}