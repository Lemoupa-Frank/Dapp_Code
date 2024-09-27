package com.example.dvote.fabric_gateway.adapters;


import static com.example.dvote.fabric_gateway.data.ImageUtils.byteObjectArrayToBitmap;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dvote.R;
import com.example.dvote.fabric_gateway.models.Candidate;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class create_candidate_adapter extends RecyclerView.Adapter<create_candidate_adapter.ViewHolder> {
    private OnImageSelectedListener onImageSelectedListener;
    public ArrayList<Candidate> localDataSet;

    public Uri image_uri;
    private static int click_item = 0;

    View itemView;

    public ArrayList<Candidate> getLocalDataSet() {
        return localDataSet;
    }

    boolean candidate_exist = false; // if there are candidates the adapter was called in update activity

    public void setOnImageSelectedListener(OnImageSelectedListener listener) {
        this.onImageSelectedListener = listener;
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView
     */
    public create_candidate_adapter(ArrayList<Candidate> dataSet) {
        if (dataSet == null) {
            dataSet = new ArrayList<>();
            dataSet.add(new Candidate());
        }
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        this.itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.candidate_creation_item2, viewGroup, false);

        return new ViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        int[] colors = {R.color.primary_blue, R.color.prime_blue, R.color.sky_blue, R.color.purple_500};
        int[] g = {R.drawable.vote1, R.drawable.vote2, R.drawable.vote3, R.drawable.vote4};
        viewHolder.getname().setText(localDataSet.get(position).getCandidateid());
        if (localDataSet.get(position).getImage() != null) {
            viewHolder.getPicture().setImageBitmap(byteObjectArrayToBitmap(localDataSet.get(position).getImage()));
        }

        viewHolder.getPicture().setOnClickListener(v -> {
            if (onImageSelectedListener != null) {
                onImageSelectedListener.onImageSelected(position);
            }
        });
        viewHolder.getNew_candidate().setOnClickListener(

                v -> {
                    if (viewHolder.getname().getText().length() > 0) {
                        localDataSet.get(position).setCandidateid(viewHolder.name.getText().toString());
                        ArrayList<Candidate> cd = localDataSet;
                        cd.add(new Candidate());
                        setLocalDataSet(cd);
                    }
                    else{
                        shakeEditText(viewHolder.getname());
                    }

                }
        );

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet == null ? 0 : localDataSet.size();
    }

    public interface OnImageSelectedListener {
        void onImageSelected(int position);
    }

    public void updateImage(int position, Byte[] bitmap) {
        localDataSet.get(position).setImage(bitmap);
        notifyItemChanged(position);
    }

    public void setLocalDataSet(ArrayList<Candidate> dataSet) {
        this.localDataSet = dataSet;
        notifyDataSetChanged();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText name;
        private ImageView candisate_picture, Organisation, new_candidate;

        public ViewHolder(@NonNull View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            name = view.findViewById(R.id.name);
            candisate_picture = view.findViewById(R.id.imageView2);
            Organisation = view.findViewById(R.id.imageView1);
            new_candidate = view.findViewById(R.id.imageView3);
        }

        public TextView getname() {
            return name;
        }

        public ImageView getOrganisation() {
            return Organisation;
        }

        public ImageView getPicture() {
            return candisate_picture;
        }

        public ImageView getNew_candidate() {
            return new_candidate;
        }


    }

    public int shuffle_list(int[] colors) {
        List<Integer> colorList = new ArrayList<>();
        for (int color : colors) {
            colorList.add(color);
        }
        Collections.shuffle(colorList);
        for (int i = 0; i < colors.length; i++) {
            colors[i] = colorList.get(i);
        }
        Random random = new Random();
        return random.nextInt(colors.length);
    }

    public int shuffle_list(Drawable[] drawables) {
        List<Drawable> colorList = new ArrayList<>(Arrays.asList(drawables));
        Collections.shuffle(colorList);
        for (int i = 0; i < drawables.length; i++) {
            drawables[i] = colorList.get(i);
        }
        Random random = new Random();
        return random.nextInt(drawables.length);
    }

    private static void shakeEditText(TextView editText) {
        // Calculate the desired translation distance
        int shakeDistance = 50;

        // Create the animation
        Animation shakeAnimation = new TranslateAnimation(0, shakeDistance, 0, 0);
        shakeAnimation.setDuration(100);
        shakeAnimation.setRepeatCount(7);
        shakeAnimation.setRepeatMode(Animation.REVERSE);
        editText.startAnimation(shakeAnimation);
    }

}

