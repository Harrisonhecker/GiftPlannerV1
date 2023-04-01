package data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import data.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class EventModel extends ViewModel {

    private static final String TAG = "EventModel";

    public EventModel() {
        //Pass in userName as variable here

        UserModel uModel = new UserModel();
        uModel.getData("Example User");
        Log.d(TAG, String.valueOf(uModel.result));
        //Log.d(TAG, document.getId() + " => " + document.getData());
        //Log.d(TAG, "isFromCache: " + document.getMetadata().isFromCache());
        //Log.d(TAG, "hasPendingWrite: " + document.getMetadata().hasPendingWrites());
        //userData = uModel.getData(user);
    }


    public void addData() {

    }

    public void getData() {

    }

    public void updateData() {

    }

    public void deleteData() {

    }
}
