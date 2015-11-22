package ru.dzen.besraznitsy;

public class StaticMathemetics {

    private static final float coeffFall=0.5f;
    private static final float coeffCall=0.5f;

    public static float countScore(float curScore, long activeTime, long unactiveTime){
        if(activeTime-unactiveTime>0)
            return curScore+(activeTime-unactiveTime)/2;
        else
            return curScore-(unactiveTime-activeTime)/2;
    }

}
