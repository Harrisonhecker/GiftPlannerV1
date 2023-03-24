package ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.giftplannerv1.R;
import com.example.giftplannerv1.databinding.SignupFragmentBinding;

public class SignupFragment extends Fragment {

    private SignupFragmentBinding binding;

    private String TAG = "SignupFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = requireActivity();

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = SignupFragmentBinding.inflate(inflater, container, false);

        Log.d(TAG, "onCreateView called");

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.finishSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(SignupFragment.this)
                        .navigate(R.id.action_SignupFragment_to_EventsFragment);
                printInformation();
            }
        });

        binding.backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(SignupFragment.this)
                        .navigate(R.id.action_SignupFragment_to_LoginFragment);
            }
        });
    }

    public void printInformation() {
        Log.d(TAG, "printInformation() called");
        String firstName = binding.signupFirstname.getText().toString();
        String lastName = binding.signupLastname.getText().toString();
        String email = binding.signupEmail.getText().toString();
        String username = binding.signupUsername.getText().toString();
        String password = binding.signupPassword.getText().toString();

        Log.d(TAG, "First Name: " + firstName);
        Log.d(TAG, "Last Name: " + lastName);
        Log.d(TAG, "Email: " + email);
        Log.d(TAG, "Username: " + username);
        Log.d(TAG, "Password: " + password);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
