package com.example.bookabarber.Interface;

import java.util.List;

public interface IAllStoreLoadListener {

    void onAllStoreLoadSuccess(List<String> areaNameList);
    void onAllStoreLoadFailed(String message);
}