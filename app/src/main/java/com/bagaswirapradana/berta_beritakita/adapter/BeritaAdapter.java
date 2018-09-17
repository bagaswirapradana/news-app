package com.bagaswirapradana.berta_beritakita.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bagaswirapradana.berta_beritakita.R;
import com.bagaswirapradana.berta_beritakita.activity.DetailBeritaActivity;
import com.bagaswirapradana.berta_beritakita.penampung.Berita;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.MyViewHolder> {

    private List<Berita> beritaList;
    private Activity activity;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_news, parent, false);

        return new MyViewHolder(itemView);
    }

    public BeritaAdapter(List<Berita> beritaList, Activity activity) {
        this.beritaList = beritaList;
        this.activity = activity;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Berita berita =  beritaList.get(position);
        holder.judul.setText(berita.getTitle());
        holder.deskripsi.setText(berita.getDescription());
        holder.sumber.setText(berita.getAuthor());

        //Konversi waktu
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault());
        String formattedDate = null;
        Date convertedDate = new Date();

        try {

            convertedDate = dateFormat.parse(berita.getPublishedAt());
            formattedDate = targetFormat.format(convertedDate);

            holder.waktu.setText(formattedDate);
        } catch (ParseException e) {

            e.printStackTrace();

        }

        Picasso.with(activity)
                .load(berita.getUrlToImage())
                .error(R.drawable.image_not_available)
                .placeholder(R.drawable.pictures)
                .resize(300, 200)
                .into(holder.gambarBerita);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, DetailBeritaActivity.class);
                i.putExtra("url", berita.getUrl());
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return beritaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView judul, deskripsi, waktu, sumber;
        ImageView gambarBerita;
        LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            judul = view.findViewById(R.id.title);
            deskripsi = view.findViewById(R.id.description);
            sumber = view.findViewById(R.id.author);
            waktu = view.findViewById(R.id.time);
            gambarBerita = view.findViewById(R.id.gambarBerita);
            linearLayout = view.findViewById(R.id.layout_berita);
        }
    }
}
