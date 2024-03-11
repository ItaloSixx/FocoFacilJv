package com.example.focofacil.Activity;

import android.widget.ImageView;
import androidx.databinding.BindingAdapter;

import kotlin.jvm.JvmStatic;

//public class BindingAdapters {
//}


public class BindingAdapters {

    @BindingAdapter("imageRes")
    public static void setImageRes(ImageView imageView, int drawableRes) {
        imageView.setImageResource(drawableRes);
    }
}
