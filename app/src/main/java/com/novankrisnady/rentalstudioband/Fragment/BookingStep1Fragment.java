package com.novankrisnady.rentalstudioband.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.novankrisnady.rentalstudioband.Adapter.MyStudioAdapter;
import com.novankrisnady.rentalstudioband.Common.Common;
import com.novankrisnady.rentalstudioband.Common.SpacesItemDecoration;
import com.novankrisnady.rentalstudioband.Interface.IAllRentalLoadListener;
import com.novankrisnady.rentalstudioband.Interface.IStudioBandLoadListener;
import com.novankrisnady.rentalstudioband.Model.Studio;
import com.novankrisnady.rentalstudioband.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;

public class BookingStep1Fragment extends Fragment implements IAllRentalLoadListener, IStudioBandLoadListener {

    //Variable
    CollectionReference allRentalRef;
    CollectionReference studioBandRef;

    IAllRentalLoadListener iAllRentalLoadListener;
    IStudioBandLoadListener iStudioBandLoadListener;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.recycler_studio)
    RecyclerView recycler_studio;

    Unbinder unbinder;

    AlertDialog dialog;


    static BookingStep1Fragment instance;
    public static BookingStep1Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep1Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allRentalRef = FirebaseFirestore.getInstance().collection("AllRental");
        iAllRentalLoadListener = this;
        iStudioBandLoadListener = this;

        dialog = new SpotsDialog.Builder().setContext(getActivity()).build();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step_one,container,false);
        unbinder = ButterKnife.bind(this,itemView);

        initView();
        loadAllRental();

        return itemView;
    }

    private void initView() {
        recycler_studio.setHasFixedSize(true);
        recycler_studio.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_studio.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void loadAllRental() {
        allRentalRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            List<String> list = new ArrayList<>();
                            list.add("Please choose city");
                            for (QueryDocumentSnapshot documentSnapshot:task.getResult())
                                list.add(documentSnapshot.getId());
                            iAllRentalLoadListener.onAllRentalLoadSuccess(list);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iAllRentalLoadListener.onAllRentalLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllRentalLoadSuccess(List<String> areaNameList) {
        spinner.setItems(areaNameList);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (position > 0)
                {
                    loadStudioBandOfCity(item.toString());
                }
                else
                    recycler_studio.setVisibility(View.GONE);
            }
        });
    }

    private void loadStudioBandOfCity(String cityName) {
        dialog.show();

        Common.city = cityName;

        studioBandRef = FirebaseFirestore.getInstance()
                .collection("AllRental")
                .document(cityName)
                .collection("StudioBand");

        studioBandRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Studio> list = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                    {
                        Studio studio = documentSnapshot.toObject(Studio.class);
                        studio.setStudioId(documentSnapshot.getId());
                        list.add(studio);
                    }
                    iStudioBandLoadListener.onStudioBandLoadSuccess(list);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iStudioBandLoadListener.onStudioBandLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onAllRentalLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onStudioBandLoadSuccess(List<Studio> studioList) {
        MyStudioAdapter adapter = new MyStudioAdapter(getActivity(),studioList);
        recycler_studio.setAdapter(adapter);
        recycler_studio.setVisibility(View.VISIBLE);

        dialog.dismiss();
    }

    @Override
    public void onStudioBandLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }
}
