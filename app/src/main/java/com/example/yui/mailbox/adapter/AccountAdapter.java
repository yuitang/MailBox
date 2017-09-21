package com.example.yui.mailbox.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.yui.mailbox.R;
import com.example.yui.mailbox.bean.UserBean;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder>  {
    private Context mContext;
    private List<UserBean> accountList;

    public AccountAdapter(List list){
        this.accountList = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;

        public ViewHolder(View view){
            super(view);

            mTextView = (TextView) view.findViewById(R.id.account_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_account_list,
                parent, false);
        return new AccountAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AccountAdapter.ViewHolder holder, int position) {
        String accountStr = accountList.get(position).getMailAddress();
        holder.mTextView.setText(accountStr);
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }
}
