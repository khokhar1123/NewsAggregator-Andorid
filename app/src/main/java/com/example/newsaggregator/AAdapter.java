package com.example.newsaggregator;

import android.content.Intent;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;


public class AAdapter extends RecyclerView.Adapter<AHolder>{

    private final MainActivity mainActivity;
    private final ArrayList<Article> aList;
    Picasso picasso = Picasso.get();
    private String url;
    public AAdapter(MainActivity mainActivity, ArrayList<Article> aList) {
        this.mainActivity = mainActivity;
        this.aList = aList;
    }
    @NonNull
    @Override
    public AHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AHolder(
                LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.pager_article, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AHolder holder, int position) {
        Article art = aList.get(position);
        holder.artheading.setText(art.title);
        holder.artdate.setText(art.published);
        holder.artSource.setText(art.author);
        holder.artData.setText(art.desc);
        holder.artData.setMovementMethod(new ScrollingMovementMethod());
        holder.artpager.setText(String.format(Locale.getDefault(), "%d of %d", position + 1, aList.size()));
        url = art.url;
        holder.artIMG.setOnClickListener(x -> whenClicked());
        holder.artheading.setOnClickListener(x -> whenClicked());
        holder.artData.setOnClickListener(x -> whenClicked());


        loadRemoteImage(art.imageUrl, holder.artIMG);

    }
    @Override
    public int getItemCount() {
        return aList.size();
    }


    private void loadRemoteImage(String imageUrl, ImageView artIMG) {
        if (imageUrl.equals("null")) {
            Picasso.get().load(imageUrl)
                    .error(R.drawable.noimage)
                    .placeholder(R.drawable.noimage)
                    .into(artIMG);

        } else {
            picasso.load(imageUrl)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.loading)
                    .into(artIMG);
        }
    }

    private void whenClicked() {
        Uri urlToUse = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, urlToUse);
        mainActivity.startActivity(intent);
    }
}



