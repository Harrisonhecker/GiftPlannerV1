package data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;


public class TestModel extends ViewModel {

    private static final String TAG = "MyViewModel";
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference usersCollectionReference;

    public TestModel() {
        Log.d(TAG, "View Model Created");
        firebaseFirestore = FirebaseFirestore.getInstance();
        usersCollectionReference = firebaseFirestore.collection("Users");
        Log.d(TAG, String.valueOf(usersCollectionReference));
    }


    public void addData() {
        Log.d(TAG, "addData() called");
    }

    public void getData() {
        usersCollectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    public void updateData() {

    }

    public void deleteData() {

    }
}
