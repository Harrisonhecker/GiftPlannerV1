package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftplannerv1.R;

import data.UserModel;
import ui.ViewEventFragment;
import ui.ViewMemberFragment;

/* The purpose of this adapter is to control the RecyclerView used to display events */
public class ViewMemberAdapter extends RecyclerView.Adapter<ViewMemberHolder> {
    private String[] testList;

    private ViewMemberFragment fragment;

    private UserModel userModel;

    /* Constructor. This takes an array of strings as an input to display to the user. The array
    of strings is only being used for testing at the moment. */
    public ViewMemberAdapter(ViewMemberFragment currentFragment, String[] items, UserModel uModel) {
        testList = items;
        fragment = currentFragment;
        userModel = uModel;
    }

    public void updateData(String[] items) {
        testList = items;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewMemberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new ViewMemberHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewMemberHolder holder, int position) {
        //Button button = new Button(holder.itemView.getContext());
        //button.setText(testList[position].toString());
        holder.mViewMemberButton.setText(testList[position].toString());
        holder.mViewMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userModel.getGift(testList[holder.getAbsoluteAdapterPosition()]);
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_viewMemberFragment_to_viewGiftFragment);

            }
        });
        //holder.mEventTextView.setText(testList[position].toString());
    }

    @Override
    public int getItemCount() { return testList.length; }
}



