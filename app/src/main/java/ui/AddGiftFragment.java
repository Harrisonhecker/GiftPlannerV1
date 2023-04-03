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
import com.example.giftplannerv1.databinding.AddGiftFragmentBinding;
import com.example.giftplannerv1.databinding.AddMemberFragmentBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import activities.LoginActivity;
import data.EventModel;

public class AddGiftFragment extends Fragment {
    private AddGiftFragmentBinding binding;

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

        binding = AddGiftFragmentBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.addGiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //not necessary getUser is called when user signs in
                //activity.userModel.getUser();

                //construct a new event object with current values
                Map<String, Object> newGift = constructGiftObject();

                //get list of events
                MutableLiveData<ArrayList<Object>> gifts = activity.userModel.getGifts();

                //add event to list of events
                //Log.d(TAG, "Before adding: " + String.valueOf(events.getValue()));
                gifts.getValue().add(newGift);
                //Log.d(TAG, "After adding: " + String.valueOf(events.getValue()));

                //update the user's events array
                //Map<String, Object> update = new HashMap<>();
                //update.put("events", events);
                activity.userModel.updateGiftsArray(newGift);


                NavHostFragment.findNavController(AddGiftFragment.this)
                        .navigate(R.id.action_addGiftFragment_to_viewMemberFragment);


            }
        });
    }
    private Map<String, Object> constructGiftObject() {
        Map<String, Object> data = new HashMap<>();

        String name = binding.giftName.getText().toString();
        String price = binding.price.getText().toString();
        String link = binding.link.getText().toString();
        data.put("name", name);
        data.put("price", price);
        data.put("link", link);

        return data;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
