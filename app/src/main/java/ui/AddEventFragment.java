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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.fragment.NavHostFragment;

import com.example.giftplannerv1.R;
import com.example.giftplannerv1.databinding.AddEventFragmentBinding;

import data.EventModel;
import data.UserModel;

public class AddEventFragment extends Fragment {

    private AddEventFragmentBinding binding;

    private EventModel eventModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = requireActivity();

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


    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
