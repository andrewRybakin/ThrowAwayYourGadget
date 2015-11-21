package ru.dzen.besraznitsy;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.HashMap;

public class SupervisorController {
    private static SupervisorController ourInstance = new SupervisorController();
    private float score;

    public static SupervisorController getInstance() {
        return ourInstance;
    }

    private HashMap<String, Long> timelineHasMap;

    private SupervisorController() {
        timelineHasMap=new HashMap<>();
    }

    public float getScore(){
        /**Тут кароч ита, формула кароч, Карл*/
        return score;
    }

    public void onDeviceActivated(long time, Context c){
        Log.d("ПриветКрл1", "Девайс активирован");
        timelineHasMap.put(MainActivityFragment.DEVICE_ACTIVATED, time);
        LocalBroadcastManager.getInstance(c).sendBroadcast(
                (new Intent()).setAction(MainActivityFragment.DEVICE_ACTIVATED).putExtra(MainActivityFragment.EXTRA_TIME, time).setType("text/*")
        );
    }

    public void onDeviceDeActivated(long time, Context c){
        Log.d("ПриветКрл1", "Девайс ДЕактивирован");
        timelineHasMap.put(MainActivityFragment.DEVICE_DEACTIVATED, time);
        LocalBroadcastManager.getInstance(c).sendBroadcast(
                (new Intent()).setAction(MainActivityFragment.DEVICE_DEACTIVATED).putExtra(MainActivityFragment.EXTRA_TIME, time).setType("text/*")
        );
    }

    public void onGameStart(long time, Context c){
        Log.d("ПриветКрл1", "Игра запущена");
        timelineHasMap.put(MainActivityFragment.GAME_STARTED, time);
        LocalBroadcastManager.getInstance(c).sendBroadcast(
                (new Intent()).setAction(MainActivityFragment.GAME_STARTED).putExtra(MainActivityFragment.EXTRA_TIME, time).setType("text/*")
        );
    }

    public void onGameEnd(long time, Context c){
        Log.d("ПриветКрл1", "Игра остановлена");
        timelineHasMap.put(MainActivityFragment.GAME_STOPPED, time);
        LocalBroadcastManager.getInstance(c).sendBroadcast(
                (new Intent()).setAction(MainActivityFragment.GAME_STOPPED).putExtra(MainActivityFragment.EXTRA_TIME,time)
        );
    }
}
