package ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftplannerv1.R;
import com.example.giftplannerv1.databinding.EventsFragmentBinding;


import java.lang.reflect.Array;
import java.util.ArrayList;

import activities.LoginActivity;
import adapters.EventAdapter;
import data.UserModel;

public class EventsFragment extends Fragment {

    private EventsFragmentBinding binding;

    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private LinearLayoutManager eventLayoutManager;
    private String[] items;
    private String TAG = "SecondFragment";

    private LoginActivity activity;

    private UserModel userModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        activity = (LoginActivity) getActivity();
        binding = EventsFragmentBinding.inflate(inflater, container, false);

        Log.d(TAG, "onCreateView called");

        // create the test data to be displayed in the RecyclerView
        this.initDataset();

        // create the RecyclerView
        this.eventRecyclerView = binding.eventRecyclerView;

        // create and set the adapter for the RecyclerView
        this.eventAdapter = new EventAdapter(this.items);
        this.eventRecyclerView.setAdapter(this.eventAdapter);

        // create and set the LayoutManager for the RecyclerView
        this.eventLayoutManager = new LinearLayoutManager(this.getContext());
        this.eventRecyclerView.setLayoutManager(this.eventLayoutManager);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // register an observer on the events MutableLiveData
        activity.userModel.getEvents().observe(getViewLifecycleOwner(),
                new Observer<ArrayList<Object>>() {

            //when the data loads, do something
            @Override
            public void onChanged(ArrayList<Object> data) {
                Log.d(TAG, "Events retrieved");
                Log.d(TAG, data.toString());
            }
        });
        binding.addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EventsFragment.this)
                        .navigate(R.id.action_EventsFragment_to_AddEventFragment);
            }
        });
        binding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EventsFragment.this)
                        .navigate(R.id.action_EventsFragment_to_EditProfileFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initDataset() {

        this.items = new String[10];

        for (int i = 0; i < 10; i++) {
            this.items[i] = "This is element #" + i;
        }
    }
}