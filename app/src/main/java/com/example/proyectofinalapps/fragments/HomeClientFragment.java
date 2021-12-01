package com.example.proyectofinalapps.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.proyectofinalapps.R;
import com.example.proyectofinalapps.databinding.FragmentHomeClientBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class HomeClientFragment extends Fragment {

    private FragmentHomeClientBinding binding;

    private View view;

    private ImageView codQrImg;

    public HomeClientFragment() {
        // Required empty public constructor
    }

    public static HomeClientFragment newInstance() {
        HomeClientFragment fragment = new HomeClientFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =FragmentHomeClientBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        codQrImg = binding.codQrImg;

        generateQR();


        return view;
    }

    private void generateQR() {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap("Funciona", BarcodeFormat.QR_CODE, 750, 750);

            codQrImg.setImageBitmap(bitmap);


        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
}