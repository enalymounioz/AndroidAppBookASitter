package com.example.bookabarber.Interface;

import com.example.bookabarber.Model.Banner;

import java.util.List;

    public interface ILookbookLoadListener{
        void onLookbookLoadSuccess (List<Banner>banners);
        void onLookbookLoadFailed(String message);
    }

