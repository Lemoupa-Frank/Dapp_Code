package com.example.dvote.fabric_gateway.adapters;


import static com.example.dvote.fabric_gateway.data.ImageUtils.convertToByteArray;
import static com.example.dvote.fabric_gateway.data.ImageUtils.drawableToBitmap;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.util.LocaleData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dvote.R;
import com.example.dvote.fabric_gateway.models.Candidate;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class candidate_adpater extends RecyclerView.Adapter<candidate_adpater.ViewHolder> {

    private Candidate[] localDataSet;
    Context context;

    private boolean[] checkBoxStates;

    SharedPreferences  sharedPreferences;

    int last_position= -1;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        CheckBox number_of_votes;
        private final MaterialTextView vote;
        private final ImageView candisate_picture;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            name = (TextView) view.findViewById(R.id.name);
            number_of_votes = (CheckBox) view.findViewById(R.id.number_of_votes);
            vote = (MaterialTextView) view.findViewById(R.id.vote);
            candisate_picture = (ImageView)view.findViewById(R.id.imageView2);
            /*vote.setOnClickListener(v->{
                number_of_votes = number_of_votes + 1;
            });*/
        }

        public TextView getname() {
            return name;
        }
        public CheckBox getvotes() {
            return number_of_votes;
        }
        public ImageView getPicture() {
            return candisate_picture;
        }
        public MaterialTextView getVote_button(){
            return vote;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public candidate_adpater(Candidate[] dataSet, Context con, SharedPreferences sharedPreferences) {
        localDataSet = dataSet;
        this.context =  con;
        this.sharedPreferences = sharedPreferences;
        this.checkBoxStates = new boolean[dataSet.length];
        for (Candidate candidate : dataSet) {
            if(candidate.getVotes()!=null)
            {
                if(candidate.getVotes().indexOf(sharedPreferences.getString("userid", "0"))!=1){
                    checkBoxStates[candidate.getVotes().indexOf(sharedPreferences.getString("userid", "0"))] = true;
                    last_position = candidate.getVotes().indexOf(sharedPreferences.getString("userid", "0"));
            }
            }
        }

    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.candidate_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        int[] colors = {R.color.primary_blue, R.color.prime_blue, R.color.sky_blue, R.color.purple_500};
        int[] g = {R.drawable.vote1, R.drawable.vote2, R.drawable.vote3, R.drawable.vote4};
        viewHolder.getname().setText(localDataSet[position].getCandidateid());
        viewHolder.getvotes().setChecked(checkBoxStates[position]);
        viewHolder.getPicture().setImageBitmap(localDataSet[position].getImage() == null ? drawableToBitmap( ResourcesCompat.getDrawable(context.getResources(), R.drawable.baseline_person_24,null)) : BitmapFactory.decodeByteArray(convertToByteArray(localDataSet[position].getImage()), 0, localDataSet[position].getImage().length));
        viewHolder.getVote_button().setOnClickListener(v->{
            if(last_position != -1 )
            {
                checkBoxStates[last_position] = false;
            }
            viewHolder.getvotes().setChecked(!viewHolder.getvotes().isChecked());
            notifyItemChanged(last_position);
            setLast_position(position);

        });
        //viewHolder.getvotes().setText(String.valueOf(localDataSet[position].getVotes() == null ? "0" : (localDataSet[position].getVotes().size() )));
        //viewHolder.getVote_button().setTextColor(shuffle_list(colors));
        //viewHolder.getPicture().setImageBitmap(BitmapFactory.decodeByteArray(localDataSet[position].getImage(), 0, localDataSet[position].getImage().length));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }

    public int shuffle_list(int[] colors)
    {
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
    public int shuffle_list(Drawable[] drawables)
    {
        List<Drawable> colorList = new ArrayList<>(Arrays.asList(drawables));
        Collections.shuffle(colorList);
        for (int i = 0; i < drawables.length; i++) {
            drawables[i] = colorList.get(i);
        }
        Random random = new Random();
        return random.nextInt(drawables.length);
    }

    public void setLast_position(int pos)
    {
        last_position = pos;
    }

    public int getLast_position()
    {
        return last_position;
    }

    public Candidate[] getLocalDataSet()
    {
        return localDataSet;
    }
}

