package com.chenyu.fangle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chenyu.fangle.R;
import com.chenyu.fangle.beans.ZhiHuArticleBean;
import com.chenyu.fangle.beans.ZhiHuStory;
import com.chenyu.fangle.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2016/10/6.
 */
public class ZhiHuArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ZhiHuStory> mDataSet;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private static final int NORMAL_TYPE = 1;
    private static final int TITLED_TYPE = 2;

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public ZhiHuArticleAdapter(Context context,List<ZhiHuStory> data){
        mContext = context;
        mDataSet = data;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == NORMAL_TYPE){
            return new NormalViewHolder(LayoutInflater.from(mContext).
                    inflate(R.layout.zhihu_item_notitle,parent,false));
        }else{
            return new TitledViewHolder(LayoutInflater.from(mContext).
                    inflate(R.layout.zhihu_item_with_title, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof TitledViewHolder){
            bindData((TitledViewHolder)holder,position);
        }else{
            bindData((NormalViewHolder)holder,position);
        }

        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(v,holder.getAdapterPosition() - 1);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return TITLED_TYPE;

        String date = mDataSet.get(position).date;
        String datePrevious = mDataSet.get(position - 1).date;
        boolean isDifferent = !date.equals(datePrevious);
        return isDifferent ? TITLED_TYPE : NORMAL_TYPE;


    }

    private void bindData(NormalViewHolder holder,int position){
        holder.mArticleTitle.setText(mDataSet.get(position).title);
        Picasso.with(mContext)
                .load(mDataSet.get(position).images.get(0))
                .fit()
                .into(holder.mArticleIcon);
    }

    private void bindData(TitledViewHolder holder,int position){
        bindData((NormalViewHolder) holder, position);
        holder.mArticleDate.setText(DateUtils.getDateHeader(mDataSet.get(position).date));
    }

    class NormalViewHolder extends RecyclerView.ViewHolder {

        TextView mArticleTitle;
        ImageView mArticleIcon;

        public NormalViewHolder(View itemView) {
            super(itemView);
            mArticleTitle = (TextView) itemView.findViewById(R.id.zhihu_article_title);
            mArticleIcon = (ImageView) itemView.findViewById(R.id.zhihu_article_icon);
        }
    }

    class TitledViewHolder extends NormalViewHolder{

        TextView mArticleDate;

        public TitledViewHolder(View itemView) {
            super(itemView);
            mArticleDate = (TextView) itemView.findViewById(R.id.zhihu_article_date);
        }
    }
}
