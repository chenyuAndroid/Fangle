package com.chenyu.fangle.widgets.superrecyclerview;

import android.content.Context;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.chenyu.fangle.R;
import com.chenyu.fangle.widgets.superrecyclerview.interfaces.OnLoadMoreListener;
import com.chenyu.fangle.widgets.superrecyclerview.WrapperRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class EnhancedRecyclerView extends RecyclerView {

    private WrapperRecyclerAdapter mAdapter;
    private RecyclerView.Adapter mRealAdapter;
    private OnLoadMoreListener mOnLoadMoreListener;
    private Context mContext;
    private List<FixedViewInfo> mHeaderViewInfos = new ArrayList<>();
    private List<FixedViewInfo> mFooterViewInfos = new ArrayList<>();


    public EnhancedRecyclerView(Context context) {
        this(context,null);
    }

    public EnhancedRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EnhancedRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public static class FixedViewInfo{
        public View view;
//        public Object data;
    }

    private void init(){
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(mOnLoadMoreListener != null){
                    LayoutManager layoutManager = getLayoutManager();
                    int lastVisibleItemPosition = 0;

                    if(layoutManager instanceof LinearLayoutManager){
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }else if(layoutManager instanceof GridLayoutManager){
                        lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }else{
                        int[] into = new int[((StaggeredGridLayoutManager)layoutManager).getSpanCount()];
                        ((StaggeredGridLayoutManager)layoutManager).findLastVisibleItemPositions(into);
                        lastVisibleItemPosition = findMaxPosition(into);
                        Log.d("cylog","into 1:"+into[0]+",into 2:"+into[1]);
                    }

                    if(lastVisibleItemPosition >= layoutManager.getItemCount() - 1 &&
                            !mAdapter.getIsLoadingFlag()){
                        Log.d("cylog", "加载更多数据ing");
                        mAdapter.setLoading();
                        mOnLoadMoreListener.onLoad();
                    }

                }
            }
        });
    }

    public int findMaxPosition(int[] into){
        int size = into.length;
        int maxPosition = 0;
        for(int i = 0; i < size; i++){
            if(maxPosition < into[i])
                maxPosition = into[i];
        }
        return maxPosition;
    }


    public void addHeaderView(View view){
        final FixedViewInfo info = new FixedViewInfo();
        info.view = view;
        mHeaderViewInfos.add(info);
    }

    public void addFooterView(View view){
        final FixedViewInfo info = new FixedViewInfo();
        info.view = view;
        mFooterViewInfos.add(info);
    }

    public void addOnLoadMoreListener(OnLoadMoreListener listener){
        this.mOnLoadMoreListener = listener;
    }

    public void setLoaded(){
        mAdapter.setLoaded();
    }

    public RecyclerView.Adapter getRealAdapter(){
        return this.mRealAdapter;
    }

    private final RecyclerView.AdapterDataObserver mDataSetObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mAdapter.notifyItemRangeChanged(positionStart,itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mAdapter.notifyItemRangeChanged(positionStart,itemCount,payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mAdapter.notifyItemRangeInserted(positionStart + mHeaderViewInfos.size(), itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };

    @Override
    public void setAdapter(Adapter adapter) {
        mRealAdapter = adapter;
        View view = LayoutInflater.from(mContext).inflate(R.layout.footer_progressbar,this,false);
        if(mHeaderViewInfos.size() > 0 || mFooterViewInfos.size() > 0){
            mAdapter = new WrapperRecyclerAdapter(adapter,mHeaderViewInfos,mFooterViewInfos,view);
            super.setAdapter(mAdapter);
        }else {
            super.setAdapter(mRealAdapter);
        }

        if(mAdapter != null){
            mRealAdapter.registerAdapterDataObserver(mDataSetObserver);
        }

    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.d("cylog","RecyclerView onSaveInstanceState");
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        Log.d("cylog","RecyclerView onRestoreInstanceState");
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container) {
        super.dispatchSaveInstanceState(container);
    }
}
