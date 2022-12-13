package com.firebaseauth.core

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class RadioButton(context: Context, attributeSet: AttributeSet) :
    AppCompatRadioButton(context, attributeSet) {

    init {
        //Call the function to apply the font to the components
        applyFont()
    }

    private fun applyFont() {

        //This is used to get the file from the assets folder and set it to the tittle textView
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "RubikSprayPaint-Regular.ttf")
        setTypeface(typeface)
    }


}