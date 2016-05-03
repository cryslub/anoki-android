package com.anoki1.common;

/**
 * Created by Administrator on 2015-07-10.
 */
public abstract class CallBack<L> implements Task{
    public <T> T execute(){return null;}

    public abstract  void success(L result);
}
