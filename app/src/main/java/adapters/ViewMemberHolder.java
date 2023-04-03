package adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.giftplannerv1.R;

import data.EventModel;

/* The purpose of the ViewHolder is to display (and cache) a single item within a list displayed
within a RecyclerView.
 */
public class ViewMemberHolder extends RecyclerView.ViewHolder {

    public Button mViewMemberButton;

    /* Constructor. Creates the item to view and (for right now) sets its data */
    ViewMemberHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.view_member_list, parent, false));
        mViewMemberButton = itemView.findViewById(R.id.view_member_info);
    }

    /* This method currently is not needed. But eventually we will probably use this to set all
    the data in the ViewHolder, rather than doing it in the constructor.
     */
    void bind(EventModel event) {
    }
}
