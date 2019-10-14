package com.example.scrumproyect.view.ui.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point
import android.os.Environment
import android.view.WindowManager
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

    /**
     * Get Width of screen
     * */
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

        fun getTime(time: Long): String {

            val minutes: Int = TimeUnit.MINUTES.convert(Date().time - time, TimeUnit.MILLISECONDS).toInt()

            if (minutes == 0) {
                return "ahora"
            } else if (minutes < 60) {
                return "hace $minutes min"
            } else if (minutes < (60 * 24)) {
                return "hace " + (minutes / 60) + " hrs"
            } else if (minutes < (60 * 24 * 30)) {
                return "hace " + (minutes / (60 * 24)) + " días"
            } else {
                return "hace " + (minutes / (60 * 24 * 30)) + " meses"
            }
        }
    }
}