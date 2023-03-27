package ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.giftplannerv1.R;
import com.example.giftplannerv1.databinding.EventsFragmentBinding;

import adapters.EventAdapter;
import adapters.InterestAdapter;
import data.UserModel;

public class EditProfileFragment extends Fragment {

    private EditProfileFragmentBinding binding;
    private String TAG = "EditProfileFragment";
    private RecyclerView interestRecyclerView;
    private InterestAdapter interestAdapter;
    private LinearLayoutManager interestLayoutManager;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    private static final int REQUEST_CAMERA_PERMISSION = 1;

    public EditProfileFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        /*binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EventsFragment.this)
                        .navigate(R.id.action_EventsFragment_to_LoginFragment);
            }
        });*/
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
                printInformation();
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