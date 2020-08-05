package com.novankrisnady.rentalstudioband.Interface;

import com.novankrisnady.rentalstudioband.Model.Banner;

import java.util.List;

public interface IBannerLoadListener {
    void onBannerLoadSuccess (List<Banner> banners);
    void onBannerLoadFailed (String message);
}
