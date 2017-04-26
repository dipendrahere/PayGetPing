package com.example.dipendra.paygetping;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.dipendra.paygetping.fragments.GetFragment;
import com.example.dipendra.paygetping.fragments.PayFragment;
import com.example.dipendra.paygetping.managingFriends.FriendsActivity;
import com.example.dipendra.paygetping.utils.MyFragmentPagerAdapter;
import com.example.dipendra.paygetping.utils.FabClickListener;
import com.example.dipendra.paygetping.wallet.WalletsActivity;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private MyFragmentPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private ListView mDrawerList;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        readSharedPreferences();
        setContentView(R.layout.activity_main);
        initialize();
       // Toast.makeText(this, "Your wallet "+currentList.getName()+" owner "+currentList.getOwner()+" pushId "+currentList.getPushId(), Toast.LENGTH_SHORT).show();
    }
    private void populate(ArrayList<Fragment> arrayList){
        arrayList.add(new PayFragment());
        arrayList.add(new GetFragment());

    }
    private void initialize(){
        fab = (FloatingActionButton) findViewById(R.id.fabs);
        FabClickListener listener = new FabClickListener(this);
        fab.setOnClickListener(listener);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.setScrimColor(getResources().getColor(R.color.backdrawer));
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ArrayList<Fragment> fragmentslist = new ArrayList<Fragment>();
        populate(fragmentslist);
        mSectionsPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentslist);
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void closedrawer(View v){
        onBackPressed();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent i;

        if (id == R.id.nav_camera) {
            i = new Intent(MainActivity.this, WalletsActivity.class);
            startActivity(i);
        }
         else if (id == R.id.nav_share) {
            i = new Intent(MainActivity.this, FriendsActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
