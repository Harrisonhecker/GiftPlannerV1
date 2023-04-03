package ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.giftplannerv1.databinding.LoginFragmentBinding;
import com.example.giftplannerv1.databinding.ViewGiftFragmentBinding;

import activities.LoginActivity;



public class ViewGiftFragment extends Fragment {
    private LoginActivity activity;

    private ViewGiftFragmentBinding binding;

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

        binding = ViewGiftFragmentBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.name.setText("Name: " + String.valueOf(activity.userModel.currentGift.get("name")));
        binding.price.setText("Price: $" + String.valueOf(activity.userModel.currentGift.get("price")));
        binding.link.setText("Link: " + String.valueOf(activity.userModel.currentGift.get("link")));
    }
}
