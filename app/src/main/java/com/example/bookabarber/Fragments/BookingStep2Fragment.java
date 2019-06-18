package com.example.bookabarber.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookabarber.Adapter.MySitterAdapter;
import com.example.bookabarber.Common.Common;
import com.example.bookabarber.Common.SpacesItemDecoration;
import com.example.bookabarber.Model.Sitter;
import com.example.bookabarber.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep2Fragment extends Fragment {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_sitter)
    RecyclerView recycler_sitter;

    private BroadcastReceiver sitterDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Sitter> sitterArrayList = intent.getParcelableArrayListExtra(Common.KEY_SITTER_LOAD_DONE);
            //Create adapter late
            MySitterAdapter adapter = new MySitterAdapter(getContext(),sitterArrayList);
            recycler_sitter.setAdapter(adapter);

        }
    };

    static BookingStep2Fragment instance;

    public static BookingStep2Fragment getInstance() {
        if(instance ==null)
            instance =new BookingStep2Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager =LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(sitterDoneReceiver, new IntentFilter(Common.KEY_SITTER_LOAD_DONE));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(sitterDoneReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_two, container,false);

        unbinder = ButterKnife.bind(this,itemView);
        
        initView();

        return itemView;
    }

    private void initView() {
        recycler_sitter.setHasFixedSize(true);
        recycler_sitter.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_sitter.addItemDecoration(new SpacesItemDecoration(4));
    }
}
