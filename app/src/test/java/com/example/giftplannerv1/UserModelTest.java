package com.example.giftplannerv1;
import org.junit.Test;
import static org.junit.Assert.*;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import data.UserModel;

public class UserModelTest {
    UserModel userModel;
    @Test
    public void constructorTest(){
        userModel = new UserModel("Test");
        assertNotNull(userModel.events);
        assertNotNull(userModel.gifts);
        assertNotNull(userModel.members);
    }

    @Test
    public void initializeInformationTest(){
        userModel = new UserModel("Test");
        Map<String, Object> data = new HashMap<>();
        data.put("email", "johndoe@gmail.com");
        data.put("first_name", "John");
        data.put("last_name", "Doe");
        data.put("password", "password123");
        data.put("spending_budget", "1000");
        data.put("username", "johndoe");
        data.put("profile_picture", "https://example.com/profile.jpg");

        userModel.initInfo(data);

        assertEquals("johndoe@gmail.com", userModel.getEmail());
        assertEquals("John", userModel.getFirstName());
        assertEquals("Doe", userModel.getLastName());
        assertEquals("password123", userModel.getPassword());
        assertEquals("1000", userModel.getBudget());
        assertEquals("johndoe", userModel.getUsername());
        assertEquals("https://example.com/profile.jpg", userModel.getProfilePic());
    }

    @Test
    public void testGetGift() {
        UserModel userModel = new UserModel("test");
        ArrayList<Object> giftsList = new ArrayList<>();
        Map<String, Object> gift1 = new HashMap<>();
        gift1.put("name", "Gift 1");
        giftsList.add(gift1);
        Map<String, Object> gift2 = new HashMap<>();
        gift2.put("name", "Gift 2");
        giftsList.add(gift2);
        Map<String, Object> gift3 = new HashMap<>();
        gift3.put("name", "Gift 3");
        giftsList.add(gift3);
        userModel.gifts = new MutableLiveData<>(giftsList);

        userModel.getGift("Gift 2");

        Map<String, Object> currentGift = userModel.currentGift;

        assertNotNull(currentGift);
        assertEquals("Gift 2", currentGift.get("name"));
    }
}
