package ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.navigation.fragment.NavHostFragment;

import com.example.giftplannerv1.R;
import com.example.giftplannerv1.databinding.LoginFragmentBinding;
import com.google.firebase.auth.FirebaseAuth;

import activities.LoginActivity;

public class LoginFragment extends Fragment {

    private LoginFragmentBinding binding;

    private String TAG = "FirstFragment";
    private FirebaseAuth mAuth;

    private LoginActivity activity;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (LoginActivity) getActivity();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = LoginFragmentBinding.inflate(inflater, container, false);

        Log.d(TAG, "onCreateView called");

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();
                Log.d(TAG, email);
                Log.d(TAG, password);
                if (email.isEmpty() || password.isEmpty()) {
                    Log.d(TAG, "Email or password was empty");
                    Toast.makeText(getContext(), "Please provide both an email and password.",
                            Toast.LENGTH_LONG).show();
                } else {
                    boolean result = activity.userModel.signIn(email, password);
                    if (true) {
                        NavHostFragment.findNavController(LoginFragment.this)
                                .navigate(R.id.action_LoginFragment_to_EventsFragment);
                    } else {
                        Toast.makeText(getContext(), "Incorrect email or password :(",
                                Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "LoginFragment -> Sign In Failed");
                    }
                }
            }
        });
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_LoginFragment_to_SignupFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}