package ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftplannerv1.databinding.EditProfileFragmentBinding;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.giftplannerv1.R;
import com.example.giftplannerv1.databinding.EventsFragmentBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import activities.LoginActivity;
import adapters.EventAdapter;
import adapters.InterestAdapter;
import data.UserModel;

public class EditProfileFragment extends Fragment {

    private EditProfileFragmentBinding binding;
    private String TAG = "EditProfileFragment";
    private RecyclerView interestRecyclerView;
    private InterestAdapter interestAdapter;
    private LinearLayoutManager interestLayoutManager;
    private LoginActivity activity;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int READ_REQUEST_CODE = 1;
    private static final int WRITE_REQUEST_CODE = 1;

    public EditProfileFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (LoginActivity) getActivity();
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {


                        // get picture data and create a bitmap
                        Bundle extras = result.getData().getExtras();
                        Bitmap thumbnail = (Bitmap) extras.get("data");

                        // Create a circular bitmap
                        Bitmap circularThumbnail = Bitmap.createBitmap(thumbnail.getWidth(),
                                thumbnail.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(circularThumbnail);

                        // Create a paint object with the BitmapShader
                        Paint paint = new Paint();
                        BitmapShader shader = new BitmapShader(thumbnail, Shader.TileMode.CLAMP,
                                Shader.TileMode.CLAMP);
                        paint.setShader(shader);

                        // Draw a circle on the canvas with the paint object
                        float centerX = thumbnail.getWidth() / 2f;
                        float centerY = thumbnail.getHeight() / 2f;
                        float radius = Math.min(centerX, centerY);

                        canvas.drawCircle(centerX, centerY, radius, paint);


                        // set the source of the profile picture icon
                        binding.profilePicture.setBackground(null);
                        binding.profilePicture.setImageBitmap(circularThumbnail);
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = EditProfileFragmentBinding.inflate(inflater, container, false);

        Log.d(TAG, "onCreateView called");

        /*
        // create the RecyclerView
        this.interestRecyclerView = binding.interestRecyclerView;

        // create and set the adapter for the RecyclerView
        this.interestAdapter = new InterestAdapter();
        this.interestRecyclerView.setAdapter(this.interestAdapter);

        // create and set the LayoutManager for the RecyclerView
        this.interestLayoutManager = new LinearLayoutManager(this.getContext());
        this.interestRecyclerView.setLayoutManager(this.interestLayoutManager);*/

        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        displayInfo();

        binding.backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EditProfileFragment.this)
                        .navigate(R.id.action_EditProfileFragment_to_EventsFragment);
            }
        });

        binding.finishEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> updates = buildUpdates();
                /*try {
                    storePicture();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/

                activity.userModel.updateUser(updates);

                NavHostFragment.findNavController(EditProfileFragment.this)
                        .navigate(R.id.action_EditProfileFragment_to_EventsFragment);
            }
        });

        binding.updateProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Update Profile Picture button pressed");
                // Check for camera permission at runtime
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request the permission
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.CAMERA},
                            REQUEST_CAMERA_PERMISSION);
                } else {
                    // Permission is already granted, launch the camera app
                    launchCamera();
                }
            }
        });
    }

    private void launchCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncher.launch(intent);
    }

    /*private void storePicture() throws IOException {

        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_REQUEST_CODE);
        }

        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_REQUEST_CODE);
        }


        binding.profilePicture.setDrawingCacheEnabled(true);
        Bitmap profilePic = binding.profilePicture.getDrawingCache();
        binding.profilePicture.setDrawingCacheEnabled(false);
        File storageDir = Environment.getExternalStorageDirectory();
        File imageFile = new File(storageDir, "profilePic.jpg");
        FileOutputStream outputStream = new FileOutputStream(imageFile);
        profilePic.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        outputStream.flush();
        outputStream.close();

    }*/

    private Map<String, Object> buildUpdates() {

        Map<String, Object> data = new HashMap<>();
        data.put(getString(R.string.firebase_email), binding.editEmail.getText().toString());
        data.put(getString(R.string.firebase_first_name), binding.editFirstname.getText().toString());
        data.put(getString(R.string.firebase_last_name), binding.editLastname.getText().toString());
        data.put(getString(R.string.firebase_password), binding.editPassword.getText().toString());
        data.put(getString(R.string.firebase_spending_budget),
                binding.editBudget.getText().toString());
        data.put(getString(R.string.firebase_username), binding.editUsername.getText().toString());

//        binding.profilePicture.setDrawingCacheEnabled(true);
//        Bitmap profilePic = binding.profilePicture.getDrawingCache();
//        binding.profilePicture.setDrawingCacheEnabled(false);
//
//        String profilePicString = bitmapToString(profilePic);
//        data.put("profile_picture", profilePicString);

        return data;
    }


    private void displayInfo() {
        String email = activity.userModel.getEmail();
        binding.editEmail.setText(email);

        String first_name = activity.userModel.getFirstName();
        binding.editFirstname.setText(first_name);

        String last_name = activity.userModel.getLastName();
        binding.editLastname.setText(last_name);

        String password = activity.userModel.getPassword();
        binding.editPassword.setText(password);

        String budget = activity.userModel.getBudget();
        binding.editBudget.setText(budget);

        String username = activity.userModel.getUsername();
        binding.editUsername.setText(username);

//        String profilePicString = activity.userModel.getProfilePic();
//        if (profilePicString != null) {
//            Bitmap picture = stringToBitmap(profilePicString);
//            binding.profilePicture.setImageBitmap(picture);
//        }
    }

    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public Bitmap stringToBitmap(String encodedString) {
        byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }



    private void printInformation() {
        Log.d(TAG, "printInformation() called");
        String firstName = binding.editFirstname.getText().toString();
        String lastName = binding.editLastname.getText().toString();
        String budget = binding.editBudget.getText().toString();
        String email = binding.editEmail.getText().toString();
        String username = binding.editUsername.getText().toString();
        String password = binding.editPassword.getText().toString();

        Log.d(TAG, "First Name: " + firstName);
        Log.d(TAG, "Last Name: " + lastName);
        Log.d(TAG, "Budget: " + budget);
        Log.d(TAG, "Email: " + email);
        Log.d(TAG, "Username: " + username);
        Log.d(TAG, "Password: " + password);

    }
}