package com.magnificsoftware.letswatch.util.form

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.widget.Button
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import timber.log.Timber

class AppButtonProgressIndicator(
    private val buttonWidget: Button,
    private val buttonText: String,
    private val onLoadingButtonText: String,
) {
    companion object {
        private fun createProgressDrawable(
            context: Context,
            buttonWidget: Button,
        ): CircularProgressDrawable {
            // create progress drawable
            return CircularProgressDrawable(context).apply {
                // let's use large style just to better see one issue
                setStyle(CircularProgressDrawable.LARGE)
                setColorSchemeColors(Color.WHITE)

                //bounds definition is required to show drawable correctly
                val size = (centerRadius + strokeWidth).toInt() * 2
                setBounds(0, 0, size, size)

                this.callback = object : Drawable.Callback {
                    override fun unscheduleDrawable(who: Drawable, what: Runnable) {
                    }

                    override fun invalidateDrawable(who: Drawable) {
                        buttonWidget.invalidate()
                    }

                    override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {
                    }
                }
            }
        }

        private fun createProgressDrawableSpannableString(
            progressDrawable: CircularProgressDrawable,
            onLoadingButtonText: String
        ): SpannableString {
            // create a drawable span using our progress drawable
            val drawableSpan = ProgressIndicatorSpan(progressDrawable)

            // create a SpannableString like "Loading [our_progress_bar]"
            return SpannableString("$onLoadingButtonText ").apply {
                setSpan(drawableSpan, length - 1, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }

    private var previousContext: Context? = null
    private var cachedProgressDrawableSpannableString: SpannableString? = null
    private var cachedProgressDrawable: CircularProgressDrawable? = null

    private fun createOrCacheProgressDrawableSpannableString(
        context: Context,
    ) {
        if (context == previousContext) return
        cachedProgressDrawable = createProgressDrawable(context, buttonWidget)
        cachedProgressDrawableSpannableString = createProgressDrawableSpannableString(
            cachedProgressDrawable!!,
            onLoadingButtonText,
        )
    }

    fun start(context: Context) {
        createOrCacheProgressDrawableSpannableString(context)
        cachedProgressDrawable?.start()
        buttonWidget.text = cachedProgressDrawableSpannableString
    }

    fun stop() {
        try {
            buttonWidget.text = buttonText
            cachedProgressDrawable?.stop()
        } catch (t: Throwable) {
            Timber.e(t)
        }
    }

    private class ProgressIndicatorSpan(
        progressDrawable: Drawable,
        val marginStart: Int = 20,
    ) : ImageSpan(progressDrawable) {
        override fun getSize(
            paint: Paint,
            text: CharSequence,
            start: Int,
            end: Int,
            fontMetricsInt: Paint.FontMetricsInt?
        ): Int {
            // get drawable dimensions
            val rect = drawable.bounds
            fontMetricsInt?.let {
                val fontMetrics = paint.fontMetricsInt

                // get a font height
                val lineHeight = fontMetrics.bottom - fontMetrics.top

                // make sure our drawable has height >= font height
                val drHeight = lineHeight.coerceAtLeast(rect.bottom - rect.top)

                val centerY = fontMetrics.top + lineHeight / 2

                // adjust font metrics to fit our drawable size
                fontMetricsInt.apply {
                    ascent = centerY - drHeight / 2
                    descent = centerY + drHeight / 2
                    top = ascent
                    bottom = descent
                }
            }

            // return drawable width which is in our case = drawable width + margin from text
            return rect.width() + marginStart
        }

        override fun draw(
            canvas: Canvas,
            text: CharSequence,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint
        ) {
            canvas.save()
            val fontMetrics = paint.fontMetricsInt
            // get font height. in our case now it's drawable height
            val lineHeight = fontMetrics.bottom - fontMetrics.top

            // adjust canvas vertically to draw drawable on text vertical center
            val centerY = y + fontMetrics.bottom - lineHeight / 2
            val transY = centerY - drawable.bounds.height() / 2

            // adjust canvas horizontally to draw drawable with defined margin from text
            canvas.translate(x + marginStart, transY.toFloat())

            drawable.draw(canvas)

            canvas.restore()
        }
    }
}