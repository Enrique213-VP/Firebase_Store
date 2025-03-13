package com.firebaseauth.core

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class RadioButton(context: Context, attributeSet: AttributeSet) :
    AppCompatRadioButton(context, attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont() {

        val typeface: Typeface = Typeface.createFromAsset(context.assets, "RubikSprayPaint-Regular.ttf")
        setTypeface(typeface)
    }


}