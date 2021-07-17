package com.revolve44.solarpanelx.ui.customviews.daylightmap;




import com.revolve44.solarpanelx.ui.customviews.daylightmap.enums.Direction;
import com.revolve44.solarpanelx.ui.customviews.daylightmap.enums.StageOfYear;

import static com.revolve44.solarpanelx.ui.customviews.daylightmap.WeatherAnim.WIDTH;
import static com.revolve44.solarpanelx.ui.customviews.daylightmap.enums.StageOfYear.FIRST;
import static com.revolve44.solarpanelx.ui.customviews.daylightmap.enums.StageOfYear.FOURTH;
import static com.revolve44.solarpanelx.ui.customviews.daylightmap.enums.StageOfYear.SECOND;
import static com.revolve44.solarpanelx.ui.customviews.daylightmap.enums.StageOfYear.THIRD;

public class AnimationSeason extends BaseFigure {

    AnimationSeason(){
        System.out.println("Launch BaseFigure! ");
        //middlePointX = Fx-Bx;

    }

    public static float REMBO  =  (WIDTH / 2.5F) * 1F;
    public static float REMBO2 =  (WIDTH / 2.5F) * 2F;
    public static float REMBO3 =  (WIDTH / 2.5F) * 3F;
    public static float REMBO4 =  (WIDTH / 2.5F) * 4F;


    public static StageOfYear stageOfYear;
    public static int minutes;

    public static void initTimeLine(){
        stageOfYear = FIRST;
        minutes = 0;
    }

    ///for Animation
    public static void dayDynamics(float days){
        //System.out.println("new day dynamic "+days);

        CURRENT_TIME = 0;

        CURRENT_TIME = days;

    }
    //its works yep
    public static void seasonDynamics(float setDaysCoefficient){
        System.out.println("day coeff is "+ setDaysCoefficient);

        if (setDaysCoefficient > REMBO4){
            BaseFigure.seasonPPCoeff = 0;
            setDaysCoefficient = 0;
        }
        if (setDaysCoefficient < 0){
            BaseFigure.seasonPPCoeff = REMBO4;
            setDaysCoefficient = REMBO4;
        }

        Ax =  0;
        Bx =(WIDTH)/5; // ><
        Cx =(WIDTH*2)/5;
        Dx =(WIDTH*3)/5;
        Fx =(WIDTH*4)/5; // ><
        Ex =WIDTH;

        Ainv =  (WIDTH*2f)/5f;
        Binv =  (WIDTH)  /5f; // ><; // ><
        Cinv =  0F;
        Dinv =  WIDTH;
        Finv =  (WIDTH*4f)/5f; // ><; // ><
        Einv =  (WIDTH*3f)/5f;

        //REMBO =  200;
        //REMBO2 = 400;
        //REMBO3=  600;
        //REMBO4 = 800;


        if (setDaysCoefficient<=REMBO){
            stageOfYear = FIRST;
        }else if (setDaysCoefficient<=REMBO2){
            stageOfYear = SECOND;
        }else if(setDaysCoefficient<=REMBO3){
            stageOfYear = THIRD;
        }else if(setDaysCoefficient<=REMBO4){
            stageOfYear = FOURTH;
        }

        if (stageOfYear == FIRST){
            setAx(setDaysCoefficient, Direction.RIGHT);
            setEx(setDaysCoefficient, Direction.LEFT);

        }else if (stageOfYear == SECOND){
            setDaysCoefficient = setDaysCoefficient-REMBO;
            setAx(REMBO, Direction.RIGHT);
            setEx(REMBO, Direction.LEFT);

            setCx(setDaysCoefficient,Direction.LEFT);
            setDx(setDaysCoefficient,Direction.RIGHT);
        }else if (stageOfYear == THIRD){

            setDaysCoefficient = setDaysCoefficient-REMBO2;
            setDaysCoefficient = REMBO - setDaysCoefficient;
            setAx(REMBO,Direction.RIGHT);
            setEx(REMBO, Direction.LEFT);

            setCx(setDaysCoefficient,Direction.LEFT);
            setDx(setDaysCoefficient,Direction.RIGHT);

        }else if (stageOfYear == FOURTH){
            setDaysCoefficient = (setDaysCoefficient-REMBO3);
            setDaysCoefficient = REMBO - setDaysCoefficient;

            setAx(setDaysCoefficient,Direction.RIGHT);
            setEx(setDaysCoefficient, Direction.LEFT);
        }


        //setNewCoordinatesForSecondCurve();
        System.out.println("Result of Season: "+" stage of year= "+stageOfYear);
    }


    public static void setAx(float acceleration, Direction direction) {


        if (direction == Direction.RIGHT){
            Ax =     Ax+acceleration;
            Ainv = Ainv+acceleration;
        }else if (direction == Direction.LEFT){
            Ax =   Ax  -acceleration;
            Ainv = Ainv-acceleration;
        }else if(direction == Direction.STOP){
            Ax = Ax;
            Ainv = Ainv;
        }


        //respect to Bx
        System.out.println("acc = "+ acceleration);
        float coeff = Math.abs(Math.abs(gravityPoint)-Math.abs(Ax));
        if (coeff > 50 & coeff < 250){

        }
    }



    public static void setCx(float acceleration, Direction direction) {
        //Cx = cx;


        if (direction == Direction.RIGHT){
            Cx =   Cx+acceleration;
            Cinv = Cinv+acceleration;
        }else if (direction == Direction.LEFT){
            Cx = Cx-acceleration;
            Cinv = Cinv-acceleration;
        }else if (direction == Direction.STOP){
            Cx = Cx;
            Cinv = Cinv;
        }

    }

    public static void setDx(float acceleration, Direction direction) {
        //Dx = dx;


        if (direction == Direction.RIGHT){
            Dx = Dx+acceleration;
            Dinv = Dinv+acceleration;
        }else if (direction == Direction.LEFT){
            Dx =   Dx   - acceleration;
            Dinv = Dinv - acceleration;

        }else if (direction == Direction.STOP){
            Dx = Dx;
            Dinv = Dinv;
        }
    }

    public static void setEx(float acceleration, Direction direction2) {


        if (direction2 == Direction.RIGHT){
            Ex = Ex+acceleration;
            Einv = Einv+acceleration;
        }else if (direction2 == Direction.LEFT){
            Ex = Ex-acceleration;
            Einv = Einv-acceleration;
        }else if (direction2 == Direction.STOP){
            Ex = Ex;
            Einv = Einv;
        }

    }

    public static void setBx() {
        Bx = Bx+minutes;
    }

    public static void setFx() {
        Fx = Fx+minutes;
    }
}
