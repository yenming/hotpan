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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
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

import static androidx.navigation.ui.NavigationUI.setupWithNavController;

public class DrawerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavController navHomeController;
    private NavGraph navGraph;
    NavArgument temperatureArg;
    private Bundle mTemperature;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        if(this.getIntent().getExtras() != null){
            mTemperature = this.getIntent().getExtras();
            temperatureArg = new NavArgument.Builder().setDefaultValue(mTemperature.getInt("set_temperature")).build();
            Log.d("===Drawer===", "get temperature bundle: "+ mTemperature.getInt("set_temperature")+" / "+ temperatureArg.getDefaultValue());
        }

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_guide, R.id.nav_helps)
                .setDrawerLayout(drawer)
                .build();
        navHomeController = Navigation.findNavController(this, R.id.nav_home_fragment);
        navGraph = navHomeController.getNavInflater().inflate(R.navigation.home_navigation);
        navGraph.addArgument("set_temperature", temperatureArg);
        navHomeController.setGraph(navGraph);
        NavigationUI.setupActionBarWithNavController(this, navHomeController, mAppBarConfiguration);
        setupWithNavController(navigationView, navHomeController);

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
                navHomeController.navigate(R.id.nav_settings, mTemperature);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // Now is empty
        getMenuInflater().inflate(R.menu.menu, menu);
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
        for (String key: getBundle.keySet()) {
            switch (key){
                case "recipe":
                    navHomeController.navigate(R.id.nav_recipe, getBundle);
                    Log.d("===Drawer===", "get recipe bundle: "+key+" / "+getBundle.getInt("recipe_img")+" / "+getBundle.getString("recipe_text"));
                    break;
                case "set_temperature":
                    Log.d("===Drawer===", "get temperature bundle: "+key+" / "+getBundle.getInt("set_temperature"));
                    break;
                default:
                    Log.d("===Drawer===", "get bundle: null");
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
