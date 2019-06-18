package com.example.bookabarber.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;import android.widget.Toast;
import com.example.bookabarber.Adapter.MyStoreAdapter;
import com.example.bookabarber.Common.Common;
import com.example.bookabarber.Common.SpacesItemDecoration;
import com.example.bookabarber.Interface.IAllStoreLoadListener;
import com.example.bookabarber.Interface.IBranchLoadListener;
import com.example.bookabarber.Model.Store;
import com.example.bookabarber.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep1Fragment extends Fragment implements IAllStoreLoadListener, IBranchLoadListener {


    //Variable
    CollectionReference allStoresRef;
    CollectionReference branchRef;

    IAllStoreLoadListener iAllStoreLoadListener;
    IBranchLoadListener iBranchLoadListener;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.recycler_store)
    RecyclerView recycler_store;

    Unbinder unbinder;
    AlertDialog dialog;

    static BookingStep1Fragment instance;

    public static BookingStep1Fragment getInstance() {
        if(instance ==null)
            instance =new BookingStep1Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allStoresRef = FirebaseFirestore.getInstance().collection("AllSitters");
        iAllStoreLoadListener =this;
        iBranchLoadListener = this;

        dialog = new SpotsDialog.Builder().setContext(getActivity()).build();


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

         View itemView =  inflater.inflate(R.layout.fragment_booking_step_one,container,false);
         unbinder = ButterKnife.bind(this,itemView);
         
         initView();
         loadAllStore();
         

         return itemView;

    }

    private void initView() {
        recycler_store.setHasFixedSize(true);
        recycler_store.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_store.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void loadAllStore() {
        allStoresRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            List<String> list = new ArrayList<>();
                            list.add("Please choose city");
                            for (QueryDocumentSnapshot documentSnapshot:task.getResult())
                                list.add(documentSnapshot.getId());
                            iAllStoreLoadListener.onAllStoreLoadSuccess(list);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllStoreLoadListener.onAllStoreLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllStoreLoadSuccess(List<String> areaNameList) {
        spinner.setItems(areaNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(position>0)
                {
                    loadBranchOfCity(item.toString());
                }
                else
                    recycler_store.setVisibility(View.GONE);
            }
        });

    }

    private void loadBranchOfCity(String cityName) {
        dialog.show();

        Common.city = cityName;

        branchRef = FirebaseFirestore.getInstance()
                .collection("AllSitters")
                .document(cityName)
                .collection("Branch");

        branchRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Store> list = new ArrayList<>();
                if(task.isSuccessful())
                {
                    for (QueryDocumentSnapshot documentSnapshot:task.getResult())
                    {
                        Store store = documentSnapshot.toObject(Store.class);
                        store.setStoreid(documentSnapshot.getId());
                        list.add(store);
                    }

                    iBranchLoadListener.onBranchLoadSuccess(list);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBranchLoadListener.onBranchLoadFailed(e.getMessage());

            }
        });
    }

    @Override
    public void onAllStoreLoadFailed(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBranchLoadSuccess(List<Store> storeList) {
        MyStoreAdapter adapter = new MyStoreAdapter(getActivity(),storeList);
        recycler_store.setAdapter(adapter);
        recycler_store.setVisibility(View.VISIBLE);

        dialog.dismiss();

    }

    @Override
    public void onBranchLoadFailed(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
        dialog.dismiss();

    }
}

