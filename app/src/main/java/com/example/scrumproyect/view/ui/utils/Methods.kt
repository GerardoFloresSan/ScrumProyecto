package com.example.scrumproyect.view.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.scrumproyect.R
import de.hdodenhof.circleimageview.CircleImageView
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

class Methods(private val context: Context) {

    private fun getPoint(): Point {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay

        val size = Point()
        display.getSize(size)

        return size
    }

    fun toPixels(dpi: Float): Float {
        val d = context.resources.displayMetrics.density
        return dpi * d
    }

    fun getWidthScreen(): Int {
        return getPoint().x
    }

    fun getHeightScreen(): Int {
        return getPoint().y
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var methods: Methods? = null

        fun init(context: Context) {
            methods = Methods(context)
        }

        @SuppressLint("NewApi")
        fun isInternetConnected(): Boolean {
            var isConnected = false
            val cm = methods?.context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm.run {
                    cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                        isConnected = when {
                            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                            hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                            else -> false
                        }
                    }
                }
            } else {
                cm.run {
                    cm.activeNetworkInfo?.run {
                        if (type == ConnectivityManager.TYPE_WIFI) {
                            isConnected = this.isConnectedOrConnecting
                        } else if (type == ConnectivityManager.TYPE_MOBILE) {
                            isConnected = this.isConnectedOrConnecting
                        }
                    }
                }
            }
            Log.d("tag-connected", isConnected.toString())
            return isConnected
        }

        /**
         * Return only first line, and key is true or false
         * */
        fun getLines(fileName: String): Map<Boolean, List<String>> {

            val lines = mutableListOf<String>()
            var result: MutableMap<Boolean, List<String>> = mutableMapOf(false to lines)

            val path = StringBuilder()
            path.append(Environment.getExternalStorageDirectory())
            path.append("/Ama/")
            path.append(fileName)

            val file = File(path.toString())

            if (!file.exists()) return result

            file.forEachLine {
                lines.add(it)
            }

            if (!lines.isEmpty()) {
                result.clear()
                result = mutableMapOf(true to lines)
            }

            return result
        }

        fun getWidthScreen(): Int {
            return methods?.getWidthScreen() ?: 0
        }

        fun getHeightScreen(): Int {
            return methods?.getHeightScreen() ?: 0
        }

        fun toPixels(dpi: Float): Float {
            return methods?.toPixels(dpi) ?: 0f
        }

        fun getRandom(min: Int, max: Int): Int {
            var min = min
            var max = max
            max = Math.pow(10.0, max.toDouble()).toInt()
            min = Math.pow(10.0, min.toDouble()).toInt()
            return Random().nextInt(max - min + 1) + min
        }

        fun ifString(s: String): Int {
            return try {
                if (s.toInt() == -1) 88 else s.toInt()
            } catch (e: NumberFormatException) {
                88
            }
        }

        fun drawable(avatar: CircleImageView): Drawable? {
            val img = ContextCompat.getDrawable(methods!!.context, R.drawable.ic_person_white_24dp)
            avatar.setColorFilter(ContextCompat.getColor(avatar.context, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP)
            return img
        }

        fun getTime(time: Long): String {

            val minutes: Int = TimeUnit.MINUTES.convert(Date().time - time, TimeUnit.MILLISECONDS).toInt()

            return when {
                minutes == 0 -> "ahora"
                minutes < 60 -> "hace $minutes min"
                minutes < (60 * 24) -> "hace " + (minutes / 60) + " hrs"
                minutes < (60 * 24 * 30) -> "hace " + (minutes / (60 * 24)) + " días"
                else -> "hace " + (minutes / (60 * 24 * 30)) + " meses"
            }
        }
    }
}