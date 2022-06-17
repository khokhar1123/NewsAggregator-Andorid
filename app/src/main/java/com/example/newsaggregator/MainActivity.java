package com.example.newsaggregator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<Source> sList;
    private ArrayList<Source> sList2;
    private ArrayList<Article> aList;
    private HashMap<String, HashSet<Source>> tHash;
    private String tFilter = "all";
    private Menu menu;
    private ViewPager2 viewPager2;
    private boolean alreadyclicked = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       viewPager2 = findViewById(R.id.pager);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doSourcesDownload();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void doSourcesDownload(){
        SourceDownload.downloadSource(this);
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void doArticlesDownload(MainActivity mainActivity, String id) {
        ArticleDownload.downloadArt(mainActivity,id);
    }
    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initializeDrawer() {
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerList = findViewById(R.id.l_drawer);

        ArrayList<String> adapterArray = new ArrayList<>();
        for (Source source : sList2) {
            adapterArray.add(source.name);
        }
        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.dlist_item, adapterArray));

        mDrawerList.setOnItemClickListener(
                (parent, view, position, id) -> sourceSel(position)
        );
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.string.drawer_open,
                R.string.drawer_close
        );
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        mDrawerToggle.syncState();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateSources(ArrayList<Source> sources, HashMap<String, HashSet<Source>> topics){
        if (sources == null) {
            Toast.makeText(this, "Error Occured During Download", Toast.LENGTH_SHORT).show();
            return;
        }
        this.sList = sources;
        this.tHash = topics;
        this.sList2 = new ArrayList<>(sources);
        this.setTitle(String.format(Locale.getDefault(),
                "News Gateway (%d)", sList2.size()));

        initializeMenu();
        initializeDrawer();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);

    }
    private void initializeMenu() {
        ArrayList<String> tList = new ArrayList<>(this.tHash.keySet());
        Collections.sort(tList);
        tList.forEach((key) -> menu.add(key));
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.hasSubMenu() || mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        String filter = (String) item.getTitle();
        if (item.getGroupId()== 0) {
            if (!this.tFilter.equals("all")) {
                sList2 = new ArrayList<>(sList);
            }
            tFilter = filter;
        }
        sList2.retainAll(Objects.requireNonNull(tHash.get(tFilter)));
        ArrayList<String> adapterArray = new ArrayList<>();
        for (Source source : sList2) {
            adapterArray.add(source.name);
        }

        mDrawerList.setAdapter(new ArrayAdapter<>(this,
                R.layout.dlist_item, adapterArray));

        if(alreadyclicked==false) {
            this.setTitle(String.format(Locale.getDefault(),
                    "News Gateway (%d)", sList2.size()));
        }

        return super.onOptionsItemSelected(item);
    }
    public void downloadFailed(){
        Toast.makeText(this, "Error occured during downloading data from the Api", Toast.LENGTH_SHORT).show();
    }

    public void updateArticle(ArrayList<Article> artList) {
        if (artList == null) {
            Toast.makeText(this, "Error occured during downloading data from the Api", Toast.LENGTH_SHORT).show();
            return;
        }
        this.aList = artList;
        ViewPager2 viewPager2 = findViewById(R.id.pager);
        AAdapter articleAdapter = new AAdapter(this, artList);
        viewPager2.setAdapter(articleAdapter);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        articleAdapter.notifyDataSetChanged();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sourceSel(int position) {
        alreadyclicked=true;
        this.mDrawerLayout.setBackground(null);
        Source source = sList2.get(position);
        this.setTitle(source.name);
        mDrawerLayout.closeDrawer(mDrawerList);
        doArticlesDownload(this,source.id);

    }





}