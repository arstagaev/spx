package com.revolve44.solarpanelx.ui.customviews.daylightmap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.revolve44.solarpanelx.R;
import com.revolve44.solarpanelx.global_utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import timber.log.Timber;

import static com.revolve44.solarpanelx.ui.customviews.daylightmap.BaseFigure.Ax;
import static com.revolve44.solarpanelx.ui.customviews.daylightmap.BaseFigure.Bx;
import static com.revolve44.solarpanelx.ui.customviews.daylightmap.BaseFigure.Cx;
import static com.revolve44.solarpanelx.ui.customviews.daylightmap.BaseFigure.Dx;
import static com.revolve44.solarpanelx.ui.customviews.daylightmap.BaseFigure.Ex;
import static com.revolve44.solarpanelx.ui.customviews.daylightmap.BaseFigure.Fx;
import static com.revolve44.solarpanelx.ui.customviews.daylightmap.BaseFigure.isSummer;


/**
@author arstagaev, 2020
 */

public class WeatherAnim extends View {

    private Paint paint;
    private Paint paint2;
    private Paint paint3;

    private Path path;
    private Path path2;
    private Path path3;
    private Path path4;

    //set sizes
    DisplayMetrics displaymetrics = new DisplayMetrics();
    //    int width =  displaymetrics.widthPixels;
//    int height = displaymetrics.heightPixels;
    Bitmap bm;

    public static float WIDTH  =  600;
    public static float HEIGHT =  300;




    public static void geometryL(){
        Ax =            0F;
        Bx = ((WIDTH)   /5F)*1F; // ><
        Cx = ((WIDTH*2F)/5F)*1F;
        Dx = ((WIDTH*3F)/5F)*1F;
        Fx = ((WIDTH*4F)/5F)*1F; // ><
        Ex = WIDTH;

        //quickSetup();
    }


    public WeatherAnim(Context context) {
        super(context);
    }

