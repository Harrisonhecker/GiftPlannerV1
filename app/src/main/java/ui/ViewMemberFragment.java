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
import com.example.giftplannerv1.databinding.ViewMemberFragmentBinding;

import java.util.ArrayList;
import java.util.Map;

import activities.LoginActivity;
import adapters.ViewEventAdapter;
import adapters.ViewMemberAdapter;
import data.UserModel;

public class ViewMemberFragment extends Fragment {
    private ViewMemberFragmentBinding binding;

    private RecyclerView viewMemberRecyclerView;
    private ViewMemberAdapter viewMemberAdapter;
    private LinearLayoutManager viewMemberLayoutManager;
    private String[] items;
    private String TAG = "ViewMember";

    private LoginActivity activity;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        activity = (LoginActivity) getActivity();
        binding = ViewMemberFragmentBinding.inflate(inflater, container, false);

        Log.d(TAG, "onCreateView called");

        // create the test data to be displayed in the RecyclerView
        this.initDataset();

        // create the RecyclerView
        this.viewMemberRecyclerView = binding.viewMemberRecyclerView;

        // create and set the adapter for the RecyclerView
        this.viewMemberAdapter = new ViewMemberAdapter(ViewMemberFragment.this,this.items, activity.userModel);
        this.viewMemberRecyclerView.setAdapter(this.viewMemberAdapter);

        // create and set the LayoutManager for the RecyclerView
        this.viewMemberLayoutManager = new LinearLayoutManager(this.getContext());
        this.viewMemberRecyclerView.setLayoutManager(this.viewMemberLayoutManager);

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ViewMemberFragment.this)
                        .navigate(R.id.action_viewMemberFragment_to_addGiftFragment);

            }
        });

        binding.backToEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ViewMemberFragment.this)
                        .navigate(R.id.action_viewMemberFragment_to_viewEventFragment);

            }
        });

        binding.deleteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.userModel.deleteMember();
                NavHostFragment.findNavController(ViewMemberFragment.this)
                        .navigate(R.id.action_viewMemberFragment_to_viewEventFragment);
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
                        viewMemberAdapter.notifyDataSetChanged();
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
        if (activity.userModel.getGifts().getValue() != null) {
            this.items = new String[activity.userModel.getGifts().getValue().size()];

            //if there are more than 0 events associated with the user
            if (this.items.length > 0) {
                for (int i = 0; i < this.items.length; i++) {
                    Map<String, Object> gift = (Map<String, Object>) activity.userModel.getGifts().getValue().get(i);
                    this.items[i] = String.valueOf(gift.get("name"));
                    Log.d(TAG, this.items[i]);
                }
            } else { // if the user has not added any events yet
                this.items = new String[1];
                this.items[0] = "No gifts added for this member";
            }
        } else { // if the data from firebase has not been received yet
            this.items = new String[1];
            this.items[0] = "Gifts are currently loading";
        }

        // first time initDataset is called, the eventAdapter has not been declared yet
        if (this.viewMemberAdapter != null) {
            this.viewMemberAdapter.updateData(this.items);
        }


    }
}
