package com.aeropay_merchant.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import com.aeropay_merchant.R;


public class CustomEditText_APSDK extends AppCompatEditText {

    private final Context context;
    private String typeface;

    public CustomEditText_APSDK(Context context, String typeface) {
        super(context);
        this.context = context;
        this.typeface = typeface;
    }

    public CustomEditText_APSDK(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TextView);

        typeface = a.getString(R.styleable.TextView_ctypeface);
        setCTypeFace(typeface);
    }

    @Override
    public void setInputType(int type) {
        super.setInputType(type);
        setCTypeFace(typeface);
    }

    public void setCTypeFace(String tf) {
        typeface = tf;
        if (tf != null) {
            FontManager_APSDK.getInstance(context).setTypeFace(this, tf);
        }
    }

}
