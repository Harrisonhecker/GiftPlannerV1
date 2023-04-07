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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.fragment.NavHostFragment;

import com.example.giftplannerv1.R;
import com.example.giftplannerv1.databinding.AddEventFragmentBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import activities.LoginActivity;
import data.EventModel;

public class AddEventFragment extends Fragment {

    private AddEventFragmentBinding binding;
    private LoginActivity activity;
    private String TAG = "AddEventFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (LoginActivity) getActivity();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = AddEventFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //if user wants to add an event
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //construct a new event object with current values
                Map<String, Object> newEvent = constructEventObject();

                //get list of events
                MutableLiveData<ArrayList<Object>> events = activity.userModel.getEvents();

                //add event to list of events
                events.getValue().add(newEvent);

                //update the user's events array in database
                activity.userModel.updateEventsArray(newEvent);

                // navigate back to list of events page
                NavHostFragment.findNavController(AddEventFragment.this)
                        .navigate(R.id.action_addEventFragment_to_EventsFragment);
            }
        });

        //if user wants to navigate back to list of events page
        binding.backToEventsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // navigate back to list of events page
                NavHostFragment.findNavController(AddEventFragment.this)
                        .navigate(R.id.action_addEventFragment_to_EventsFragment);
            }
        });

    }

    /* This method constructs an event object to be added to the database */
    private Map<String, Object> constructEventObject() {

        Map<String, Object> data = new HashMap<>();

        // set event name
        String name = binding.eventName.getText().toString();
        data.put("name", name);

        // set event date
        String date = binding.date.getText().toString();
        data.put("date", date);

        // set event budget
        String budget = binding.budget.getText().toString();
        data.put("purchasing_budget", budget);

        // set event notification preference
        boolean notify = binding.remindSwitch.isChecked();
        data.put("notify", notify);

        // set if event has met budget
        data.put("budget_met", false);

        // set event members (empty at this point)
        data.put("members", new ArrayList<Map<String, Object>>());

        return data;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}