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

        binding.finishSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createUser();

                NavHostFragment.findNavController(SignupFragment.this)
                        .navigate(R.id.action_SignupFragment_to_EventsFragment);

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

    private void createUser() {

        String email = binding.signupEmail.getText().toString();
        String password = binding.signupPassword.getText().toString();
        Map<String, Object> userData = buildUserDataObject();
        activity.userModel.addUser(email, password, userData);


        /*mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Map<String, Object> userData = buildUserDataObject();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("Users").document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener((OnSuccessListener<Void>) aVoid -> {
                                        raiseResultToast("Account Successfully Created!");
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            raiseResultToast("Account Creation Failed.");
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });*/
    }

    private Map<String, Object> buildUserDataObject() {

        //apparently you can't serialize arrays
        //Object[] emptyEvents = {};
        //Object[] emptyGroups = {};
        //Object[] emptyInterests = {};
        //Object[] emptyWishlists = {};
        List<Object> emptyEvents = new ArrayList<>();
        List<Object> emptyGroups = new ArrayList<>();
        List<Object> emptyInterests = new ArrayList<>();
        List<Object> emptyWishlists = new ArrayList<>();

        Map<String, Object> data = new HashMap<>();
        data.put(getString(R.string.firebase_email), binding.signupEmail.getText().toString());
        data.put(getString(R.string.firebase_events), emptyEvents);
        data.put(getString(R.string.firebase_first_name), binding.signupFirstname.getText().toString());
        data.put(getString(R.string.firebase_groups), emptyGroups);
        data.put(getString(R.string.firebase_interests), emptyInterests);
        data.put(getString(R.string.firebase_last_name), binding.signupLastname.getText().toString());
        data.put(getString(R.string.firebase_password), binding.signupPassword.getText().toString());
        data.put(getString(R.string.firebase_spending_budget), binding.signupBudget.getText().toString());
        data.put(getString(R.string.firebase_username), binding.signupUsername.getText().toString());
        data.put(getString(R.string.firebase_wishlists), emptyWishlists);
        data.put("profile_picture", "");

        return data;
    }

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

    public void raiseResultToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, message);
        printInformation();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
