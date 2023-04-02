package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.giftplannerv1.R;

import java.util.List;

/* The purpose of this adapter is to control the RecyclerView used to display events */
public class EventAdapter extends RecyclerView.Adapter<EventHolder> {
    private String[] testList;

    /* Constructor. This takes an array of strings as an input to display to the user. The array
    of strings is only being used for testing at the moment. */
    public EventAdapter(String[] items) {
        testList = items;
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
        holder.mEventTextView.setText(testList[position].toString());
    }

    /* The third of three methods that need to be overridden when creating an adapter. This
    method is responsible for returning the number of items in the list being displayed.
     */
    @Override
    public int getItemCount() { return testList.length; }
}

