package com.example.yui.mailbox.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.yui.mailbox.R;
import com.example.yui.mailbox.Application.MailApplication;
import com.example.yui.mailbox.adapter.MailListAdapter;
import com.example.yui.mailbox.bean.MailBean;
import com.example.yui.mailbox.dao.DBUtils;
import com.example.yui.mailbox.utils.MailReceiverUtils;

import java.util.ArrayList;
import java.util.List;


public class MailListFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private RecyclerView mRecyclerView;
    private List<MailBean> mData;
    private MailListAdapter adapter;

    private SwipeRefreshLayout swipeRefresh;

    private MailApplication mMailApplication;

    private RelativeLayout progress;

    public static MailListFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);

        MailListFragment fragment = new MailListFragment();
        // 传递参数
        fragment.setArguments(args);

        return fragment;
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Toast.makeText(getContext(), "刷新数据错误！", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    swipeRefresh.setRefreshing(false);
                    break;
                case 3:
                    Toast.makeText(getContext(), "刷新完成！", Toast.LENGTH_SHORT).show();
                case 4:
                    mData.clear();
                    mData.addAll(DBUtils.queryMailList());
                    adapter.notifyDataSetChanged();
                case 5:
                    progress.setVisibility(View.GONE);
                    swipeRefresh.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(mRunnable).start();

        // 获取参数
        mPage = getArguments().getInt(ARG_PAGE);
        mData = new ArrayList<>();
        mMailApplication = (MailApplication)getActivity().getApplication();
    }


    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Message message = new Message();
            if (DBUtils.getUserLastPosition() == 0){
                try {
                    int position = MailReceiverUtils.getLastPositionFromServer();
                    MailReceiverUtils.receiveMail("inbox", position - 20, position);
                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    message.what = 4;
                }
            } else {
                message.what = 5;
            }
            mHandler.sendMessage(message);
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        mData.clear();
        mData.addAll(DBUtils.queryMailList());
        adapter.notifyDataSetChanged();

        if (!mData.isEmpty()){
            progress.setVisibility(View.GONE);
            swipeRefresh.setVisibility(View.VISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mail_list, container, false);

        progress = (RelativeLayout)view.findViewById(R.id.tips);

        swipeRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swip_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int positionFromUser = DBUtils.getUserLastPosition();
                            int positionFromServer = MailReceiverUtils.getLastPositionFromServer();

                            Message message = new Message();

                            if (positionFromServer == positionFromUser){
                                message.what = 3;
                            } else if (positionFromServer > positionFromUser){
                                MailReceiverUtils.receiveMail("inbox", positionFromServer,
                                        positionFromUser + 1);
                                List<MailBean> list = DBUtils.queryMailList();

                                if (!list.isEmpty()){
                                    message.what = 3;
                                }else{
                                    message.what = 1;
                                }
                            }
                            mHandler.sendMessage(message);
                        } catch(Exception e){
                            e.printStackTrace();
                        } finally {
                            Message message = new Message();
                            message.what = 2;
                            mHandler.sendMessage(message);
                        }
                    }
                }).start();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView = (RecyclerView) view.findViewById(R.id.mail_list);
        mRecyclerView.setLayoutManager(layoutManager);

        List<MailBean> temp = DBUtils.queryMailList();

        if (mMailApplication.getmMailBeanList() != null){
            mData.addAll(temp) ;
        }
        adapter = new MailListAdapter(mData);

        mRecyclerView.setAdapter(adapter);

        return view;
    }
}
