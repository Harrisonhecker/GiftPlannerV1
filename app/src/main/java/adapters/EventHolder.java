package adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.giftplannerv1.R;

import data.EventModel;

/* The purpose of the ViewHolder is to display (and cache) a single item within a list displayed
within a RecyclerView.
 */
public class EventHolder extends RecyclerView.ViewHolder {

    public TextView mEventTextView;

    /* Constructor. Creates the item to view and (for right now) sets its data */
    EventHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.event_list_item, parent, false));
        mEventTextView = itemView.findViewById(R.id.event_info);
    }

    /* This method currently is not needed. But eventually we will probably use this to set all
    the data in the ViewHolder, rather than doing it in the constructor.
     */
    void bind(EventModel event) {
    }
}