    public WeatherAnim(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //quickSetup();

        path = new Path();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        path2 = new Path();
        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setStyle(Paint.Style.FILL_AND_STROKE);

        path3 = new Path();
        paint3 = new Paint();
        paint3.setAntiAlias(true);
        paint3.setStyle(Paint.Style.FILL_AND_STROKE);

        path3 = new Path();

        path4 = new Path();

        bm= BitmapFactory.decodeResource(context.getResources(), R.drawable.bright_map);



    }


    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld){
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        //Log.i("sizech","new old"+yNew+" |" + yOld);

        WIDTH = xNew;
        HEIGHT = yNew;
        geometryL();
        Timber.i("new sizes "+WIDTH+"  "+HEIGHT);
        //width = WIDTH;
        //height = HEIGHT;


//        Ax =   0F;
//        Bx = (WIDTH)/5F; // ><
//        Cx = (WIDTH*2F)/5F;
//        Dx = (WIDTH*3F)/5F;
//        Fx = (WIDTH*4F)/5F; // ><
//        Ex = WIDTH;
//
        if (Constants.switcherMap){
            quickSetup();
            Constants.switcherMap = false;
        }


        //Log.d("width: "," is "+WIDTH+" ][ "+HEIGHT);
        //System.out.println("xxx OnSizchanged");
    }


    private void refreshView(){
        /*
        USE this if you need manual real time settings with SeekBars
         */
        //Clear arrays
        paint.reset();

    }
    Bitmap mDrawBitmap;
    Canvas mBitmapCanvas;
    Paint drawPaint = new Paint();

    @SuppressLint("DrawAllocation")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //System.out.println("xxx OnDraw width is "+WIDTH);
        drawPaint.setColor(Color.RED);

        if (mDrawBitmap == null) {
            mDrawBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            mBitmapCanvas = new Canvas(mDrawBitmap);
        }



        mBitmapCanvas.drawBitmap(bm,null,new RectF(0F,0F,WIDTH,HEIGHT),paint3);

        //System.out.println("is sum "+isSummer);

        if (isSummer) {

            Curve.curveSummer(      mBitmapCanvas,path,paint, WIDTH*1.005F,HEIGHT,WIDTH);
            Curve.curveSummerShadow(mBitmapCanvas,path2,paint,WIDTH*1.005F,HEIGHT,WIDTH);


            Curve.curveSummer(      mBitmapCanvas,path3,paint2,0,HEIGHT,WIDTH);
            Curve.curveSummerShadow(mBitmapCanvas,path4,paint2,0,HEIGHT,WIDTH);

        } else {

            Curve.curveFirst(       mBitmapCanvas,path,paint,0F,HEIGHT,WIDTH);
            Curve.curveFirstShadow(mBitmapCanvas,path2,paint,0F,HEIGHT,WIDTH);

            Curve.curveFirst(      mBitmapCanvas,path3,paint,WIDTH*1.1F,HEIGHT,WIDTH);
            Curve.curveFirstShadow(mBitmapCanvas,path4,paint,WIDTH*1.1F,HEIGHT,WIDTH);
        }
        // Curve.curveFirstShadow(path2,paint3,width,height,width);
        //canvas.drawPath(path, paint);
        // draw everything to the screen
        canvas.drawBitmap(mDrawBitmap, 0, 0, drawPaint);
        Constants.bitmapMain = mDrawBitmap;

        paint.reset();
        path.reset();
        path2.reset();
        path3.reset();
        path4.reset();


        invalidate();
        //System.out.println("reloadfast! " +WIDTH+" // "+ HEIGHT+" bx ex "+Bx+" ex "+Ex);
    }



    public static void manualX(int hr){

        timeMachine(hr,00,0,true);
    }

    public static int hour = 0;
    public static int min = 0;
    public static int dayOfYear = 0;
    public static int monthOfYear = 0;

    public static void quickSetup(){
        AnimationSeason.initTimeLine();


        Calendar calendar = Calendar.getInstance();
        Date date = new Date();
        dayOfYear = calendar.get(Calendar.DAY_OF_YEAR); //
        monthOfYear = Integer.parseInt((String) DateFormat.format("MM",   date));

        long timestampUTC = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        hour = Integer.parseInt(sdf.format(new Date(timestampUTC))); //

        SimpleDateFormat sdf2 = new SimpleDateFormat("mm");
        sdf2.setTimeZone(TimeZone.getTimeZone("GMT+0"));
        min = Integer.parseInt((sdf2.format(new Date(timestampUTC)))); //

        //.out.println("current time UTC: "+hour +":"+ min);

        if (monthOfYear<=3 || monthOfYear>=9){
            isSummer = false;
        }else {
            isSummer = true;
        }
        //System.out.println("leto is "+isSummer+" month of year = "+monthOfYear);

        //hour=13;
        // min =;
        dayOfYear=0;

        //monthOfYear = 4;
        System.out.println("timex: "+hour+":"+min);


        //timeMachine(hour,min,dayOfYear,true);
        timeMachine(hour,min,dayOfYear,true);

        //calcMyLocation(36.71F,-85.303F); // USA Atlanta
        //calcMyLocation(-23.664F,-47.047F); // Brasil Santa Paulo

    }


    /////////////////////////////////////////////////////////////////////////
    //       Set Here Times   !!!      Here set only UTC time    !!!       //
    /////////////////////////////////////////////////////////////////////////
    // 21 march summer 21 sep

    public static void timeMachine(float hr, float min,float day, boolean manual){
        //Ax =   0;
        //Bx = 100;
        //Cx = 200;
        //Dx = 300;
        //Fx = 400;
        //Ex = 500;

        manual = BaseFigure.isDEBUG;

        if(hr>24 || min>60 || day>366 || hr<0 || min<0 || day<0 ){
            Log.e("in map","ERROR: illegal number");

            return;
        }
        //System.out.println("is debug "+ BaseFigure.isDEBUG);

        AnimationSeason.dayDynamics(-(float) ((WIDTH/1440F)*min + (WIDTH/1440F)*60F*hr));


        //System.out.println("xxx quicksetup stop  ");
    }
}
