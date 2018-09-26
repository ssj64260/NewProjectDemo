package com.android.newprojectdemo.a_test.ui;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.newprojectdemo.R;
import com.android.newprojectdemo.ui.adapter.IOnListClickListener;

import java.util.List;

public class ButtonListAdapter extends RecyclerView.Adapter {

    private List<String> mDatas;
    private LayoutInflater mInflater;
    private IOnListClickListener mListClick;

    public ButtonListAdapter(Context context, List<String> datas) {
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(mInflater.inflate(R.layout.test_item_button_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        bindItem((BaseViewHolder) holder, position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    private void bindItem(BaseViewHolder holder, final int position) {
        final String data = mDatas.get(position);

        holder.mButton.setText(data);
        holder.mButton.setTag(position);
        holder.mButton.setOnClickListener(mClick);
    }

    private class BaseViewHolder extends RecyclerView.ViewHolder {

        private Button mButton;

        private BaseViewHolder(View itemView) {
            super(itemView);
            mButton = (Button) itemView;
        }
    }

    public void setListClick(IOnListClickListener listClick) {
        this.mListClick = listClick;
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListClick != null) {
                final int position = (int) v.getTag();
                mListClick.onItemClick(position);
            }
        }
    };

}
