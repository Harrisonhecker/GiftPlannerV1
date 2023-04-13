package ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
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
import java.util.Map;

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
        this.eventAdapter = new EventAdapter(EventsFragment.this,this.items, activity.userModel);
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
                Log.d(TAG, "onViewCreated -> Events retrieved");
                Log.d(TAG, data.toString());
                initDataset();
            }
        });

        // if user wants to add an event
        binding.addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // navigate to add event fragment
                NavHostFragment.findNavController(EventsFragment.this)
                        .navigate(R.id.action_EventsFragment_to_AddEventFragment);

            }
        });

        // if user wants to edit their profile
        binding.editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // navigate to edit profile page
                NavHostFragment.findNavController(EventsFragment.this)
                        .navigate(R.id.action_EventsFragment_to_EditProfileFragment);
            }
        });
    }

    /* This function essentially refreshes the list of events when the user returns to the page */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        activity.userModel.getEvents().observe(getViewLifecycleOwner(),
                new Observer<ArrayList<Object>>() {

                    //when the data loads, do something
                    @Override
                    public void onChanged(ArrayList<Object> data) {
                        Log.d(TAG, "onResume -> Events retrieved");
                        Log.d(TAG, data.toString());
                        initDataset();
                    }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /* Initialize the data for the RecyclerView that displays events */
    private void initDataset() {

        // if data from firebase is read in
        if (activity.userModel.getEvents().getValue() != null) {
            this.items = new String[activity.userModel.getEvents().getValue().size()];

            //if there are more than 0 events associated with the user
            if (this.items.length > 0) {
                for (int i = 0; i < this.items.length; i++) {
                    Map<String, Object> event = (Map<String, Object>) activity.userModel.getEvents().getValue().get(i);
                    this.items[i] = String.valueOf(event.get("name"));
                    Log.d(TAG, this.items[i]);
                }
            } else { // if the user has not added any events yet
                this.items = new String[1];
                this.items[0] = "You have no events";
            }
        } else { // if the data from firebase has not been received yet
            this.items = new String[1];
            this.items[0] = "Events are currently loading";
        }

        // first time initDataset is called, the eventAdapter has not been declared yet
        if (this.eventAdapter != null) {
            this.eventAdapter.updateData(this.items);
        }
    }

}