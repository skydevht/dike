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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.TextView
import tech.skydev.dike.R
import tech.skydev.dike.data.model.Article
import timber.log.Timber

/**
 * Created by Hash Skyd on 4/4/2017.
 */
class ArticleView : FrameLayout, GestureDetector.OnGestureListener {

    private val SWIPE_THRESHOLD = 100
    private val SWIPE_VELOCITY_THRESHOLD = 100

    var swipeListener: OnSwipeListener? = null

    val gestureScanner = GestureDetectorCompat(context, this)
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

    override fun onShowPress(e: MotionEvent?) {
        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false//TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return true
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        var result = false
        try {
            val diffY: Float = e2!!.getY() - e1!!.getY();
            val diffX: Float = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY) && swipeListener != null) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                    result = true
                }
            } else {
                // onTouch(e);
            }
        } catch (exception: Exception) {
            exception.printStackTrace();
        }
        return result;
    }

    fun onSwipeLeft() {
        val animate = AnimationUtils.loadAnimation(context, R.anim.slide_out_left)
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(animation: Animation?) {
                swipeListener?.onSwipeLeft()
                startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_right))
            }

            override fun onAnimationStart(animation: Animation?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        startAnimation(animate)
    }

    fun onSwipeRight() {
        val animate = AnimationUtils.loadAnimation(context, R.anim.slide_out_right)
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(animation: Animation?) {
                swipeListener?.onSwipeRight()
                startAnimation(AnimationUtils.loadAnimation(context, R.anim.slide_in_left))
            }

            override fun onAnimationStart(animation: Animation?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        startAnimation(animate)
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        return false //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onLongPress(e: MotionEvent?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    interface OnSwipeListener {
        fun onSwipeLeft()
        fun onSwipeRight()
    }
}