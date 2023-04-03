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

    private EventModel eventModel;

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


        eventModel = new ViewModelProvider((ViewModelStoreOwner) this).get(EventModel.class);
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //not necessary getUser is called when user signs in
                //activity.userModel.getUser();

                //construct a new event object with current values
                Map<String, Object> newEvent = constructEventObject();

                //get list of events
                MutableLiveData<ArrayList<Object>> events = activity.userModel.getEvents();

                //add event to list of events
                Log.d(TAG, "Before adding: " + String.valueOf(events.getValue()));
                events.getValue().add(newEvent);
                Log.d(TAG, "After adding: " + String.valueOf(events.getValue()));

                //update the user's events array
                //Map<String, Object> update = new HashMap<>();
                //update.put("events", events);
                activity.userModel.updateEventsArray(newEvent);

                NavHostFragment.findNavController(AddEventFragment.this)
                        .navigate(R.id.action_addEventFragment_to_EventsFragment);
            }
        });

        binding.backToEventsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(AddEventFragment.this)
                        .navigate(R.id.action_addEventFragment_to_EventsFragment);
            }
        });

    }

    private Map<String, Object> constructEventObject() {
        Map<String, Object> data = new HashMap<>();

        String name = binding.eventName.getText().toString();
        data.put("name", name);

        String date = binding.date.getText().toString();
        data.put("date", date);

        String budget = binding.budget.getText().toString();
        data.put("purchasing_budget", budget);

        boolean notify = binding.remindSwitch.isChecked();
        data.put("notify", notify);

        data.put("budget_met", false);
        data.put("members", new ArrayList<Map<String, Object>>());

        return data;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}