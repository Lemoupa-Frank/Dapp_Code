package com.example.dvote.fabric_gateway.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.dvote.R;
 

public class ScreenSlidePageFragment extends Fragment {
    private static final String ARG_IMAGE_RES_ID = "image_res_id";

    public static ScreenSlidePageFragment newInstance(int imageResId) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE_RES_ID, imageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        Bundle args = getArguments();
        if (args != null) {
            int imageResId = args.getInt(ARG_IMAGE_RES_ID);
            imageView.setImageResource(imageResId);
        }
        return view;
    }
}
