package com.aven.svgdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/6/28.
 */
public class SvgImageView extends ImageView{
    public SvgImageView(Context context) {
        super(context);
        loadDrawable();
    }

    public SvgImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadDrawable();
    }

    public SvgImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadDrawable();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SvgImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        loadDrawable();
    }

    private void loadDrawable() {
        long timeload = System.currentTimeMillis();
        Drawable drawable;
        if (Build.VERSION.SDK_INT < 21) {
            drawable = getResources().getDrawable(R.drawable.circle);
        } else {
            drawable = getResources().getDrawable(R.drawable.circle, null);
        }
        setImageDrawable(drawable);
        Log.e("hyf", "loadDrawable = " + (System.currentTimeMillis() - timeload));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long time = System.currentTimeMillis();
        super.onDraw(canvas);
        Log.e("hyf" , "onDraw svgImage = " + (System.currentTimeMillis() - time));
    }

}
