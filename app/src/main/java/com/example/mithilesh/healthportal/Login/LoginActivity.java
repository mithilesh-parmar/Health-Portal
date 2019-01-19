package com.example.mithilesh.healthportal.Login;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mithilesh.healthportal.MainActivity;
import com.example.mithilesh.healthportal.Model.User;
import com.example.mithilesh.healthportal.R;
import com.example.mithilesh.healthportal.SingleFragmentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends SingleFragmentActivity implements LoginFragment.LoginListener,
        SignupFragment.SignupListener {

    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;

    @Override
    public Fragment createFragment() {
        return LoginFragment.createFragment();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null)showMainActivity();

    }

    @Override
    public void loginRequest(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email,pass)
                .addOnSuccessListener(authResult -> {
                    Log.i(TAG, "loginRequest: Sucess");
                   showMainActivity();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void showSignupFragment() {
        Log.i(TAG, "showSignupFragment: ");
        fm.beginTransaction()
                .replace(R.id.fragment_container,SignupFragment.createFragment())
                .commit();
    }

    @Override
    public void forgotPasswordRequest() {
        Log.i(TAG, "forgotPasswordRequest: ");
    }

    @Override
    public void signUpRequest(String email, String pass) {
        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnSuccessListener(authResult -> {
                    Log.i(TAG, "signUpRequest: Success");
                    showLoginFragment();
                })
                .addOnFailureListener(e -> {
                    Log.i(TAG, "signUpRequest: Failure "+e);
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showMainActivity() {
        FirebaseUser user = mAuth.getCurrentUser();
        //String displayName, String uid, String email, String phoneNumber
        User currentUser = User.getInstance(
                user.getDisplayName(),
                user.getUid(),
                user.getEmail(),
                user.getPhoneNumber()

        );
        User.setUser(currentUser);
        Log.i(TAG, "showMainActivity: User already logged in ");
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }


    private void showLoginFragment() {
        Log.i(TAG, "showLoginFragment: ");
        fm.beginTransaction()
                .replace(R.id.fragment_container,LoginFragment.createFragment())
                .commit();
    }
}
