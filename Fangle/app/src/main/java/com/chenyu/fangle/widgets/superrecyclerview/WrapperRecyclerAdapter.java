package com.chenyu.fangle.widgets.superrecyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chenyu.fangle.R;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
public class WrapperRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final RecyclerView.Adapter mRealAdapter;
    private View mProgressbar;
    private boolean isLoadingFlag;
    private List<EnhancedRecyclerView.FixedViewInfo> mHeaderViewInfos;
    private List<EnhancedRecyclerView.FixedViewInfo> mFooterViewInfos;

    private static final int HEADER_TYPE = 0x1001;
    private static final int FOOTER_TYPE = 0x1002;
    private static final int PROGRESSBAR_TYPE = 0x1003;

    public WrapperRecyclerAdapter(RecyclerView.Adapter adapter,
                                  List<EnhancedRecyclerView.FixedViewInfo> headerViewInfos,
                                  List<EnhancedRecyclerView.FixedViewInfo> footerViewInfos,
                                  View progressbar) {
        this.mRealAdapter = adapter;
        this.mHeaderViewInfos = headerViewInfos;
        this.mFooterViewInfos = footerViewInfos;
        this.mProgressbar = progressbar;
        isLoadingFlag = false;
    }


    public int getHeadersCount(){
        return mHeaderViewInfos.size();
    }

    public int getFootersCount(){
        return mFooterViewInfos.size();
    }

    public boolean getIsLoadingFlag(){
        return this.isLoadingFlag;
    }

    public void setLoading(){
        setIsLoadingFlag(true);
    }

    public void setLoaded(){
        setIsLoadingFlag(false);
    }

    private void setIsLoadingFlag(boolean isLoading){
        this.isLoadingFlag = isLoading;
        notifyDataSetChanged();
    }

    public boolean isHeaderPosition(int position){
        return position < mHeaderViewInfos.size();
    }

    public boolean isFooterPosition(int position){
        if(position >= getHeadersCount() + mRealAdapter.getItemCount()
                && position < getHeadersCount() + mRealAdapter.getItemCount() + getFootersCount())
            return true;
        else
            return false;
    }


    public boolean isProgressbarPosition(int position){
        if(position >= getHeadersCount() + mRealAdapter.getItemCount() + getFootersCount()
                && isLoadingFlag)
            return true;
        else
            return false;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.d("cylog","onAttachedToRecyclerView");
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if(manager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean shouldFullSpan = isHeaderPosition(position) ||
                            isFooterPosition(position) || isProgressbarPosition(position);
                    return shouldFullSpan ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Log.d("cylog","onViewAttachedToWindow");
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if(lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams){
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            int position = holder.getLayoutPosition();
//            Log.d("cylog","position"+position);
            boolean shouldFullSpan = isHeaderPosition(position) || isFooterPosition(position)
                    || isProgressbarPosition(position);
            p.setFullSpan(shouldFullSpan);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == HEADER_TYPE){
            return new HeaderViewHolder(mHeaderViewInfos.get(0).view);
        }else if(viewType == FOOTER_TYPE){
            return new HeaderViewHolder(mFooterViewInfos.get(0).view);
        }else if(viewType == PROGRESSBAR_TYPE){
            return new FooterProgressbarViewHolder(mProgressbar);
        }else
            return mRealAdapter.onCreateViewHolder(parent,viewType);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int headersCount = getHeadersCount();
        if(position < headersCount)
            return;
        if(holder instanceof FooterProgressbarViewHolder){
            ((FooterProgressbarViewHolder) holder).pb.setIndeterminate(true);
        }

        int contentPosition = position - headersCount;
//        Log.d("cylog","position:"+position+",Class:"+holder.getClass());
        if(mRealAdapter != null){
            if(contentPosition < mRealAdapter.getItemCount()){
                mRealAdapter.onBindViewHolder(holder,contentPosition);
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        if(!isLoadingFlag) {
            return mRealAdapter.getItemCount() + getHeadersCount() + getFootersCount();
        }else {
            return mRealAdapter.getItemCount() + getHeadersCount() + getFootersCount() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position < getHeadersCount()){
//            Log.d("cylog","position:"+position+",type:"+"header");
            return HEADER_TYPE;
        }

        if(position >= getHeadersCount() + mRealAdapter.getItemCount()
                && position < getHeadersCount() + mRealAdapter.getItemCount() + getFootersCount()){
//            Log.d("cylog","position:"+position+",type:"+"footer");
            return FOOTER_TYPE;
        }

        if(position >= getHeadersCount() + mRealAdapter.getItemCount() + getFootersCount()
                && isLoadingFlag){
            return PROGRESSBAR_TYPE;
        }
//        Log.d("cylog","position:"+position+",type:"+mRealAdapter.getItemViewType(position - getHeadersCount()));
        return mRealAdapter.getItemViewType(position - getHeadersCount());
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    class FooterProgressbarViewHolder extends RecyclerView.ViewHolder{
        private ProgressBar pb;
        public FooterProgressbarViewHolder(View itemView) {
            super(itemView);
            pb = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }
    }

}
