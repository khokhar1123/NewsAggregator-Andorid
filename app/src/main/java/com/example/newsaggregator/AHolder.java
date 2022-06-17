package com.example.newsaggregator;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AHolder extends RecyclerView.ViewHolder{
    TextView artpager;
    TextView artdate;
    TextView artheading;
    TextView artData;
    TextView artSource;
    ImageView artIMG;

    public AHolder(@NonNull View itemView) {
        super(itemView);
        artpager = itemView.findViewById(R.id.pageno);
        artdate = itemView.findViewById(R.id.date);
        artheading = itemView.findViewById(R.id.heading);
        artData = itemView.findViewById(R.id.artData);
        artSource = itemView.findViewById(R.id.artSource);
        artIMG = itemView.findViewById(R.id.artIMG);
    }

}
