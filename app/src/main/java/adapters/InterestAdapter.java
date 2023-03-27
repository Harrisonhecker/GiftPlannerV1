package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/* The purpose of this adapter is to control the RecyclerView used to display events */
public class InterestAdapter extends RecyclerView.Adapter<InterestHolder> {
    private String[] testList;

    /* Constructor. This takes an array of strings as an input to display to the user. The array
    of strings is only being used for testing at the moment. */
    public InterestAdapter() {
        testList = new String[6];
        /*testList[0]="Interest 1";
        testList[1]="Interest 2";
        testList[2]="Interest 3";
        testList[3]="Interest 4";
        testList[4]="Interest 5";
        testList[5]="Interest 6";*/
        testList[0]="";
        testList[1]="";
        testList[2]="";
        testList[3]="";
        testList[4]="";
        testList[5]="";


    }

    /* The first of three methods that need to be overridden when creating an adapter. This
    method is responsible for creating a new ViewHolder object that will be used to display a new
     item in the list. */
    @NonNull @Override
    public InterestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new InterestHolder(inflater, parent);
    }

    /* The second of three methods that need to be overridden when creating an adapter. This
    method is responsible for binding data to a ViewHolder object that represents an item in the
    list.
     */
    @Override
    public void onBindViewHolder(@NonNull InterestHolder holder, int position) {
        holder.mEventTextView.setText(testList[position].toString());
    }

    /* The third of three methods that need to be overridden when creating an adapter. This
    method is responsible for returning the number of items in the list being displayed.
     */
    @Override
    public int getItemCount() { return testList.length; }
}

