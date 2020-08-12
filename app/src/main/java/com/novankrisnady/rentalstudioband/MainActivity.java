package com.novankrisnady.rentalstudioband;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.novankrisnady.rentalstudioband.Common.Common;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int APP_REQUEST_CODE = 7117; //Any number you want

    private List<AuthUI.IdpConfig> providers; //Checked
    private FirebaseAuth firebaseAuth; //Checked
    private FirebaseAuth.AuthStateListener authStateListener; //Checked

    @BindView(R.id.btn_login) //Checked
    Button btn_login; //Checked
    @BindView(R.id.txt_skip) //Checked
    TextView txt_skip; //Checked

    @OnClick(R.id.btn_login) //Checked
    void loginUser() //Checked
    {
        startActivityForResult(AuthUI.getInstance() //Checked
        .createSignInIntentBuilder() //Checked
        .setAvailableProviders(providers).build(),APP_REQUEST_CODE); //Checked
    }

    @OnClick(R.id.txt_skip) //Checked
    void SkipLoginJustGoHome() //Checked
    {
        Intent intent = new Intent(this,HomeActivity.class); //Checked
        intent.putExtra(Common.IS_LOGIN,false); //Checked
        startActivity(intent); //Checked
    }

    @Override
    protected void onStart() { //Checked
        super.onStart(); //Checked
        firebaseAuth.addAuthStateListener(authStateListener); //Checked
    }

    @Override
    protected void onStop() { //Checked
        if (authStateListener!= null) //Checked
            firebaseAuth.removeAuthStateListener(authStateListener); //Checked
        super.onStop(); //Checked
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //Checked
        super.onActivityResult(requestCode, resultCode, data); //Checked
        if (requestCode == APP_REQUEST_CODE) //Checked
        {
            IdpResponse response = IdpResponse.fromResultIntent(data); //Checked
            if (resultCode == RESULT_OK) //Checked
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //Checked
            }
            else //Checked
            {
                Toast.makeText(this, "Failed to Sign In", Toast.LENGTH_SHORT).show(); //Checked
            }

            Intent intent = new Intent(this,HomeActivity.class);
            intent.putExtra(Common.IS_LOGIN,false);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { //Checked
        super.onCreate(savedInstanceState); //Checked

        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build()); //Checked

        firebaseAuth = FirebaseAuth.getInstance(); //Checked
        authStateListener = firebaseAuth1 -> { //Checked
            FirebaseUser user = firebaseAuth1.getCurrentUser(); //Checked
            {
                checkUserFromFirebase(user);;//Checked
            }
        };

        Dexter.withActivity(this)
                .withPermissions(new String[]{
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.WRITE_CALENDAR
                }).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) //If already logged
                {
                    //Get Token
                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    intent.putExtra(Common.IS_LOGIN,true);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    setContentView(R.layout.activity_main);
                    ButterKnife.bind(MainActivity.this);
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();

    }

    private void checkUserFromFirebase(FirebaseUser user) {


    }
}