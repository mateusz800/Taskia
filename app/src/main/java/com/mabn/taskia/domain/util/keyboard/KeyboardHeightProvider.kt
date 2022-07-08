package com.mabn.taskia.domain.util.keyboard

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class KeyboardHeightProvider(
    private val context: Context,
    windowManager: WindowManager,
    private val decorView: View?,
    listener: KeyboardHeightListener?
) :
    PopupWindow(context), OnApplyWindowInsetsListener {
    private val metrics: DisplayMetrics = DisplayMetrics()
    private val popupView: LinearLayout
    private val globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener
    private var insets: Rect = Rect()
    fun start() {
        popupView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
        decorView?.post { showAtLocation(decorView, Gravity.NO_GRAVITY, 0, 0) }
    }

    override fun dismiss() {
        popupView.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
        super.dismiss()
    }

    override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
        val cutoutCompat = insets.displayCutout
        if (cutoutCompat != null) {
            this.insets = Rect(
                cutoutCompat.safeInsetLeft,
                cutoutCompat.safeInsetTop,
                cutoutCompat.safeInsetRight,
                cutoutCompat.safeInsetBottom
            )
        } else {
            this.insets = Rect(
                insets.systemWindowInsetLeft,
                insets.systemWindowInsetTop,
                insets.systemWindowInsetRight,
                insets.systemWindowInsetBottom
            )
        }
        if (decorView != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val rootWindowInsets: WindowInsets = decorView.getRootWindowInsets()
            val displayCutout = rootWindowInsets.displayCutout
            if (displayCutout != null) {
                this.insets = Rect(
                    displayCutout.safeInsetLeft,
                    displayCutout.safeInsetTop,
                    displayCutout.safeInsetRight,
                    displayCutout.safeInsetBottom
                )
            }
        }
        return insets
    }

    val keyboardHeight: Int
        get() {
            val rect = Rect()
            popupView.getWindowVisibleDisplayFrame(rect)
            var keyboardHeight: Int =
                metrics.heightPixels - (rect.bottom - rect.top) - (insets.bottom - insets.top)
            val resourceID: Int =
                context.getResources().getIdentifier("status_bar_height", "dimen", "android")
            if (resourceID > 0) {
                keyboardHeight -= context.getResources().getDimensionPixelSize(resourceID)
            }
            if (keyboardHeight < 100) {
                keyboardHeight = 0
            }
            return keyboardHeight
        }

    interface KeyboardHeightListener {
        fun onKeyboardHeightChanged(height: Int, isLandscape: Boolean)
    }

    init {
        windowManager.defaultDisplay.getMetrics(metrics)
        popupView = LinearLayout(context)
        popupView.layoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            windowManager.defaultDisplay.getMetrics(metrics)
            val keyboardHeight = keyboardHeight
            val screenLandscape = metrics.widthPixels > metrics.heightPixels
            listener?.onKeyboardHeightChanged(keyboardHeight, screenLandscape)
        }
        contentView = popupView
        softInputMode =
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        inputMethodMode = INPUT_METHOD_NEEDED
        width = 0
        height = ViewGroup.LayoutParams.MATCH_PARENT
        setBackgroundDrawable(ColorDrawable(0))
        ViewCompat.setOnApplyWindowInsetsListener(popupView, this)
    }
}