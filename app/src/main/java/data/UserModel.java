package data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.fragment.NavHostFragment;

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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ui.EventsFragment;


public class UserModel extends ViewModel {

    private static final String TAG = "UserModel";
    public FirebaseFirestore firebaseFirestore;
    public CollectionReference usersCollectionReference;

    public FirebaseAuth mAuth;

    private String userUID;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userPassword;
    private String userBudget;
    private String userUsername;
    private String profilePic;
    public MutableLiveData<ArrayList<Object>> events;

    public MutableLiveData<ArrayList<Object>> members;

    public MutableLiveData<ArrayList<Object>> gifts;
    public Map<String,Object> currentEvent;

    public Map<String,Object> currentMember;

    public Map<String,Object> currentGift;


    public UserModel() {

        Log.d(TAG, "View Model Created");
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.enableNetwork();
        usersCollectionReference = firebaseFirestore.collection("Users");

        events = new MutableLiveData<>();
        members = new MutableLiveData<>();
        gifts = new MutableLiveData<>();
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

        //get the user from the database
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

                        String returnType = document.get("events").getClass().getSimpleName();

                        //document.get("events") returns a list if non-empty and a map if empty
                        if (returnType.equals("ArrayList")) {
                            events.setValue((ArrayList<Object>) document.get("events"));
                        } else {
                            events.setValue(new ArrayList<Object>());
                        }

                        //initialize the members and gifts arrays
                        members.setValue(new ArrayList<>());
                        gifts.setValue(new ArrayList<>());

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

    /* Update the users event array. */
    public void updateEventsArray(Map<String, Object> newEvent) {
        usersCollectionReference.document(userUID).update("events", FieldValue.arrayUnion(newEvent))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Item added to array successfully!");
                        // need to add code to navigate back to events page
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding item to array", e);
                    }
                });
    }

    /* Update the user's members array for specific event. This method makes the necessary
    changes to an event's members array and then updates the entire events array for a user.
     */
    public void updateMembersArray() {

        // iterate over all events
        for(int i = 0; i < events.getValue().size();i++) {
            Map<String, Object> event = (Map<String, Object>)
                    events.getValue().get(i);

            // find event inside the events array and replace its members array
            if (event.get("name") == currentEvent.get("name")) {
                event.replace("members", members.getValue());
            }
        }

        //update the entire events array...not ideal but it works
        usersCollectionReference.document(userUID).update("events", events.getValue())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Events field updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating events field", e);
                    }
                });
    }

    /* Update the user's gifts array for specific event and member. This method makes the necessary
    changes to an event's member's gifts array and then updates the entire events array for a user.
     */
    public void updateGiftsArray() {

        // iterate over all events
        for(int i = 0; i < events.getValue().size();i++) {
            Map<String, Object> event = (Map<String, Object>)
                    events.getValue().get(i);

            // find event inside the events array
            if (event.get("name") == currentEvent.get("name")) {

                // iterate over all members
                for(int j = 0; j < members.getValue().size();j++) {
                    Map<String, Object> member = (Map<String, Object>)
                            members.getValue().get(j);

                    //find member and update gifts array
                    if (member.get("name") == currentMember.get("name")) {
                        member.replace("gifts", gifts.getValue());
                    }
                }
            }
        }

        //update entire events array...not ideal but it works
        usersCollectionReference.document(userUID).update("events", events.getValue())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Events field updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating events field", e);
                    }
                });

    }

    /* Delete a gift from the local gifts array and then update the database */
    public void deleteGift() {

        //remove gift from local gift array
        gifts.getValue().remove(currentGift);

        //set currentGift to empty
        currentGift = new HashMap<>();

        //update firebase
        updateGiftsArray();
    }

    /* Delete a member from the local members array and then update the database */
    public void deleteMember() {
        //remove gift from local gift array
        members.getValue().remove(currentMember);

        //set currentMember to empty
        currentMember = new HashMap<>();

        //update firebase
        updateMembersArray();
    }

    /* Delete an event from the local events array and then update the database */
    public void deleteEvent() {
        //remove gift from local gift array
        events.getValue().remove(currentEvent);

        //set currentMember to empty
        currentEvent = new HashMap<>();

        //update firebase
        usersCollectionReference.document(userUID).update("events", events.getValue())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Events field updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error updating events field", e);
                    }
                });
    }


    /* Delete a user from the database */
    public void deleteUser() {
        usersCollectionReference.document(userUID)
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

    public void initInfo(Map<String, Object> data) {


        // initialize account information
        userEmail = (String) data.get("email");
        userFirstName = (String) data.get("first_name");
        userLastName = (String) data.get("last_name");
        userPassword = (String) data.get("password");
        userBudget = (String) data.get("spending_budget");
        userUsername = (String) data.get("username");
        profilePic = (String) data.get("profile_picture");

        // initialize events, members, and gifts arrays
        events.setValue(new ArrayList<Object>());
        members.setValue(new ArrayList<Object>());
        gifts.setValue(new ArrayList<Object>());
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

    /* Getter method for members data (MutableLiveData) */
    public MutableLiveData<ArrayList<Object>> getMembers() {
        return members;
    }

    /* Getter method for gifts data (MutableLiveData) */
    public MutableLiveData<ArrayList<Object>> getGifts() {
        return gifts;
    }

    /* Get the event a user clicks on. Also initialize the members for that event */
    public void getEvent(String name){

        // iterate over events
        for(int i = 0; i < events.getValue().size();i++){
            Map<String, Object> event = (Map<String, Object>)
                    events.getValue().get(i);
            String eventName = String.valueOf(event.get("name"));

            //when we find event we're looking for, set currentEvent and initialize members array
            if(eventName.equals(name)){
                currentEvent = (Map<String, Object>) events.getValue().get(i);
                members.setValue((ArrayList<Object>) currentEvent.get("members"));
                if (members.getValue() == null) {
                    members.setValue(new ArrayList<>());
                }
            }
        }
    }

    /* Get the member a user clicks on. Also initialize the gifts for that member */
    public void getMember(String name){

        // iterate over members
        for(int i = 0; i < members.getValue().size();i++){
            Map<String, Object> member = (Map<String, Object>)
                    members.getValue().get(i);
            String memberName = String.valueOf(member.get("name"));

            // when we find member we're looking for, set currentMember and initialize gifts array
            if(memberName.equals(name)){
                currentMember = (Map<String, Object>) members.getValue().get(i);
                gifts.setValue((ArrayList<Object>) currentMember.get("gifts"));
            }
        }
    }

    /* Get the gift a user clicks on. */
    public void getGift(String name){

        // iterate over gifts
        for(int i = 0; i < gifts.getValue().size();i++){
            Map<String, Object> gift = (Map<String, Object>)
                    gifts.getValue().get(i);
            String giftName = String.valueOf(gift.get("name"));

            // when we find gift we're looking for, set currentGift
            if(giftName.equals(name)){
                currentGift = (Map<String, Object>) gifts.getValue().get(i);
            }
        }
    }

    /* Getter method for profile picture information. Not currently in use */
    public String getProfilePic() { return this.profilePic; }

    public UserModel(String test) {
        events = new MutableLiveData<>();
        members = new MutableLiveData<>();
        gifts = new MutableLiveData<>();
    }
    public void initInfoTest(Map<String, Object> data) {


        // initialize account information
        userEmail = (String) data.get("email");
        userFirstName = (String) data.get("first_name");
        userLastName = (String) data.get("last_name");
        userPassword = (String) data.get("password");
        userBudget = (String) data.get("spending_budget");
        userUsername = (String) data.get("username");
        profilePic = (String) data.get("profile_picture");
    }



}
