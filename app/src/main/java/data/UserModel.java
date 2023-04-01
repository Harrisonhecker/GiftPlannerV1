package data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;


public class UserModel extends ViewModel {

    private static final String TAG = "UserModel";
    private FirebaseFirestore firebaseFirestore;
    private CollectionReference usersCollectionReference;

    private FirebaseAuth mAuth;

    private String userUID;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userPassword;
    private String userBudget;
    private String userUsername;
    private String profilePic;
    private MutableLiveData<ArrayList<Object>> events;


    public UserModel() {

        Log.d(TAG, "View Model Created");
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.enableNetwork();
        usersCollectionReference = firebaseFirestore.collection("Users");
        Log.d(TAG, String.valueOf(usersCollectionReference));

        events = new MutableLiveData<>();
    }

    /* Add a user to the database */
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
    }

    /* Get a user's information */
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

                        events.setValue((ArrayList<Object>) document.get("events"));
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "Error getting document: ", task.getException());
                }
            }
        });
    }

    /* Update a user's information. This method takes a map of all the key value pairs to be
    updated in the database. */
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

    public void updateEventsArray(Map<String, Object> newEvent) {
        usersCollectionReference.document(userUID).update("events", FieldValue.arrayUnion(newEvent))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Item added to array successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding item to array", e);
                    }
                });
    }

    /* Delete a user from the database */
    public void deleteUser() {
        usersCollectionReference.document("")
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

    /* This method is called when the user creates an account. It initializes local variables of
    all the account information */
    private void initInfo(Map<String, Object> data) {
        userEmail = (String) data.get("email");
        userFirstName = (String) data.get("first_name");
        userLastName = (String) data.get("last_name");
        userPassword = (String) data.get("password");
        userBudget = (String) data.get("spending_budget");
        userUsername = (String) data.get("username");
        profilePic = (String) data.get("profile_picture");
    }

    /* Getter method for user email */
    public String getEmail() {
        return this.userEmail;
    }

    /* Getter method for user first name */
    public String getFirstName() {
        return this.userFirstName;
    }

    /* Getter method for user last name */
    public String getLastName() {
        return this.userLastName;
    }

    /* Getter method for user password */
    public String getPassword() {
        return this.userPassword;
    }

    /* Getter method for user budget */
    public String getBudget() {
        return this.userBudget;
    }

    /* Getter method for user username */
    public String getUsername() {
        return this.userUsername;
    }

    /* Getter method for events data (MutableLiveData) */
    public MutableLiveData<ArrayList<Object>> getEvents() {
        return events;
    }

    /* Getter method for profile picture information. Not currently in use */
    public String getProfilePic() { return this.profilePic; }
}
