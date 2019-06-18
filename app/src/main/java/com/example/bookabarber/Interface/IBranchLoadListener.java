package com.example.bookabarber.Interface;

import com.example.bookabarber.Model.Store;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Store> storeList);
    void onBranchLoadFailed(String message);
}