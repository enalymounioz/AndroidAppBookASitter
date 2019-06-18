package com.example.bookabarber;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;

import com.example.bookabarber.Adapter.MyViewPagerAdapter;
import com.example.bookabarber.Common.Common;
import com.example.bookabarber.Model.Sitter;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BookingActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;
    AlertDialog dialog;
    CollectionReference sitterRef;

    @BindView(R.id.step_view)
    StepView stepView;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;
    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    //Event
    @OnClick(R.id.btn_previous_step)
    void previousStep(){
        if (Common.step ==3 || Common.step>0);
        {
            Common.step--;
            viewPager.setCurrentItem(Common.step);
        }
    }

    @OnClick(R.id.btn_next_step)
    void nextClick(){
        if (Common.step <3 || Common.step == 0)
        {
            Common.step++;//increase
            if (Common.step==1)//After choose sitter
            {
                if(Common.currentStore!=null)
                    loadSitterByStore(Common.currentStore.getStoreid());
            }
            else if (Common.step==2)//Pick time Slot
            {
                if (Common.currentSitter != null)
                    loadTimeSlotOfSitter(Common.currentSitter.getSitterId());
            }
            viewPager.setCurrentItem(Common.step);
        }

    }

    private void loadTimeSlotOfSitter(String sitterId) {
        //Send Loacal Broadcast to Fragment step 3
        Intent intent = new Intent(Common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadSitterByStore(String storeId) {
        dialog.show();

        //Now, select all sitters of Local
        ///AllSitters/Kallithea/Branch/KMwZXoApKH48BXBDVdFl/Sitter
        if (!TextUtils.isEmpty(Common.city))
        {
            sitterRef = FirebaseFirestore.getInstance()
                    .collection("AllSitters")
                    .document(Common.city)
                    .collection("Branch")
                    .document(storeId)
                    .collection("Sitter");

            sitterRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<Sitter> sitters = new ArrayList<>();
                            for (QueryDocumentSnapshot sitterSnapShot:task.getResult())
                            {
                                Sitter sitter = sitterSnapShot.toObject(Sitter.class);
                                sitter.setPassword("");//Remove password because in client app
                                sitter.setSitterId(sitterSnapShot.getId());//Get id of sitter

                                sitters.add(sitter);
                            }
                            //Send broadcast to BookingStep2Fragment to load Recycler
                            Intent intent = new Intent(Common.KEY_SITTER_LOAD_DONE);
                            intent.putParcelableArrayListExtra(Common.KEY_SITTER_LOAD_DONE,sitters);
                            localBroadcastManager.sendBroadcast(intent);

                            dialog.dismiss();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();

                        }
                    });
        }

    }

    //Broadcast Receiver
    private BroadcastReceiver buttonNextReceiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int step = intent.getIntExtra(Common.KEY_STEP,0);
            if (step==1)
                Common.currentStore = intent.getParcelableExtra(Common.KEY_LOCAL_STORE);
            else if (step == 2)
                Common.currentSitter = intent.getParcelableExtra(Common.KEY_SITTER_SELECTED);

            Common.currentStore = intent.getParcelableExtra(Common.KEY_LOCAL_STORE);
            btn_next_step.setEnabled(true);
            setColorButton();
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(BookingActivity.this);

        dialog = new SpotsDialog.Builder().setContext(this).build();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver,new IntentFilter(Common.KEY_ENABLE_BUTTON_NEXT));

        setupStepView();
        setColorButton();

        //View
        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);//We have 4 fragments so we need to keep the state of his 4 screens page
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                //Show step
                stepView.go(i,true);
                if (i==0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

                //Set disable button next
                btn_next_step.setEnabled(false);
                setColorButton();

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setColorButton() {
        if (btn_next_step.isEnabled())
        {
            btn_next_step.setBackgroundResource(R.color.colorButton);
        }
        else {
            btn_next_step.setBackgroundResource(android.R.color.darker_gray);
        }
        if (btn_previous_step.isEnabled())
        {
            btn_previous_step.setBackgroundResource(R.color.colorButton);
        }
        else
        {
            btn_previous_step.setBackgroundResource(android.R.color.darker_gray);
        }

    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Store");
        stepList.add("Sitter");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);


    }
}


