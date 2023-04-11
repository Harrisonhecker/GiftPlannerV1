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

        // if user wants to add a gift
        binding.addGiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //construct a new gift object with current values
                Map<String, Object> newGift = constructGiftObject();

                //get list of gifts
                MutableLiveData<ArrayList<Object>> gifts = activity.userModel.getGifts();

                //add gift to list of gifts
                gifts.getValue().add(newGift);

                //update the user's gifts array in the database
                activity.userModel.updateGiftsArray();

                // navigate back to list of gifts (an event's member page)
                NavHostFragment.findNavController(AddGiftFragment.this)
                        .navigate(R.id.action_addGiftFragment_to_viewMemberFragment);
            }
        });

        // if user wants to go back to the member page
        binding.backToMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // navigate back to list of gifts (an event's member page)
                NavHostFragment.findNavController(AddGiftFragment.this)
                        .navigate(R.id.action_addGiftFragment_to_viewMemberFragment);
            }
        });
    }

    /* Build a gift object to add to the database */
    private Map<String, Object> constructGiftObject() {

        Map<String, Object> data = new HashMap<>();

        // set gift name
        String name = binding.giftName.getText().toString();
        data.put("name", name);

        // set gift price
        String price = binding.price.getText().toString();
        data.put("price", price);

        // set gift link
        String link = binding.link.getText().toString();
        data.put("link", link);

        return data;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
