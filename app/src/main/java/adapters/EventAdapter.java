package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.giftplannerv1.R;

import java.util.List;

import activities.LoginActivity;
import data.UserModel;
import ui.EventsFragment;

/* The purpose of this adapter is to control the RecyclerView used to display events */
public class EventAdapter extends RecyclerView.Adapter<EventHolder> {
    private String[] testList;
    private EventsFragment fragment;

    private UserModel userModel;

    /* Constructor. This takes an array of strings as an input to display to the user. The array
    of strings is only being used for testing at the moment. */
    public EventAdapter(EventsFragment currentFragment, String[] items, UserModel uModel) {
        testList = items;
        fragment = currentFragment;
        userModel = uModel;

    }

    public void updateData(String[] items) {
        testList = items;
        notifyDataSetChanged();
    }

    /* The first of three methods that need to be overridden when creating an adapter. This
    method is responsible for creating a new ViewHolder object that will be used to display a new
     item in the list. */
    @NonNull @Override
    public EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new EventHolder(inflater, parent);
    }

    /* The second of three methods that need to be overridden when creating an adapter. This
    method is responsible for binding data to a ViewHolder object that represents an item in the
    list.
     */
    @Override
    public void onBindViewHolder(@NonNull EventHolder holder, int position) {
        //Button button = new Button(holder.itemView.getContext());
        //button.setText(testList[position].toString());
        Log.d("Adapter",testList[position].toString());
        holder.mEventButton.setText(testList[position].toString());
        holder.mEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userModel.getEvent(testList[holder.getAbsoluteAdapterPosition()]);
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_EventsFragment_to_viewEventFragment);

            }
        });
    }

    /* The third of three methods that need to be overridden when creating an adapter. This
    method is responsible for returning the number of items in the list being displayed.
     */
    @Override
    public int getItemCount() { return testList.length; }
}

