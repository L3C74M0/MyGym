package com.example.proyectofinalapps.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinalapps.activities.Notifications;
import com.example.proyectofinalapps.databinding.FragmentHomeRegisteredUserBinding;
import com.example.proyectofinalapps.model.Notification;
import com.example.proyectofinalapps.model.Person;
import com.example.proyectofinalapps.model.Subscription;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.proyectofinalapps.activities.ClientQRCodeActivity;


public class FragmentHomeRegisteredUser extends Fragment {

    private TextView welcomeTitle,actions,membership,cutoffDate;
    private Button generateCode, requestPayment;
    private ImageView QRIcon, cashIcon, goToNotifications;
    private View view;
    private FragmentHomeRegisteredUserBinding binding;

    private Person client;

    public FragmentHomeRegisteredUser() {
        // Required empty public constructor
    }

    public static FragmentHomeRegisteredUser newInstance() {
        FragmentHomeRegisteredUser fragment = new FragmentHomeRegisteredUser();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeRegisteredUserBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        welcomeTitle = binding.welcomeTitle;
        actions = binding.actions;
        membership = binding.membership;
        cutoffDate = binding.cutoffDate;
        goToNotifications = binding.goToNotifications;
        generateCode = binding.generateCode;
        requestPayment = binding.requestPayment;
        QRIcon = binding.QRIcon;
        cashIcon = binding.cashIcon;

        generateCode.setOnClickListener(this::generateQR);
        requestPayment.setOnClickListener(this::requestPayment);
        goToNotifications.setOnClickListener(this::goToNotifications);

        chargeData();

        return view;
    }

    private void generateQR(View view){
        Intent intent = new Intent(getActivity(), ClientQRCodeActivity.class);
        intent.putExtra("client", client);
        startActivity(intent);
    }

    private void requestPayment(View view){
        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("Clientes").document(auth.getUid()).collection("Subscription").get().addOnSuccessListener(
                task -> {
                    for(DocumentSnapshot doc: task.getDocuments()) {
                        Subscription sub = doc.toObject(Subscription.class);

                        if (sub.isActive() == true){
                            Log.e(">>>odioesto",""+sub.isActive());

                        } else {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                                    .setTitle("Realizar pago")
                                    .setMessage("¿Esta seguro que desea realizar el pago?")
                                    .setNegativeButton("NO", (dialog, id) -> {
                                        dialog.dismiss();
                                    })
                                    .setPositiveButton("SI", (dialog, id) -> {
                                        successPayment();
                                        dialog.dismiss();
                                    });
                            builder.show();
                        }
                    }
                }
        );
    }

    private void successPayment () {
        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseFirestore.getInstance().collection("Clientes").document(auth.getUid()).get().addOnSuccessListener(
                per -> {
                    Person myperson = per.toObject(Person.class);
                    Notification noti = new Notification(myperson.getFullName(), "pendiente", myperson.getId());

                    //send user to Payments collection
                    FirebaseFirestore.getInstance().collection("Payments").document(myperson.getId()).set(noti).addOnSuccessListener(
                            added -> {
                                Toast.makeText(getActivity(), "Esperando confirmación de staff", Toast.LENGTH_LONG);
                            }
                    );
                }
        );
    }

    private void goToNotifications(View view){
        Intent intent = new Intent(getActivity(), Notifications.class);
        intent.putExtra("rol","Client");
        getActivity().startActivity(intent);
    }

    public Person getClient() {
        return client;
    }

    public void setClient(Person client) {
        this.client = client;
    }

    private void chargeData() {
        //String[] fullName = client.getFullName().toUpperCase(Locale.ROOT).split(" ");

        //welcomeTitle.setText("BIENVENIDO " + fullName[0] + "!");
        membership.setText(" - ");
        cutoffDate.setText(" - ");

    }
}
