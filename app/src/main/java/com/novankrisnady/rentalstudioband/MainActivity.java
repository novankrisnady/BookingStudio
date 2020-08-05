package com.novankrisnady.rentalstudioband;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.novankrisnady.rentalstudioband.Common.Common;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int APP_REQUEST_CODE = 7117; //Any number you want

    private List<AuthUI.IdpConfig> providers;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.txt_skip)
    TextView txt_skip;

    @OnClick(R.id.btn_login)
    void loginUser()
    {
        startActivityForResult(AuthUI.getInstance() 
        .createSignInIntentBuilder()
        .setAvailableProviders(providers).build(),APP_REQUEST_CODE);
    }

    @OnClick(R.id.txt_skip)
    void SkipLoginJustGoHome()
    {
        Intent intent = new Intent(this,HomeActivity.class);
        intent.putExtra(Common.IS_LOGIN,false);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        if (authStateListener!= null)
            firebaseAuth.removeAuthStateListener(authStateListener);
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            }
            else
            {
                Toast.makeText(this, "Failed to Sign In", Toast.LENGTH_SHORT).show();
            }

            Intent intent = new Intent(this,HomeActivity.class);
            intent.putExtra(Common.IS_LOGIN,false);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth1 -> {
            FirebaseUser user = firebaseAuth1.getCurrentUser();
            if (user != null)
            {
                checkUserFromFirebase(user);
            }
        };

        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
    }

    private void checkUserFromFirebase(FirebaseUser user) {
        
    }
}