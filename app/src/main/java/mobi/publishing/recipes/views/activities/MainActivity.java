package mobi.publishing.recipes.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiImage;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.Crop;
import mobi.publishing.recipes.R;
import mobi.publishing.recipes.adapters.CookbooksAdapter;
import mobi.publishing.recipes.helpers.RecognizeImage;
import mobi.publishing.recipes.models.database.CookbookModel;
import mobi.publishing.recipes.views.fragments.CookbooksFragment;
import mobi.publishing.recipes.views.fragments.DiscoverFragment;
import mobi.publishing.recipes.views.fragments.FavouritesFragment;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {
                    case R.id.action_cookbooks:
                        fragment = new CookbooksFragment();
                        break;
                    case R.id.action_discover:
                        fragment = new DiscoverFragment();
                        break;
                    case R.id.action_favourites:
                        fragment = new FavouritesFragment();
                        break;
                    default:
                        fragment = new CookbooksFragment();
                        break;
                }

                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, fragment, fragment.toString())
                        .addToBackStack(null)
                        .commit();

                return true;
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.containsKey("position")) {
                int position = extras.getInt("position");
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.action_cookbooks);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.action_discover);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.action_favourites);
                        break;
                    default:
                        bottomNavigationView.setSelectedItemId(R.id.action_cookbooks);
                        break;
                }
                return;
            }
        }

        bottomNavigationView.setSelectedItemId(R.id.action_cookbooks);
    }
}
