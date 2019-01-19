package com.example.mithilesh.healthportal.Login;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mithilesh.healthportal.R;
import com.example.mithilesh.healthportal.databinding.FragmentSignupBinding;

public class SignupFragment extends Fragment {


    private SignupListener listener;

    public static SignupFragment createFragment(){
        return new SignupFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (SignupListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSignupBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_signup,
                container,
                false
        );


        binding.signupButton.setOnClickListener((e)->{
            if (binding.emailTextInput.length()<=0||binding.passTextInput.length()<=0)return;



         listener.signUpRequest (binding.emailTextInput.getText().toString(),
                  binding.passTextInput.getText().toString());
        });

        return binding.getRoot();
    }




    public interface SignupListener{
        void signUpRequest(String email, String pass);
    }
}
