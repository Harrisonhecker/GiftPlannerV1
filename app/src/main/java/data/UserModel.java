package data;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.example.giftplannerv1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import activities.LoginActivity;
import ui.SignupFragment;


public class UserModel extends ViewModel {

    private static final String TAG = "UserModel";
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference usersCollectionReference;

    private FirebaseAuth mAuth;

    private String addedDocumentId;
    private String userUID;

    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userPassword;
    private String userBudget;
    private String userUsername;
    private String profilePic;

    private ArrayList<Object> events;

    public UserModel() {

        Log.d(TAG, "View Model Created");
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.enableNetwork();
        usersCollectionReference = firebaseFirestore.collection("Users");
        Log.d(TAG, String.valueOf(usersCollectionReference));
    }


    public void addUser(String email, String password, Map<String, Object> userData) {

        // locally save all the account information
        initInfo(userData);

        // create the user in Firebase
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            usersCollectionReference.document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener((OnSuccessListener<Void>) aVoid -> {
                                        Log.d(TAG, "Account Successfully Created!");
                                        userUID = user.getUid();
                                    })
                                    .addOnFailureListener(new OnFailureListener() {

                                        // Sign in failure
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d(TAG, "Account Creation Failed.");
                                        }
                                    });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        }
                    }
                });

        /*Map<String, Object> user = new HashMap<>();
        user.put("testString", "testValue");
        // Add a new document with a generated ID
        usersCollectionReference
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        addedDocumentId = documentReference.getId();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });*/
    }

    public void getUser() {


        usersCollectionReference.document(userUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Get the data from the document
                        userEmail = document.getString("email");
                        userFirstName = document.getString("first_name");
                        userLastName = document.getString("last_name");
                        userPassword = document.getString("password");
                        userBudget = document.getString("spending_budget");
                        userUsername = document.getString("username");
                        events = (ArrayList<Object>) document.get("events");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "Error getting document: ", task.getException());
                }
            }
        });


        /*usersCollectionReference
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.d(TAG, "isFromCache: " + document.getMetadata().isFromCache());
                                Log.d(TAG, "hasPendingWrite: " + document.getMetadata().hasPendingWrites());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });*/
    }

    public void updateUser(Map<String, Object> updates) {
        usersCollectionReference.document(userUID)
                .update(updates)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "Document successfully updated");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error updating document", e);
                }
            });
    }

    public void deleteUser() {
        usersCollectionReference.document(addedDocumentId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    /* This method signs the user in using their email and password */
    public boolean signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            userUID = user.getUid();
                            Log.d(TAG, "UID: " + userUID);
                            getUser(); //initialize user information
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d(TAG, task.getException().toString());
                        }
                    }
                });
        boolean result = true;
        return result;
    }

    private void initInfo(Map<String, Object> data) {
        userEmail = (String) data.get("email");
        userFirstName = (String) data.get("first_name");
        userLastName = (String) data.get("last_name");
        userPassword = (String) data.get("password");
        userBudget = (String) data.get("spending_budget");
        userUsername = (String) data.get("username");
        profilePic = (String) data.get("profile_picture");
    }

    public String getEmail() {
        return this.userEmail;
    }

    public String getFirstName() {
        return this.userFirstName;
    }

    public String getLastName() {
        return this.userLastName;
    }

    public String getPassword() {
        return this.userPassword;
    }

    public String getBudget() {
        return this.userBudget;
    }

    public String getUsername() {
        return this.userUsername;
    }
    public ArrayList<Object> getEvents() {
        return this.events;
    }
    public String getProfilePic() { return this.profilePic; }
}
