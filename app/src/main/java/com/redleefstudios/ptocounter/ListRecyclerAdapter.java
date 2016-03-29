package com.redleefstudios.ptocounter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Fred on 3/29/2016.
 */
public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ViewHolder> {

    //Interface for Fragment to Implement
    public interface OnItemClickListener
    {
        public void onItemClicked(int position);
    }
    public interface OnItemLongClickListener
    {
        public boolean onItemLongClicked(int position);
    }

    private ArrayList<PTOItem> itemData;

    private MainActivity mActivity;

    public ListRecyclerAdapter(ArrayList<PTOItem> itemData, MainActivity mActivity)
    {
        this.itemData = itemData;
        this.mActivity = mActivity;
    }

    @Override
    public ListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_list_item, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder,final int position)
    {
        viewHolder.mLargeText.setText(itemData.get(position).GetEvent());
        viewHolder.mSmallText.setText(itemData.get(position).GetType().name());
        viewHolder.mImage.setText(itemData.get(position).GetDays() + "");

        viewHolder.itemLayoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.onItemClicked(position);
            }
        });

        viewHolder.itemLayoutView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                mActivity.onItemLongClicked(position);
                return true;
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView mLargeText;
        public TextView mSmallText;
        public TextView mImage;
        public View itemLayoutView;

        public ViewHolder(View itemLayoutView)
        {
            super(itemLayoutView);
            this.itemLayoutView = itemLayoutView;
            mLargeText = (TextView)itemLayoutView.findViewById(R.id.cardTextLarge);
            mSmallText = (TextView)itemLayoutView.findViewById(R.id.cardTextSmall);
            mImage = (TextView)itemLayoutView.findViewById(R.id.small_card_image);
        }
    }

    @Override
    public int getItemCount()
    {
        return itemData.size();
    }
}