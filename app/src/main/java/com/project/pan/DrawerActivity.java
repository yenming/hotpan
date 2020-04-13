package com.project.pan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.project.pan.R;
import com.project.pan.ui.settings.SettingsFragment;
import com.project.pan.ui.viewpager.RecipeSaver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;

public class DrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavController navHomeController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        EventBus.getDefault().register(this);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_guide, R.id.nav_helps)
                .setDrawerLayout(drawer)
                .build();

        navHomeController = Navigation.findNavController(this, R.id.nav_home_fragment);
        NavigationUI.setupActionBarWithNavController(this, navHomeController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navHomeController);

        View getHeader = navigationView.getHeaderView(0);
        ImageButton menuButton = getHeader.findViewById(R.id.header_menu_btn);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
            }
        });

        ImageButton setButton = findViewById(R.id.pan_setting);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navHomeController.navigate(R.id.nav_settings);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_home_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Subscribe
    public void onEvent(Bundle getBundle){
        if(getBundle.getBoolean("recipe")){
            navHomeController.navigate(R.id.nav_recipe, getBundle);
            Log.d("===Drawer===", "get recipe bundle: "+getBundle.getInt("recipe_img")+" / "+getBundle.getString("recipe_text"));
        } else {
            Log.d("===Drawer===", "no recipe bundle");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
