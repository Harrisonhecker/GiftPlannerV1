package adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.giftplannerv1.R;

import data.EventModel;

/* The purpose of the ViewHolder is to display (and cache) a single item within a list displayed
within a RecyclerView.
 */
public class ViewEventHolder extends RecyclerView.ViewHolder {

    public Button mViewEventButton;

    /* Constructor. Creates the item to view and (for right now) sets its data */
    ViewEventHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.view_event_list_item, parent, false));
        mViewEventButton = itemView.findViewById(R.id.view_event_info);
    }

    /* This method currently is not needed. But eventually we will probably use this to set all
    the data in the ViewHolder, rather than doing it in the constructor.
     */
    void bind(EventModel event) {
    }
}
