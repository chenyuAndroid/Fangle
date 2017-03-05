package com.chenyu.fangle.view.interfaces;


import com.chenyu.fangle.beans.ZhiHuDetail;

public interface IDetailView {
    void showLoading();
    void hideLoading();
    void showDetail(ZhiHuDetail bean);
}
