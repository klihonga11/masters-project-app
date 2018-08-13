package mobi.publishing.recipes.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import mobi.publishing.recipes.R;
import mobi.publishing.recipes.adapters.CookbooksAdapter;
import mobi.publishing.recipes.adapters.RecipeCategoriesAdapter;
import mobi.publishing.recipes.models.database.CookbookModel;
import mobi.publishing.recipes.models.database.RecipeCategoryModel;

public class RecipeCategoriesActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseInstance;
    private DatabaseReference firebaseDatabase;

    private RecyclerView recyclerView;
    private RecipeCategoriesAdapter adapter;

    private CookbookModel cookbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_categories);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        assignList();

        firebaseInstance = FirebaseDatabase.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.containsKey("cookbook")){
                cookbook = (CookbookModel) extras.getSerializable("cookbook");
                firebaseDatabase = firebaseInstance.getReference("recipeCategories/" +
                    cookbook.getAuthorId() + "/" + cookbook.getId() + "/");
            }
        }

        firebaseDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("added", "child added");
                //for(DataSnapshot node : dataSnapshot.getChildren()) {
                    RecipeCategoryModel recipeCategory = dataSnapshot.getValue(RecipeCategoryModel.class);
                    recipeCategory.setId(dataSnapshot.getKey());
                    recipeCategory.setCookbook(cookbook);

                    adapter.add(recipeCategory);
                //}
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //Log.d("changed", s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("databaseError", databaseError.getMessage());
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return true;
        }
    }

    private void assignList() {
        adapter = new RecipeCategoriesAdapter(this, new ArrayList<RecipeCategoryModel>());
        recyclerView.setAdapter(adapter);
    }
}
