package com.example.mithilesh.healthportal.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mithilesh.healthportal.R;
import com.example.mithilesh.healthportal.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";


    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private LoginListener listener;

    //XML view Components
    private TextInputEditText emailInput,passInput;


    public static LoginFragment createFragment(){
        return new LoginFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (LoginListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth= FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Logging in...");
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentLoginBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_login,
                container,
                false
        );

        this.emailInput = binding.emailTextInput;
        this.passInput = binding.passTextInput;

        binding.loginButton.setOnClickListener((e)->{

            if (!isAdded())return;

            if (emailInput.length()<=0 || passInput.length()<=0 ) return;

            String email = emailInput.getText().toString();
            String pass = passInput.getText().toString();

            listener.loginRequest(email,pass);

        });


        binding.forgotPasswordText.setOnClickListener((e)->listener.forgotPasswordRequest());


        binding.signupButton.setOnClickListener((e)->{
            listener.showSignupFragment();
        });

        return binding.getRoot();
    }

   @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing())progressDialog.hide();
    }


    public interface LoginListener{
       void loginRequest(String email, String pass);
       void showSignupFragment();
       void forgotPasswordRequest();
    }
}
