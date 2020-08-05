package com.novankrisnady.rentalstudioband.Interface;

import com.novankrisnady.rentalstudioband.Model.Studio;

import java.util.List;

public interface IStudioBandLoadListener {
    void onStudioBandLoadSuccess(List<Studio> studioList);
    void onStudioBandLoadFailed(String message);
}
