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

    /* Constructor. This takes an array of strings as an input to display to the user.*/
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

    /* The second of three methods that need to be overridden when creating an adapter. This
    method is responsible for binding data to a ViewHolder object that represents an item in the
    list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewMemberHolder holder, int position) {
        holder.mViewMemberButton.setText(testList[position].toString());
        holder.mViewMemberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userModel.getGift(testList[holder.getAbsoluteAdapterPosition()]);
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_viewMemberFragment_to_viewGiftFragment);

            }
        });
    }

    /* The third of three methods that need to be overridden when creating an adapter. This
    method is responsible for returning the number of items in the list being displayed.
     */
    @Override
    public int getItemCount() { return testList.length; }
}



