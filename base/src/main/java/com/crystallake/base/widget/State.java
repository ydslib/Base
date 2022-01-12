package com.crystallake.base.widget;

/**
 * author：    zp
 * date：      2015/10/6 & 11:37
 * version     1.0
 * description:
 * modify by
 */
public interface State {
    /**
     * 加载中
     */
    int LOADING = 1;

    /**
     * 加载成功
     */
    int SUCCESS = 2;

    /**
     * 加载失败
     */
    int FAILED = 3;

    /**
     * 加载内容为空
     */
    int EMPTY = 4;

    /**
     * 无网络
     */
    int NO_NETWORK = 5;


}
