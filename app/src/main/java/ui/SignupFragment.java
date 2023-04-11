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
import androidx.navigation.fragment.NavHostFragment;

import com.example.giftplannerv1.R;
import com.example.giftplannerv1.databinding.SignupFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import activities.LoginActivity;

public class SignupFragment extends Fragment {

    private SignupFragmentBinding binding;
    private FirebaseAuth mAuth;
    private String TAG = "SignupFragment";
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

        binding = SignupFragmentBinding.inflate(inflater, container, false);
        Log.d(TAG, "onCreateView called");
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //if user wants to create their account
        binding.finishSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkPassword(binding.signupPassword.getText().toString())) {
                    //create a user object and add it to the database
                    createUser();

                    // navigate to the main page that lists all events
                    NavHostFragment.findNavController(SignupFragment.this)
                            .navigate(R.id.action_SignupFragment_to_EventsFragment);
                } else {
                    Toast.makeText(getContext(), "Password have one uppercase " +
                            "letter, one lowercase letter, one digit, one special character, and " +
                                    "have a minimum of 8 characters. ",
                            Toast.LENGTH_LONG).show();
                }


            }
        });

        // if user wants to go back to login page
        binding.backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // navigate back into the login page
                NavHostFragment.findNavController(SignupFragment.this)
                        .navigate(R.id.action_SignupFragment_to_LoginFragment);
            }
        });
    }

    /* Create the user object and add it to the database */
    private void createUser() {

        // find the email and password
        String email = binding.signupEmail.getText().toString();
        String password = binding.signupPassword.getText().toString();

        // create the user object
        Map<String, Object> userData = buildUserDataObject();

        // add the user object to the database
        // the email and password are passed as separate parameters for firebase to use for auth
        activity.userModel.addUser(email, password, userData);
    }

    /* Build the user database object */
    private Map<String, Object> buildUserDataObject() {

        Map<String, Object> data = new HashMap<>();

        // set events
        List<Object> emptyEvents = new ArrayList<>();
        data.put(getString(R.string.firebase_events), emptyEvents);

        // set groups
        List<Object> emptyGroups = new ArrayList<>();
        data.put(getString(R.string.firebase_groups), emptyGroups);

        // set interests
        List<Object> emptyInterests = new ArrayList<>();
        data.put(getString(R.string.firebase_interests), emptyInterests);

        // set wishlists
        List<Object> emptyWishlists = new ArrayList<>();
        data.put(getString(R.string.firebase_wishlists), emptyWishlists);

        //set all general account info
        data.put(getString(R.string.firebase_email), binding.signupEmail.getText().toString());
        data.put(getString(R.string.firebase_first_name), binding.signupFirstname.getText().toString());
        data.put(getString(R.string.firebase_last_name), binding.signupLastname.getText().toString());
        data.put(getString(R.string.firebase_password), binding.signupPassword.getText().toString());
        data.put(getString(R.string.firebase_spending_budget), binding.signupBudget.getText().toString());
        data.put(getString(R.string.firebase_username), binding.signupUsername.getText().toString());

        // will be used eventually for profile picture
        data.put("profile_picture", "");

        return data;
    }

    /* debugging method */
    private void printInformation() {
        Log.d(TAG, "printInformation() called");
        String firstName = binding.signupFirstname.getText().toString();
        String lastName = binding.signupLastname.getText().toString();
        String budget = binding.signupBudget.getText().toString();
        String email = binding.signupEmail.getText().toString();
        String username = binding.signupUsername.getText().toString();
        String password = binding.signupPassword.getText().toString();

        Log.d(TAG, "First Name: " + firstName);
        Log.d(TAG, "Last Name: " + lastName);
        Log.d(TAG, "Budget: " + budget);
        Log.d(TAG, "Email: " + email);
        Log.d(TAG, "Username: " + username);
        Log.d(TAG, "Password: " + password);

    }

    /* Show result for if account creation succeeded or failed */
    public void raiseResultToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, message);
        printInformation();
    }

    /* Check if password matches the requirements */
    private boolean checkPassword(String password) {

        //password must have
        //one uppercase letter
        //one lowercase letter
        //one digit
        //one special character
        //minimum length of 8 characters

        String pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        return password.matches(pattern);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
