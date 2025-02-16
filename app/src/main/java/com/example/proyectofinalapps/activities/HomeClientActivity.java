package com.example.proyectofinalapps.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.proyectofinalapps.R;
import com.example.proyectofinalapps.fragments.ConfigFragment;
import com.example.proyectofinalapps.fragments.FragmentHomeRegisteredUser;
import com.example.proyectofinalapps.fragments.ProfileClientFragment;
import com.example.proyectofinalapps.model.Person;
import com.example.proyectofinalapps.model.Subscription;
import com.example.proyectofinalapps.model.User;
import com.example.proyectofinalapps.databinding.ActivityHomeClientBinding;
import com.example.proyectofinalapps.fragments.HomeClientFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class HomeClientActivity extends AppCompatActivity implements HomeClientFragment.OnIsActiveClientListener {

    private ActivityHomeClientBinding binding;
    private BottomNavigationView navigatorClient;

    private HomeClientFragment homeClientFragment;
    private ConfigFragment configFragment;
    private ProfileClientFragment profileClientFragment;
    private FragmentHomeRegisteredUser homeRegisteredUserFragment;

    private User user;
    private Person client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeClientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navigatorClient = binding.navigatorClient;

        //Load User from SP
        user = loadUser();
        if(user == null || FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            this.user = user;
        }

        homeClientFragment = HomeClientFragment.newInstance();
        configFragment = ConfigFragment.newInstance();
        profileClientFragment = ProfileClientFragment.newInstance();
        homeRegisteredUserFragment = FragmentHomeRegisteredUser.newInstance();
        homeClientFragment.setListener(this);

        FirebaseFirestore.getInstance().collection("Clientes").document(user.getId()).get().addOnSuccessListener(
                task -> {
                    client = task.toObject(Person.class);
                    Log.e(">>>", "cliente: " + client.getId() + " " + client.getIsActive());
                    if(client.getIsActive().equals("Y")) {
                        homeRegisteredUserFragment.setClient(client);
                        showFragment(homeRegisteredUserFragment);
                    } else if(client.getIsActive().equals("N")) {
                        homeClientFragment.setPerson(client);
                        showFragment(homeClientFragment);
                    }
                }
        );

        navigatorClient.setOnItemSelectedListener(
                menuItem -> {
                    if (menuItem.getItemId() == R.id.configClientMenu){
                        showFragment(configFragment);
                    }else if (menuItem.getItemId() == R.id.homeClientMenu){
                        //TODO Condicional que me permita saber si ya está registrado en un gimnasio
                        //Sino está registrado muestre este fragmento
                        if(client.getIsActive().equals("Y")) {
                            showFragment(homeRegisteredUserFragment);
                        } else if(client.getIsActive().equals("N")) {
                            showFragment(homeClientFragment);
                        }

                        //Si esta registrado que muestre este otro fragmento
                    }else if(menuItem.getItemId() == R.id.profileClientMenu){
                        profileClientFragment.setPerson(client);
                        FirebaseFirestore.getInstance().collection("Clientes").document(client.getId()).collection("Subscription")
                                .get().addOnSuccessListener(
                                        task -> {
                                            Subscription subscription = null;
                                            for(DocumentSnapshot doc: task.getDocuments()) {
                                                subscription = doc.toObject(Subscription.class);
                                                break;
                                            }
                                            profileClientFragment.setSubscription(subscription);
                                            showFragment(profileClientFragment);
                                        }
                                );
                    }
                    return true;
                }
        );
        
    }

    private User loadUser() {
        String json = getSharedPreferences("data", MODE_PRIVATE).getString("user", "NO_USER");
        if(json.equals("NO_USER")){
            return null;
        }else{
            return new Gson().fromJson(json, User.class);
        }
    }

    public void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentClientContainer, fragment);
        transaction.commit();
    }

    @Override
    public void onIsActiveClient() {
        showFragment(homeRegisteredUserFragment);
    }
}