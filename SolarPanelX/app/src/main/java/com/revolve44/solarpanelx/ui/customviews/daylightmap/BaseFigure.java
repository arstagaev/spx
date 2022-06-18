package com.revolve44.solarpanelx.ui.customviews.daylightmap;


import static com.revolve44.solarpanelx.ui.customviews.daylightmap.WeatherAnim.WIDTH;

public class BaseFigure {

    public static float seasonPPCoeff = 1;

    public static float x = 0;
    public static float y = 0;

    //public static int WIDTH  =  0;
    //public static int HEIGHT =  0;

//    public static int xn = 0;
//    public static int yn = 0;

    //public static final int AxConst = 0;
    //public static final int BxConst = 100; // ><
    //public static final int CxConst = 200;
    //public static final int DxConst = 300;
    //public static final int FxConst = 400; // ><
    //public static final int ExConst = 600;

    public static void toDefault(){
        CURRENT_TIME = 0;

        Ax =  0f;;
        Bx = (WIDTH)  /5f; // ><; // ><
        Cx = (WIDTH*2f)/5f;;
        Dx = (WIDTH*3f)/5f;;
        Fx = (WIDTH*4f)/5f; // ><; // ><
        Ex = WIDTH;;

        Ayy =  (WIDTH*2f)/5f;
        Byy =  (WIDTH)  /5f; // ><;
        Cyy =  0F;
        Dyy =  WIDTH;
        Fyy =  (WIDTH*4f)/5f; // ><;
        Eyy =  (WIDTH*3f)/5f;
    }

    public static float Ax =  0f;;
    public static float Bx = (WIDTH)  /5f; // ><; // ><
    public static float Cx = (WIDTH*2f)/5f;;
    public static float Dx = (WIDTH*3f)/5f;;
    public static float Fx = (WIDTH*4f)/5f; // ><; // ><
    public static float Ex = WIDTH;;



    public static float Ainv =  (WIDTH*2f)/5f;
    public static float Binv =  (WIDTH)  /5f; // ><; // ><
    public static float Cinv =  0F;
    public static float Dinv =  WIDTH;
    public static float Finv =  (WIDTH*4f)/5f; // ><; // ><
    public static float Einv =  (WIDTH*3f)/5f;


    public static float Ayy =  (WIDTH*2f)/5f;
    public static float Byy =  (WIDTH)  /5f; // ><;
    public static float Cyy =  0F;
    public static float Dyy =  WIDTH;
    public static float Fyy =  (WIDTH*4f)/5f; // ><;
    public static float Eyy =  (WIDTH*3f)/5f;

//    public static float Ainv =  (WIDTH*1f)/5f;;;
//    public static float Binv =  (WIDTH)  /5f; // ><; // ><
//    public static float Cinv =  0F;;
//    public static float Dinv =  (WIDTH);;
//    public static float Finv =  (WIDTH*4f)/5f; // ><; // ><
//    public static float Einv =  (WIDTH*4f)/5f;;


    static float CURRENT_TIME = 0f;


    public static final float coeffLittle = WIDTH/12F;



    //static int timeOfDay = 1;




    //public static int currentTime = ((800/1440)*0+(800/1440)*60*13);
    public static int gravityPoint = 250;

    public static final boolean isDEBUG = true;
    public static boolean isSummer = true;

    public static int XLocation = 0;
    public static int YLocation = 0;

}