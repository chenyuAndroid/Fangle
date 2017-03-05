package com.chenyu.fangle.presenter;


import com.chenyu.fangle.beans.ZhiHuDetail;
import com.chenyu.fangle.model.ZhiHuDetailModel;
import com.chenyu.fangle.view.interfaces.IDetailView;

public class ZhiHuDetailPresenter {

    private IDetailView mDetailView;
    private ZhiHuDetailModel mDetailModel;

    public ZhiHuDetailPresenter(IDetailView view){
        mDetailView = view;
        mDetailModel = new ZhiHuDetailModel(this);
    }

    public void obtainDetails(int id){
        mDetailView.showLoading();
        ZhiHuDetail bean = mDetailModel.loadDataFromDB(id);
        if (bean != null){
            mDetailView.hideLoading();
            mDetailView.showDetail(bean);
        }else
            mDetailModel.loadDataFromNet(id);
    }

    public void obtainDetailsFinished(ZhiHuDetail bean){
        mDetailView.hideLoading();
        mDetailView.showDetail(bean);
        mDetailModel.saveData(bean);
    }

    public boolean favorThisActicle(int id){
        return mDetailModel.favorSpecificArticle(id);
    }

    public boolean dislikeThisArticle(int id){
        return mDetailModel.dislikeSpecificArticle(id);
    }

}
