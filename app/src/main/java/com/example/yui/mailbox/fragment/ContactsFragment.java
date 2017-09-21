package com.example.yui.mailbox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.yui.mailbox.R;
import com.example.yui.mailbox.view.ContactListView;


public class ContactsFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private Intent intent;
    private int mPage;

    public static ContactsFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);

        ContactsFragment fragment = new ContactsFragment();
        // 传递参数
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取参数
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ContactListView listView = (ContactListView) view.findViewById(R.id.contact_list_view);

        return view;
    }
}
