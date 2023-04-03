package ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftplannerv1.R;
import com.example.giftplannerv1.databinding.EventsFragmentBinding;
import com.example.giftplannerv1.databinding.ViewEventFragmentBinding;

import java.util.ArrayList;
import java.util.Map;

import activities.LoginActivity;
import adapters.ViewEventAdapter;
import data.UserModel;

public class
ViewEventFragment extends Fragment {
    private ViewEventFragmentBinding binding;

    private RecyclerView viewEventRecyclerView;
    private ViewEventAdapter viewEventAdapter;
    private LinearLayoutManager viewEventLayoutManager;
    private String[] items;
    private String TAG = "ViewEvent";

    private LoginActivity activity;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        activity = (LoginActivity) getActivity();
        binding = ViewEventFragmentBinding.inflate(inflater, container, false);

        Log.d(TAG, "onCreateView called");

        // create the test data to be displayed in the RecyclerView
        this.initDataset();

        // create the RecyclerView
        this.viewEventRecyclerView = binding.viewEventRecyclerView;

        // create and set the adapter for the RecyclerView
        this.viewEventAdapter = new ViewEventAdapter(ViewEventFragment.this,this.items, activity.userModel);
        this.viewEventRecyclerView.setAdapter(this.viewEventAdapter);

        // create and set the LayoutManager for the RecyclerView
        this.viewEventLayoutManager = new LinearLayoutManager(this.getContext());
        this.viewEventRecyclerView.setLayoutManager(this.viewEventLayoutManager);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ViewEventFragment.this)
                        .navigate(R.id.action_viewEventFragment_to_addMemberFragment);

            }
        });

        // register an observer on the events MutableLiveData
        activity.userModel.getEvents().observe(getViewLifecycleOwner(),
                new Observer<ArrayList<Object>>() {

                    //when the data loads, do something
                    @Override
                    public void onChanged(ArrayList<Object> data) {
                        Log.d(TAG, "Events retrieved");
                        Log.d(TAG, data.toString());
                        initDataset();
                        viewEventAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void initDataset() {

        // if data from firebase is read in
        if (activity.userModel.getMembers().getValue() != null) {
            this.items = new String[activity.userModel.getMembers().getValue().size()];

            //if there are more than 0 events associated with the user
            if (this.items.length > 0) {
                for (int i = 0; i < this.items.length; i++) {
                    Map<String, Object> member = (Map<String, Object>) activity.userModel.getMembers().getValue().get(i);
                    this.items[i] = String.valueOf(member.get("name"));
                    Log.d(TAG, this.items[i]);
                }
            } else { // if the user has not added any events yet
                this.items = new String[1];
                this.items[0] = "No members for this event";
            }
        } else { // if the data from firebase has not been received yet
            this.items = new String[1];
            this.items[0] = "Members are currently loading";
        }

        // first time initDataset is called, the eventAdapter has not been declared yet
        if (this.viewEventAdapter != null) {
            this.viewEventAdapter.updateData(this.items);
        }


    }
}
