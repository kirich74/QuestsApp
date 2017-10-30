package com.kirich74.questsapp.FirstLaunch;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;

import com.kirich74.questsapp.R;
import com.kirich74.questsapp.mainscreen.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Kirill Pilipenko on 30.10.2017.
 */

public class SignInActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CODE_SIGN_IN = 10;

    private SignInButton signInButton;

    private GoogleApiClient googleApiClient;

    private PrefManager prefManager;

    @Override
    public void onConnectionFailed(@NonNull final ConnectionResult connectionResult) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        signInButton.setScopes(googleSignInOptions.getScopeArray());
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                if (account != null) {
                    Toast.makeText(this, account.getDisplayName() + " - " + account.getEmail(),
                            Toast.LENGTH_LONG).show();
                    prefManager = new PrefManager(this);
                    prefManager.setSavedEmail(account.getEmail());
                    launchHomeScreen();
                    finish();

                } else {
                    Toast.makeText(this, "Account == null!", Toast.LENGTH_LONG).show();
                }
            } else {
                Status status = result.getStatus();
                int statusCode = status.getStatusCode();
                Toast.makeText(this, "Failed to sign in: " + statusCode, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void launchHomeScreen() {
        startActivity(new Intent(SignInActivity.this, MainActivity.class));
        finish();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    }
}