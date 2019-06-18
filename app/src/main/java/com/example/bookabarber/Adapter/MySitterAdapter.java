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
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.bookabarber.Common.Common;
import com.example.bookabarber.Interface.IRecyclerItemSelectedListener;
import com.example.bookabarber.Model.Sitter;
import com.example.bookabarber.R;

import java.util.ArrayList;
import java.util.List;

public class MySitterAdapter extends RecyclerView.Adapter<MySitterAdapter.MyViewHolder> {

    Context context;
    List<Sitter> sitterList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MySitterAdapter(Context context, List<Sitter>sitterList){
        this.context =  context;
        this.sitterList = sitterList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) { View itemView = LayoutInflater.from(context)
            .inflate(R.layout.layout_sitter,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.txt_sitter_name.setText(sitterList.get(i).getName());
        myViewHolder.ratingBar.setRating((float)sitterList.get(i).getRating());
        if(!cardViewList.contains(myViewHolder.card_sitter))
            cardViewList.add(myViewHolder.card_sitter);

        myViewHolder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //Set background for all item not choice
                for (CardView cardView: cardViewList)

                    cardView.setCardBackgroundColor(context.getResources()
                    .getColor(android.R.color.white)

                    );
                //Send Local broadcast to enable button next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_SITTER_SELECTED,sitterList.get(pos));
                intent.putExtra(Common.KEY_STEP,2);
                localBroadcastManager.sendBroadcast(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sitterList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_sitter_name;
        RatingBar ratingBar;
        CardView card_sitter;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView){
            super (itemView);

            card_sitter = (CardView)itemView.findViewById(R.id.card_sitter);
            txt_sitter_name = (TextView)itemView.findViewById(R.id.txt_sitter_name);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rtb_sitter);

            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view){
            iRecyclerItemSelectedListener.onItemSelectedListener(view,getAdapterPosition());
        }


    }
}
