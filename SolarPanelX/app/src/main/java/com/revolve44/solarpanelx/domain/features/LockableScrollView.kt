package com.revolve44.solarpanelx.domain.features

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

/**
https://stackoverflow.com/questions/65543781/how-to-disable-work-of-scrollview-on-inner-seekbar/65544704#65544704
 */

class LockableScrollView : ScrollView {
    // true if we can scroll (not locked)
    // false if we cannot scroll (locked)
    var isScrollable = true
        private set

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    fun setScrollingEnabled(enabled: Boolean) {
        isScrollable = enabled
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return if (ev.action == MotionEvent.ACTION_DOWN) { // if we can scroll pass the event to the superclass
            isScrollable && super.onTouchEvent(ev)
        } else super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // Don't do anything with intercepted touch events if
        // we are not scrollable
        return isScrollable && super.onInterceptTouchEvent(ev)
    }
}