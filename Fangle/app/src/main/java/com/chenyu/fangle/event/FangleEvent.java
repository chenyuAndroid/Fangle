package com.chenyu.fangle.event;

/**
 * Created by Administrator on 2017/3/1.
 */

public class FangleEvent {

    private int refreshEvent;
    public static final int REFRESH_ARTICLE_EVENT = 1000;
    public static final int REFRESH_ARTICLE_DONE_EVENT = 1001;

    public FangleEvent(int event){
        refreshEvent = event;
    }

    public int getEvent(){
        return refreshEvent;
    }
}
