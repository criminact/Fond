package com.noobcode.fond.ViewModel.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.noobcode.fond.Model.CardStack;
import com.noobcode.fond.Model.User;
import com.noobcode.fond.R;
import com.noobcode.fond.View.HomeActivity;
import com.rd.PageIndicatorView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class CardStackAdapter extends RecyclerView.Adapter<CardStackAdapter.ViewHolder> {
    private List<CardStack> items;
    private Context mContext;

    public CardStackAdapter(List<CardStack> items, Context mContext) {
        this.items = items;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CardStackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new CardStackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardStackAdapter.ViewHolder holder, int position) {

        CardStack cardStack = items.get(position);

        Picasso.get().load(items.get(position).getImageUrl()).fit().centerCrop().into(holder.profile);
        holder.name.setText(items.get(position).getName());
        holder.age.setText(items.get(position).getAge()+"");
        holder.gender.setText(items.get(position).getGender());
        holder.location.setText(items.get(position).getLocation());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //open a dialog - which has details of this user, give a back button
                final Dialog d = new Dialog(mContext);
                d.setContentView(R.layout.card_layout);

                ViewPager viewPager = (ViewPager) d.findViewById(R.id.viewpager);
                PageIndicatorView pageIndicatorView = (PageIndicatorView) d.findViewById(R.id.pageIndicatorView);
                TextView name = (TextView) d.findViewById(R.id.nameindialog);
                TextView age = (TextView) d.findViewById(R.id.ageindialog);
                TextView gender = (TextView) d.findViewById(R.id.genderindialog);
                TextView lookinfor = (TextView) d.findViewById(R.id.lookingforindialog);
                TextView interestedin = (TextView) d.findViewById(R.id.interestedindialog);
                TextView bio = (TextView) d.findViewById(R.id.bioindialog);

                //set images

                setImages(pageIndicatorView, viewPager, cardStack);

                //set textfields

                name.setText(cardStack.getName());
                age.setText(String.valueOf(cardStack.getAge()));
                gender.setText(cardStack.getGender());
                lookinfor.setText("Looking for a "+cardStack.getLookingfor());
                interestedin.setText("Interested in "+cardStack.getInterestedin());
                bio.setText(cardStack.getBio());

                d.setCancelable(true);
                d.show();
            }
        });
    }

    private void setImages(PageIndicatorView pageIndicatorView, ViewPager viewPager, CardStack user) {
        pageIndicatorView.setCount(user.getNumberOfImages()); // specify total count of indicators
        pageIndicatorView.setSelection(1);

        String url = user.getImageUris();
        String[] arr = url.split("@", user.getNumberOfImages());
        viewPager.setOffscreenPageLimit(user.getNumberOfImages());
        ImageAdapter imageAdapter = new ImageAdapter(mContext, arr);
        viewPager.setAdapter(imageAdapter);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profile;
        public TextView name;
        public TextView age;
        public TextView gender;
        public TextView location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            location = itemView.findViewById(R.id.locationFieldText);
            profile = itemView.findViewById(R.id.profileImage);
            name = itemView.findViewById(R.id.nameFieldText);
            age = itemView.findViewById(R.id.ageFieldText);
            gender = itemView.findViewById(R.id.genderFieldText);

        }
    }


    public List<CardStack> getItems() {
        return items;
    }

    public void setItems(List<CardStack> items) {
        this.items = items;
    }
}
