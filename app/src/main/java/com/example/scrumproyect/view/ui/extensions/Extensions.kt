@file:Suppress("unused")

package com.example.scrumproyect.view.ui.extensions

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import java.io.Serializable

fun Context.postDelayed(unit: () -> Unit, delay: Long) {
    Handler().postDelayed(unit, delay)
}

fun AppCompatActivity.startActivityPostDelayed(cls: Class<out AppCompatActivity>, delay: Long, vararg extras: Serializable) {
    val intent = Intent(this, cls)

    for (i in 0 until extras.size) {
        intent.putExtra("extra$i", extras[i])
    }

    postDelayed({ startActivity(intent) }, delay)
}

//fun Toast.makeText(message: String) = Toast.makeText(con)

fun Double.isEmpty() = this == 0.0

fun Any.toDouble(): Double {
    return (this as? Int)?.toDouble() ?: if (this is String) {
        java.lang.Double.parseDouble(this)
    } else {
        try {
            java.lang.Double.parseDouble(this.toString())
        } catch (e: Exception) {
            0.0
        }
    }
}

fun Any?.toInt(): Int {
    if (this == null) {
        return -1
    } else if (this is Double) {
        return this.toInt()
    } else if (this is String) {
        try {
            return Integer.parseInt(this)
        } catch (e: Exception) {
            /** maybe parse to double */
            return -1
        }
    } else {
        return Integer.parseInt(java.lang.String.valueOf(this))
    }
}

fun TextView.isEmpty() = getString().isEmpty()

fun TextView.isNotEmpty() = getString().isNotEmpty()

fun TextView.getString() = text.toString().trim()

fun String.trimBetween() = trim().replace("\\s+".toRegex(), " ")

fun String?.safeNull() = this ?: ""

fun TextView.clean() = setText("")

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun LayoutInflater.inflate(@LayoutRes layoutRes: Int): View = inflate(layoutRes, null)

fun ImageView.setImageDrawable(@DrawableRes drawableRes: Int) = setImageDrawable(getDrawable(drawableRes))

fun AppCompatActivity.startActivity(cls: Class<out AppCompatActivity>, vararg extras: Serializable) {
    val intent = Intent(this, cls)

    for (i in 0 until extras.size) {
        intent.putExtra("extra$i", extras[i])
    }

    startActivity(intent)
}

fun String.firstCap(): String {
    return try {
        this.substring(0, 1).toUpperCase() + this.substring(1, this.length).toLowerCase()
    } catch (e: Exception) {
        ""
    }
}

fun String.firstAllCaps(): String {
    val builder = StringBuilder()

    for (part in this.split(" ")) {
        builder.append(part.firstCap())
        builder.append(" ")
    }

    return builder.toString().trim()
}

fun TextInputLayout.showError(@StringRes message: Int) {
    showError(context.getString(message))
}

fun TextInputLayout.showError(message: String?) {
    this.error = message
    this.getChildAt(1).visibility = View.VISIBLE
}

fun TextInputLayout.cleanError() {
    this.error = null
    this.getChildAt(1).visibility = View.GONE
}

fun TextInputLayout.enableError() {
    this.isErrorEnabled = true
    this.cleanError()
}

fun EditText.addOnIsEmpty(listener: () -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (s.isEmpty()) listener()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    })
}

fun EditText.getWrapper() = this.parent.parent as TextInputLayout

fun EditText.showError(@StringRes message: Int) {
    showError(context.getString(message))
}

fun EditText.showError(message: String) {
    val parent = this.parent.parent as TextInputLayout
    parent.showError(message)
}

fun EditText.cleanError() {
    try {
        val parent = this.parent.parent as TextInputLayout
        parent.cleanError()
    } catch (ignore: Exception) { }
}

fun EditText.enableError() {
    val parent = this.parent.parent as TextInputLayout
    parent.enableError()
}

fun EditText.addActionListener(unit: () -> Unit) {
    this.setOnEditorActionListener { _, actionId, _ ->
        var handled = false

        if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
            unit()
            handled = true
        } else if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND) {
            unit()
            handled = true
        }

        handled
    }
}

fun TextInputLayout.configCountLetter(maxLength: Int) {
    this.isCounterEnabled = true
    this.isErrorEnabled = true
    this.error = null
    this.counterMaxLength = maxLength
}

fun EditText.filterCountLetter(maxLength: Int) {
    val fArray = arrayOfNulls<InputFilter>(1)
    fArray[0] = InputFilter.LengthFilter(maxLength)
    this.filters = fArray

}

fun EditText.maxLength(max: Int) {
    this.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(max))
}

fun View.addOnGlobalLayoutListener(unit: () -> Unit) {
    val view = this
    view.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            unit()
            view.viewTreeObserver.removeOnGlobalLayoutListener(this)
        }
    })
}

fun View.getDrawable(@DrawableRes drawableRes: Int) = ContextCompat.getDrawable(context, drawableRes)

fun View.getColor(@ColorRes colorRes: Int) = ContextCompat.getColor(context, colorRes)

fun EditText.disableEmojis() {
    filters = arrayOf(InputFilter { source, start, end, _, _, _ ->

        for (index in start until end) {

            val type = Character.getType(source[index])
            if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                return@InputFilter ""
            }
        }
        null
    })
}

fun EditText.disableSpecialCharacter(arrayList: List<String>) {
    filters = arrayOf(InputFilter { source, start, end, _, _, _ ->

        for (index in start until end) {
            if (!Character.isLetterOrDigit(source[index])) {
               /* if ((source[index]).toString() != " " && (source[index]).toString() != "," && (source[index]).toString() != "." && (source[index]).toString() != "/" && (source[index]).toString() != "'" && (source[index]).toString() != "-") {
                    return@InputFilter ""
                }*/
                if ((source[index]).toString() !in arrayList){
                    return@InputFilter ""
                }
            }
        }
        null
    })
}

@Suppress("DEPRECATED_IDENTITY_EQUALS")
fun String.toStrng(): String {
    //Change Binaries to String
    val sb = StringBuilder()
    val mapping = intArrayOf(1, 2, 4, 8, 16, 32, 64, 128)
    var j = 0
    while (j < this.length) {
        var idx = 0
        var sum = 0
        for (i in 7 downTo 0) {
            if (this[i + j] === '1') {
                sum += mapping[idx]
            }
            idx++
        }
        sb.append(Character.toChars(sum))
        j += 9
    }
    return sb.toString()
}