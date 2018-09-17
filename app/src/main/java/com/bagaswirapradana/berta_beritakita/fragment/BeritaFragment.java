package com.bagaswirapradana.berta_beritakita.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bagaswirapradana.berta_beritakita.BuildConfig;
import com.bagaswirapradana.berta_beritakita.config.Config;
import com.bagaswirapradana.berta_beritakita.R;
import com.bagaswirapradana.berta_beritakita.adapter.BeritaAdapter;
import com.bagaswirapradana.berta_beritakita.penampung.Berita;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class BeritaFragment extends Fragment {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    String sumberBerita,keywords;
    List<Berita> beritaList = new ArrayList<>();
    BeritaAdapter beritaAdapter;

    public BeritaFragment() {
        //Constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_berita, container, false);

        recyclerView = v.findViewById(R.id.listNews);
        progressBar = v.findViewById(R.id.loader);
        progressBar.setVisibility(View.INVISIBLE);

        final Bundle sourceNews = getArguments();
        try {
            sumberBerita = sourceNews.getString("sources");
            keywords = sourceNews.getString("query");
        }
        catch(final Exception e){
            // Do nothing
        }

        if (sumberBerita!=null) {
            ambilBerita(sumberBerita);
        }else if (keywords!=null){
            pencarianBerita(keywords);
        }else{
            ambilBeritaHeadlines();
        }
        return v;
    }

    private void ambilBeritaHeadlines() {
        OkHttpClient client = new OkHttpClient();
        progressBar.setVisibility(View.VISIBLE);

        Request request = new Request.Builder()
                .url(BuildConfig.URL_BASE+"top-headlines?country=id&apiKey="+Config.API_KEY)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity().getApplicationContext(),"Telah terjadi sesuatu, mohon coba kembali nanti", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        call.cancel();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Ketika Ada Response
                if (!response.isSuccessful()) {
                    call.cancel();
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String json_response = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            Gson gson = new Gson();
                            try {
                                final JSONObject jsonObject = new JSONObject(json_response);
                                final String status = (String) jsonObject.get("status");
                                if (status.equals("ok")){
                                    final String artikel = jsonObject.getJSONArray("articles").toString();
                                    beritaList = Arrays.asList(gson.fromJson(artikel, Berita[].class));
                                    beritaAdapter = new BeritaAdapter(beritaList,getActivity());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setNestedScrollingEnabled(false);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(beritaAdapter);
                                    beritaAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    private void pencarianBerita(String keywords) {
        progressBar.setVisibility(View.VISIBLE);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BuildConfig.URL_BASE+"everything?q="+keywords+"&apiKey="+Config.API_KEY)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call,final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity().getApplicationContext(),"Telah terjadi sesuatu, mohon coba kembali nanti", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        call.cancel();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    call.cancel();
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String json_response = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            Gson gson = new Gson();
                            try {
                                final JSONObject jsonObject = new JSONObject(json_response);
                                final String status = (String) jsonObject.get("status");
                                if (status.equals("ok")){
                                    final String artikel = jsonObject.getJSONArray("articles").toString();
                                    beritaList = Arrays.asList(gson.fromJson(artikel, Berita[].class));
                                    beritaAdapter = new BeritaAdapter(beritaList,getActivity());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setNestedScrollingEnabled(false);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(beritaAdapter);
                                    beritaAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    }
            }
        });
    }

    private void ambilBerita(String sumberBerita) {
        OkHttpClient client = new OkHttpClient();
        progressBar.setVisibility(View.VISIBLE);

        Request request = new Request.Builder()
                .url(BuildConfig.URL_BASE+"top-headlines?sources="+sumberBerita+"&apiKey="+ Config.API_KEY)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity().getApplicationContext(),"Telah terjadi sesuatu, mohon coba kembali nanti", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        call.cancel();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //Ketika Ada Response
                if (!response.isSuccessful()) {
                    call.cancel();
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String json_response = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.INVISIBLE);
                            Gson gson = new Gson();
                            try {
                                final JSONObject jsonObject = new JSONObject(json_response);
                                final String status = (String) jsonObject.get("status");
                                if (status.equals("ok")){
                                    final String artikel = jsonObject.getJSONArray("articles").toString();
                                    beritaList = Arrays.asList(gson.fromJson(artikel, Berita[].class));
                                    beritaAdapter = new BeritaAdapter(beritaList,getActivity());
                                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                                    recyclerView.setLayoutManager(mLayoutManager);
                                    recyclerView.setNestedScrollingEnabled(false);
                                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                                    recyclerView.setAdapter(beritaAdapter);
                                    beritaAdapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }
}
