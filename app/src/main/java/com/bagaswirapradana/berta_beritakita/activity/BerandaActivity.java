package com.bagaswirapradana.berta_beritakita.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bagaswirapradana.berta_beritakita.BuildConfig;
import com.bagaswirapradana.berta_beritakita.R;
import com.bagaswirapradana.berta_beritakita.adapter.SumberBeritaAdapter;
import com.bagaswirapradana.berta_beritakita.config.Config;
import com.bagaswirapradana.berta_beritakita.fragment.BeritaFragment;
import com.bagaswirapradana.berta_beritakita.penampung.SumberBerita;
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

public class BerandaActivity extends AppCompatActivity {

    DrawerLayout mDrawer;
    Toolbar toolbar;
    ListView listMenu;
    List<SumberBerita> sumberBeritas = new ArrayList<>();
    SumberBeritaAdapter sumberBeritaAdapter;
    SumberBerita sumberBerita;

    //Status Navigation Drawer
    boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);

        if (savedInstanceState == null) {
            Fragment fragment = new BeritaFragment();
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mDrawer = findViewById(R.id.drawer_layout);

        listMenu = findViewById(R.id.left_drawer);
        getSourceNews();
    }

    private void getSourceNews() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BuildConfig.URL_BASE+"sources?apiKey="+ Config.API_KEY)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        e.printStackTrace();
                        call.cancel();
                        Toast.makeText(getApplicationContext(),"Telah terjadi sesuatu, mohon coba kembali nanti", Toast.LENGTH_SHORT).show();
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
                    try {
                        final JSONObject jsonObject = new JSONObject(json_response);
                        final String status = (String) jsonObject.get("status");

                        if (status.equals("ok")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Gson gson = new Gson();
                                        final String sourceNews = jsonObject.getJSONArray("sources").toString();
                                        sumberBeritas = Arrays.asList(gson.fromJson(sourceNews, SumberBerita[].class));
                                        sumberBeritaAdapter = new SumberBeritaAdapter(BerandaActivity.this,sumberBeritas);
                                        listMenu.setAdapter(sumberBeritaAdapter);
                                        sumberBeritaAdapter.notifyDataSetChanged();
                                        listMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                pilihSumberBerita(position);
                                            }
                                        });

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void pilihSumberBerita(int position) {

        sumberBerita = sumberBeritas.get(position);

        Fragment fragment = new BeritaFragment();
        Bundle sourcesNews = new Bundle();
        sourcesNews.putString("sources", sumberBerita.getId());
        fragment.setArguments(sourcesNews);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        listMenu.setItemChecked(position, true);
        setTitle(sumberBerita.getName());
        mDrawer.closeDrawer(listMenu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search News...");

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cariBerita(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void cariBerita(String query) {

        Fragment fragment = new BeritaFragment();
        Bundle keywords = new Bundle();
        keywords.putString("query", query);
        fragment.setArguments(keywords);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

    }

    @Override
    public void onBackPressed() {

        if (mDrawer.isDrawerVisible(listMenu)) {
            mDrawer.closeDrawer(listMenu);
        }else if(exit){
            moveTaskToBack(true);
        }else {
            Toast.makeText(getApplicationContext(),"Tekan tombol kembali lagi untuk keluar", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2 * 500);
        }
    }
}
