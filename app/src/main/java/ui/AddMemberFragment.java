package ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.fragment.NavHostFragment;

import com.example.giftplannerv1.R;
import com.example.giftplannerv1.databinding.AddEventFragmentBinding;
import com.example.giftplannerv1.databinding.AddMemberFragmentBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import activities.LoginActivity;
import data.EventModel;

public class AddMemberFragment extends Fragment {
    private AddMemberFragmentBinding binding;
    private LoginActivity activity;

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

        binding = AddMemberFragmentBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //if user wants to add a member
        binding.addMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //construct a new member object with current values
                Map<String, Object> newMember = constructMemberObject();

                //get list of members
                MutableLiveData<ArrayList<Object>> members = activity.userModel.getMembers();

                //add member to list of members
                members.getValue().add(newMember);

                //update the user's member array
                activity.userModel.updateMembersArray();

                // navigate back to list of members (an event page)
                NavHostFragment.findNavController(AddMemberFragment.this)
                        .navigate(R.id.action_addMemberFragment_to_viewEventFragment);


            }
        });

        //if user wants to navigate back to list of members
        binding.backToEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // navigate back to list of members (an event page)
                NavHostFragment.findNavController(AddMemberFragment.this)
                        .navigate(R.id.action_addMemberFragment_to_viewEventFragment);
            }
        });
    }

    /* Build a member object to be added to the database */
    private Map<String, Object> constructMemberObject() {
        Map<String, Object> data = new HashMap<>();

        //set member name
        String name = binding.name.getText().toString();
        data.put("name", name);

        //set member gifts
        data.put("gifts", new ArrayList<Map<String, Object>>());

        return data;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
