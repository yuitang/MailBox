package com.example.yui.mailbox.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yui.mailbox.R;
import com.example.yui.mailbox.activity.MailDetailActivity;
import com.example.yui.mailbox.bean.MailBean;

import java.util.List;


public class MailListAdapter extends RecyclerView.Adapter<MailListAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout mRelativeLayout;
        TextView sender;
        TextView title;
        TextView content;
        ImageView point;
        TextView receivedData;
        ImageView hasFile;

        public ViewHolder(View view){
            super(view);

            mRelativeLayout = (RelativeLayout) view;
            sender = (TextView)view.findViewById(R.id.sender);
            title = (TextView)view.findViewById(R.id.title);
            content = (TextView)view.findViewById(R.id.content);
            point = (ImageView)view.findViewById(R.id.point);
            receivedData = (TextView)view.findViewById(R.id.received_data);
            hasFile = (ImageView)view.findViewById(R.id.has_file);
        }
    }

    private List<MailBean> mData ;
    private Context mContext;

    public MailListAdapter(List<MailBean> mData){
        this.mData = mData ;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mail_list, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();

                Intent intent = new Intent(mContext, MailDetailActivity.class);
                intent.putExtra("messageId", mData.get(position).getMessageId());

                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MailBean mail = mData.get(position);

        if (mail.getSender().contains("13971024963@163.com")) {
            holder.sender.setText("我");
        } else {
            holder.sender.setText(mail.getName());
        }

        if (mail.getNew() == 1){
            holder.point.setVisibility(View.VISIBLE);
        } else {
            holder.point.setVisibility(View.GONE);
        }

        if (mail.getHasFile() == 1){
            holder.hasFile.setVisibility(View.VISIBLE);
        }else{
            holder.hasFile.setVisibility(View.GONE);
        }

        holder.receivedData.setText(mail.getReceivedData());
        holder.title.setText(mail.getSubject());
        holder.content.setText("发自" + mail.getSentDate());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
