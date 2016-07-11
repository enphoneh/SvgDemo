package com.aven.svgdemo;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Administrator on 2016/6/28.
 */
public class SvgDrawerView extends ImageView {

    private ArrayList<Circle> mCircles;
    private ArrayList<PathShape> mPaths;

    public SvgDrawerView(Context context) {
        super(context);
//        pharseSvg();
        pharseSvgFromLocal();
    }

    public SvgDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        pharseSvg();
        pharseSvgFromLocal();
    }

    public SvgDrawerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        pharseSvg();
        pharseSvgFromLocal();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SvgDrawerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
//        pharseSvg();
        pharseSvgFromLocal();
    }

    private void pharseSvg() {

//        mCircles = new ArrayList<Circle>();
//        mPaths = new ArrayList<PathShape>();
//        long phaseTime = System.currentTimeMillis();
//        InputStream is = getResources().openRawResource(R.raw.simple_big);
//        Log.e("hyf", "InputStream time = " + (System.currentTimeMillis() - phaseTime));
//        SAXParserFactory spf = SAXParserFactory.newInstance();
//        SAXParser saxParser = null;
//        try {
//            saxParser = spf.newSAXParser();
//            //设置解析器的相关特性，true表示开启命名空间特性
//            XMLContentHandler handler = new XMLContentHandler();
//            saxParser.parse(is, handler);
//            is.close();
//        } catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Log.e("hyf", "phaseSvg = " + (System.currentTimeMillis() - phaseTime));
    }

    private void pharseSvgFromLocal() {
        mPaths = new ArrayList<PathShape>();
        long phaseTime = System.currentTimeMillis();
//        String[] paths = SvgDarwable.planet.trim().split("\\$");
//        for(String strPath : paths) {
//            int index = getColorIndex(strPath);
//            String color = strPath.substring(0,index);
//            String path = strPath.substring(index + 1);
//            pharsePath(color, path);
//        }
        int svglen = SvgDarwable.simple_big.length;
        for(int j = 0; j < svglen; j++) {
            int length = SvgDarwable.simple_big[j].length();
            Log.e("hyf", "length = " + length);
            StringBuilder sb = new StringBuilder();
            int color = 0;
            List<Float> params = new ArrayList<>();
            PathShape pathShape = new PathShape();
            Path path = new Path();
            char preOperator = ' ';
            for (int i = 0; i < length; i++) {
                char ch = SvgDarwable.simple_big[j].charAt(i);
                switch (ch) {
                    case '@':
                        color = Color.parseColor(sb.toString());
                        sb.delete(0, sb.length());
                        break;
                    case 'M':
                    case 'C':
                    case 'S':
                    case 'Z':
                    case 'L':
                    case 'A':
                    case 'Q':
                    case 'T':
                        producePathWithParams(path, preOperator, params);
                        params.clear();
                        preOperator = ch;
                        break;
                    case ' ':
                        if (sb.length() > 0) {
                            params.add(Float.parseFloat(sb.toString()));
                        }
                        sb.delete(0, sb.length());
                        break;
                    case '$':
                        pathShape.setFillColor(color);
                        pathShape.setPath(path);
                        mPaths.add(pathShape);
                        path = new Path();
                        pathShape = new PathShape();
                        break;
                    default:
                        sb.append(ch);
                        break;
                }
            }
        }
        Log.e("hyf", "pharseSvgFromLocal = " + (System.currentTimeMillis() - phaseTime));
    }

    private void producePathWithParams(Path path, char operator, List<Float> params){
        if (params.size() <= 0) {
            return;
        }
        if (operator == ' '){
            return;
        }
        switch (operator) {
            case 'M':
                float x = params.get(0);
                float y = params.get(1);
                path.moveTo(x,y);
                break;
            case 'C':
            case 'S':
                float x1 = params.get(0);
                float y1 = params.get(1);
                float x2 = params.get(2);
                float y2 = params.get(3);
                float x3 = params.get(4);
                float y3 = params.get(5);
                path.cubicTo(x1,y1,x2,y2,x3,y3);
                break;
            case 'Z':
                path.close();
                break;
            case 'L':
                x = params.get(0);
                y = params.get(1);
                path.lineTo(x,y);
                break;
            case 'A':
                float left = params.get(0);
                float top = params.get(1);
                float right = params.get(2);
                float bottom = params.get(3);
                float startAngle = params.get(4);
                float sweepAngle = params.get(5);
                path.arcTo(new RectF(left,top,right,bottom),startAngle,sweepAngle);
            case 'Q':
            case 'T':
                x1 = params.get(0);
                y1 = params.get(1);
                x2 = params.get(2);
                y2 = params.get(3);
                path.quadTo(x1, y1, x2, y2);
                break;
        }
    }

    private int getColorIndex(String strPath) {
        for(int i = 7 ; i < 11 ; i++) {
            if(strPath.charAt(i) == '@')
                return i;
        }
        return -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long drawTime = System.currentTimeMillis();
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.save();
//        for (Circle circle:mCircles)
//        canvas.scale(0.48f,0.48f);
//        canvas.scale(0.8f,0.8f);
        for(PathShape path:mPaths) {
            paint.setColor(path.getFillColor());
            canvas.drawPath(path.getPath(), paint);
        }
        canvas.restore();
        Log.e("hyf", "onDraw svgDrawer = " + (System.currentTimeMillis() - drawTime));
    }

    long docTime;
    int count = 0;
    StringBuilder stringBuilder;
    static final String PATH_DIR = "/sdcard/svg/";
    private class XMLContentHandler extends DefaultHandler{

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            docTime = System.currentTimeMillis();
            stringBuilder = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
//            Log.e("hyf" , "startElement localName= " + localName + " qName = ");
            if (localName.equals("circle")) {
                Circle circle = new Circle();
                int x = Integer.parseInt(attributes.getValue("cx"));
                int y = Integer.parseInt(attributes.getValue("cy"));
                int r = Integer.parseInt(attributes.getValue("r"));
                String color = attributes.getValue("fill");
                circle.setColor(Color.parseColor(color));
                circle.setX(x);
                circle.setY(y);
                circle.setRadio(r);
                mCircles.add(circle);
            } else if (localName.equalsIgnoreCase("path")) {
                String color = attributes.getValue("fill");
                String strPath = attributes.getValue("d");
                stringBuilder.append(color).append("@");
                stringBuilder.append(strPath.trim()).append("$");
                pharsePath(color, strPath);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
//            Log.e("hyf" , "endElement localName= " + localName + " qName = ");
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            Log.e("hyf", "doc time = " + (System.currentTimeMillis() - docTime));
            Log.e("hyf", "node count = " + count);
            File file = new File(PATH_DIR + "simple_big.txt");
            try {
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileOutputStream fis = new FileOutputStream(file,false);
                Log.e("hyf", "write = " + stringBuilder.toString());
                fis.write(stringBuilder.toString().getBytes());
                fis.flush();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void pharsePath(String color, String strPath) {
        String[] cmd = strPath.trim().split(" ");
        int index = 0;
        PathShape pathShape = new PathShape();
        pathShape.setFillColor(Color.parseColor(color));
        Path path = new Path();
        while (cmd.length > 0) {
            count++;
//                    Log.e("hyf", "cmd = " + cmd[index]);
            switch (cmd[index].toUpperCase()) {
                case "M":
                    float x = Float.parseFloat(cmd[index + 1]);
                    float y = Float.parseFloat(cmd[index + 2]);
                    path.moveTo(x,y);
                    index += 3;
                    break;
                case "C":
                case "S":
                    float x1 = Float.parseFloat(cmd[index + 1]);
                    float y1 = Float.parseFloat(cmd[index + 2]);
                    float x2 = Float.parseFloat(cmd[index + 3]);
                    float y2 = Float.parseFloat(cmd[index + 4]);
                    float x3 = Float.parseFloat(cmd[index + 5]);
                    float y3 = Float.parseFloat(cmd[index + 6]);
                    path.cubicTo(x1,y1,x2,y2,x3,y3);
                    index += 7;
                    break;
                case "Z":
                    path.close();
                    index += 1;
                    break;
                case "L":
                    x = Float.parseFloat(cmd[index + 1]);
                    y = Float.parseFloat(cmd[index + 2]);
                    path.lineTo(x,y);
                    index += 3;
                    break;
                case "A":
                    float left = Float.parseFloat(cmd[index + 1]);
                    float top = Float.parseFloat(cmd[index + 2]);
                    float right = Float.parseFloat(cmd[index + 3]);
                    float bottom = Float.parseFloat(cmd[index + 4]);
                    float startAngle = Float.parseFloat(cmd[index + 5]);
                    float sweepAngle = Float.parseFloat(cmd[index + 6]);
                    path.arcTo(new RectF(left,top,right,bottom),startAngle,sweepAngle);
                    index += 7;
                case "Q":
                case "T":
                    x1 = Float.parseFloat(cmd[index + 1]);
                    y1 = Float.parseFloat(cmd[index + 2]);
                    x2 = Float.parseFloat(cmd[index + 3]);
                    y2 = Float.parseFloat(cmd[index + 4]);
                    path.quadTo(x1, y1, x2, y2);
                    index += 5;
                    break;
                default:
                    index += 1;
                    break;

            }
            if (index >= cmd.length) {
                break;
            }
        }
        pathShape.path = path;
        mPaths.add(pathShape);
    }

}
