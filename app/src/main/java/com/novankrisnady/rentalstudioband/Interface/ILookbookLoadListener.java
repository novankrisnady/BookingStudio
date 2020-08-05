package com.novankrisnady.rentalstudioband.Interface;

import com.novankrisnady.rentalstudioband.Model.Banner;

import java.util.List;

public interface ILookbookLoadListener {
    void onLookbookLoadSuccess (List<Banner> banners);
    void onLookbookLoadFailed (String message);
}
