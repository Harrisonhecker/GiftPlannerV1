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

        //if user wants to log in to an account
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // do some simple checking to make sure fields have been populated
                String email = binding.loginEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Log.d(TAG, "Email or password was empty");
                    Toast.makeText(getContext(), "Please provide both an email and password.",
                            Toast.LENGTH_LONG).show();
                } else {

                    //this is skeleton logic for not signing in if firebase authentication fails
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

        //if user wants to create a new account
        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // navigate to sign-up page
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