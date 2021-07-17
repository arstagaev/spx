package com.revolve44.solarpanelx.ui.customviews.daylightmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;


public class Curve extends BaseFigure {

    static int colorSoft = Color.parseColor("#9A222222");
    static int colorSoft2 = Color.parseColor("#4A222222");

    static int c1 = Color.argb(80,0, 0,0);
    static int c2 = Color.argb(99,0, 0,0);
    static float halfheight = 0;
    static float halfwidth = 0;


    //public static float getTimeOfDay(){
    //   return ((WIDTH/1440)*60*13);
    //



    public static void curveFirst(Canvas canvas, Path path, Paint paint, float timeOfDay, float height, float width){
        halfheight = height/2;
        halfwidth = width/2;

        timeOfDay = timeOfDay+CURRENT_TIME;
        //gravityPoint = Ex-150;
        //System.out.println("time is "+timeOfDay + "Current time const "+ CURRENT_TIME+" width is "+WIDTH + " pizdec "+((600F/1440F)*60F*13F));
        //System.out.println("ABCDFE "+"E "+Ex);


        paint.setColor(c1);
        path.moveTo(Ax+timeOfDay,y);
        path.cubicTo(
                Ax+timeOfDay,y,
                Bx+timeOfDay,y,
                Bx+timeOfDay,y+halfheight);
        path.cubicTo(
                Bx+timeOfDay,y+halfheight,
                Bx+timeOfDay,y+height*0.96F,
                Cx+timeOfDay,y+height*0.96F);
        path.lineTo(Dx+timeOfDay,y+height*0.96F);
        path.cubicTo(
                Dx+timeOfDay,y+height*0.96F,
                Fx+timeOfDay,y+height*0.96F,
                Fx+timeOfDay,y+halfheight);
        path.cubicTo(
                Fx+timeOfDay,y+halfheight,
                Fx+timeOfDay,y,
                Ex+timeOfDay,y);
        path.moveTo(Ax+timeOfDay,y);
        path.close();


        canvas.drawPath(path, paint);
    }


    public static void curveFirstShadow(Canvas canvas, Path path, Paint paint, float timeOfDay, float height, float width){
        halfheight = height/2;
        halfwidth = width/2;

        timeOfDay = timeOfDay+ CURRENT_TIME;
        //gravityPoint = Ex-150;

        paint.setColor(c2);
        path.moveTo(Ax+coeffLittle +timeOfDay,y);
        path.cubicTo(
                Ax+coeffLittle+timeOfDay,y,
                Bx+coeffLittle+timeOfDay,y,
                Bx+coeffLittle+timeOfDay,y+halfheight);
        path.cubicTo(
                Bx+coeffLittle+timeOfDay,y+halfheight,
                Bx+coeffLittle+timeOfDay,y+height*0.94F,
                Cx+coeffLittle+timeOfDay,y+height*0.94F);
        path.lineTo(Dx-coeffLittle+timeOfDay,y+height*0.94F);
        path.cubicTo(
                Dx-coeffLittle+timeOfDay,y+height*0.94F,
                Fx-coeffLittle+timeOfDay,y+height*0.94F,
                Fx-coeffLittle+timeOfDay,y+halfheight);
        path.cubicTo(
                Fx-coeffLittle+timeOfDay,y+halfheight,
                Fx-coeffLittle+timeOfDay,y,
                Ex-coeffLittle+timeOfDay,y);
        path.moveTo(Ax+coeffLittle+timeOfDay,y);
        path.close();

        canvas.drawPath(path, paint);
    }






    public static void curveSummer(Canvas canvas, Path path, Paint paint, float timeOfDay, float height, float width){
        halfheight = height/2;
        halfwidth = width/2;

        timeOfDay = timeOfDay+CURRENT_TIME;
        //gravityPoint = Ex-150;
        //System.out.println("time is "+timeOfDay + "Current time const "+ CURRENT_TIME+" width is "+WIDTH + " pizdec "+((600F/1440F)*60F*13F));
        //System.out.println("ABCDFE "+"E "+Ex);
        float coeffx = 0;


        paint.setColor(c1);
        path.moveTo(Ayy+timeOfDay,y);
        path.cubicTo(
                Ayy+timeOfDay,y,
                Byy+timeOfDay,y,
                Byy+timeOfDay,y+halfheight);
        path.cubicTo(
                Byy+timeOfDay,y+halfheight,
                Byy+timeOfDay,y+height,
                Cyy+timeOfDay,y+height);
        path.lineTo(Dyy+timeOfDay,y+height);
        path.cubicTo(
                Dyy+timeOfDay,y+height,
                Fyy+timeOfDay,y+height,
                Fyy+timeOfDay,y+halfheight);
        path.cubicTo(
                Fyy+timeOfDay,y+halfheight,
                Fyy+timeOfDay,y,
                Eyy+timeOfDay,y);
        path.moveTo(Ayy+timeOfDay,y);
        path.close();


        canvas.drawPath(path, paint);
    }

    public static void curveSummerShadow(Canvas canvas, Path path, Paint paint, float timeOfDay, float height, float width){
        halfheight = height/2;
        halfwidth = width/2;

        timeOfDay = timeOfDay+CURRENT_TIME;
        //gravityPoint = Ex-150;
       // System.out.println("time is "+timeOfDay + "Current time const "+ CURRENT_TIME+" width is "+WIDTH + " pizdec "+((600F/1440F)*60F*13F));
       // System.out.println("ABCDFE "+"E "+Ex);
        float coeffx = 0;


        paint.setColor(c2);
        path.moveTo(Ayy+coeffLittle+timeOfDay,y+height*0.06F);
        path.cubicTo(
                Ayy+coeffLittle+timeOfDay,y+height*0.06F,
                Byy+coeffLittle+timeOfDay,y+height*0.06F,
                Byy+coeffLittle+timeOfDay,y+halfheight);
        path.cubicTo(
                Byy+coeffLittle+timeOfDay,y+halfheight,
                Byy+coeffLittle+timeOfDay,y+height,
                Cyy+coeffLittle+timeOfDay,y+height);
        path.lineTo(Dyy-coeffLittle+timeOfDay,y+height);
        path.cubicTo(
                Dyy-coeffLittle+timeOfDay,y+height,
                Fyy-coeffLittle+timeOfDay,y+height,
                Fyy-coeffLittle+timeOfDay,y+halfheight);
        path.cubicTo(
                Fyy-coeffLittle+timeOfDay,y+halfheight,
                Fyy-coeffLittle+timeOfDay,y+height*0.06F,
                Eyy-coeffLittle+timeOfDay,y+height*0.06F);
        path.moveTo(Ayy+coeffLittle+timeOfDay,y+height*0.06F);
        path.close();


        canvas.drawPath(path, paint);
        timeOfDay = 0;
    }
}
