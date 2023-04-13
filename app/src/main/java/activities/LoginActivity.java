package activities;

import android.os.Bundle;

import com.example.giftplannerv1.R;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giftplannerv1.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import adapters.EventAdapter;
import data.UserModel;
import ui.EditProfileFragment;

public class LoginActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    public UserModel userModel;
    private String TAG = "MainActivity";

    private RecyclerView eventRecyclerView;
    private EventAdapter eventAdapter;
    private LinearLayoutManager eventLayoutManager;
    private String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate Called");
        Log.d(TAG, "just before call to get ViewModel");

        if (savedInstanceState != null) {
            for (String key : savedInstanceState.keySet()) {
                Log.d(TAG, "Key: " + key + ", Value: " + savedInstanceState.get(key));
            }
            if (savedInstanceState.containsKey("email")) {
                if (savedInstanceState.containsKey("password")) {
                    Log.d(TAG, "EMAIL/PW SAVED");
                    userModel = new UserModel();
                    userModel.signIn(savedInstanceState.getString("email"), savedInstanceState.getString("password"));
                    //userModel.getUser(savedInstanceState.getString("userUID"));
                    userModel.setEvents((ArrayList<Object>) savedInstanceState.getSerializable("events"));
                    userModel.setMembers((ArrayList<Object>) savedInstanceState.getSerializable("members"));
                    userModel.setGifts((ArrayList<Object>) savedInstanceState.getSerializable("gifts"));
                    Log.d(TAG, "Updated Members: " + String.valueOf(userModel.getMembers().getValue()));
                    if (savedInstanceState.containsKey("currentEvent")) {
                        userModel.currentEvent = (Map<String, Object>) savedInstanceState.getSerializable("currentEvent");
                    }

                    if (savedInstanceState.containsKey("currentMember")) {
                        userModel.currentMember = (Map<String, Object>) savedInstanceState.getSerializable("currentMember");
                    }

                    if (savedInstanceState.containsKey("currentGift")) {
                        userModel.currentGift = (Map<String, Object>) savedInstanceState.getSerializable("currentGift");
                    }

                }
            }
        } else {

            Log.d(TAG, "savedInstanceState was null");
            userModel = new UserModel();
        }
/*
        if (savedInstanceState != null) {
            for (String key : savedInstanceState.keySet()) {
                Log.d(TAG, "Key: " + key + ", Value: " + savedInstanceState.get(key));
            }
        }

 */
        //userModel = new ViewModelProvider(this).get(UserModel.class);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
    /*
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart called");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }
     */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceStateCalled");
        outState.putString("userUID", userModel.getUserUID());
        outState.putString("email", userModel.getEmail());
        outState.putString("password", userModel.getPassword());
        outState.putSerializable("events", (Serializable) userModel.getEvents().getValue());
        outState.putSerializable("members", (Serializable) userModel.getMembers().getValue());
        Log.d(TAG, "Members: " + userModel.getMembers().getValue());
        outState.putSerializable("gifts", (Serializable) userModel.getGifts().getValue());

        if (userModel.currentEvent != null) {
            outState.putSerializable("currentEvent", (Serializable) userModel.currentEvent);
        }
        if (userModel.currentMember != null) {
            outState.putSerializable("currentMember", (Serializable) userModel.currentMember);
        }
        if (userModel.currentGift != null) {
            outState.putSerializable("currentGift", (Serializable) userModel.currentGift);
        }

        //Log.d(TAG, userModel.getUserUID());

    }

    /*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceStateCalled");
        Log.d(TAG, "Email: " + savedInstanceState.getString("email"));
        this.userModel.signIn(savedInstanceState.getString("email"), savedInstanceState.getString("password"));
        Log.d(TAG, String.valueOf(this.userModel.getEmail() == null));
        if (savedInstanceState.containsKey("eventName")) {
            userModel.getEvent(savedInstanceState.getString("eventName"));
        }

    }


     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}