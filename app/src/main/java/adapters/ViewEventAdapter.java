package adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.giftplannerv1.R;

import java.util.List;

import data.UserModel;
import ui.EventsFragment;
import ui.ViewEventFragment;

/* The purpose of this adapter is to control the RecyclerView used to display events */
public class ViewEventAdapter extends RecyclerView.Adapter<ViewEventHolder> {
    private String[] testList;

    private ViewEventFragment fragment;

    private UserModel userModel;

    /* Constructor. This takes an array of strings as an input to display to the user. */
    public ViewEventAdapter(ViewEventFragment currentFragment, String[] items, UserModel uModel) {
        testList = items;
        fragment = currentFragment;
        userModel = uModel;
    }

    public void updateData(String[] items) {
        testList = items;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewEventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewEventHolder(inflater, parent);
    }

    /* The second of three methods that need to be overridden when creating an adapter. This
    method is responsible for binding data to a ViewHolder object that represents an item in the
    list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewEventHolder holder, int position) {
        holder.mViewEventButton.setText(testList[position].toString());

        holder.mViewEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userModel.getMember(testList[holder.getAbsoluteAdapterPosition()]);
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_viewEventFragment_to_viewMemberFragment);

            }
        });
    }

    /* The third of three methods that need to be overridden when creating an adapter. This
    method is responsible for returning the number of items in the list being displayed.
     */
    @Override
    public int getItemCount() { return testList.length; }
}



