package com.aven.svgdemo;

import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.larvalabs.svgandroid.SVGParser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        long time = System.currentTimeMillis();
//        PictureDrawable pictureDrawable = SVGParser.getSVGFromResource(getResources(), R.raw.planet_svg, 500, 500).createPictureDrawable();
//        Log.e("hyf","time1 = " + (System.currentTimeMillis() - time));
//        ((SvgImageView) findViewById(R.id.svg)).setImageDrawable(pictureDrawable);
    }
}
