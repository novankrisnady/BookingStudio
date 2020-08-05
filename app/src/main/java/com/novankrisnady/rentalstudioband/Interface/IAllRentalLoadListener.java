package com.novankrisnady.rentalstudioband.Interface;

import java.util.List;

public interface IAllRentalLoadListener {
    void onAllRentalLoadSuccess(List<String> areaNameList);
    void onAllRentalLoadFailed(String message);

}
