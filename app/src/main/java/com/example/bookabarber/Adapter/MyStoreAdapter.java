package com.example.bookabarber.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.bookabarber.Common.Common;
import com.example.bookabarber.Interface.IRecyclerItemSelectedListener;
import com.example.bookabarber.Model.Store;
import com.example.bookabarber.R;

import java.util.ArrayList;
import java.util.List;

public class MyStoreAdapter extends RecyclerView.Adapter<MyStoreAdapter.MyViewHolder>{

    Context context;
    List<Store> storeList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyStoreAdapter(Context context, List<Store> storeList) {
        this.context = context;
        this.storeList = storeList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_store,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.txt_store_name.setText(storeList.get(i).getName());
        myViewHolder.txt_store_address.setText(storeList.get(i).getAddress());

        if (!cardViewList.contains(myViewHolder.card_store))
            cardViewList.add(myViewHolder.card_store);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Set white background for all not selected card

                for (CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                //Set selected BG for only selected item
                myViewHolder.card_store.setCardBackgroundColor(context.getResources()
                        .getColor(android.R.color.holo_orange_dark));

                //Send Broadcast to tell Booking Activity enable Button next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_LOCAL_STORE,storeList.get(pos));
                intent.putExtra(Common.KEY_STEP,1);
                localBroadcastManager.sendBroadcast(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_store_name, txt_store_address;
        CardView card_store;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_store = (CardView) itemView.findViewById(R.id.card_store);
            txt_store_address= (TextView) itemView.findViewById(R.id.txt_store_address);
            txt_store_name= (TextView) itemView.findViewById(R.id.txt_store_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v
                    ,getAdapterPosition());
        }
    }
}