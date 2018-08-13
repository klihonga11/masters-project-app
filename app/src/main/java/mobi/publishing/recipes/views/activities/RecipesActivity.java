package mobi.publishing.recipes.views.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import mobi.publishing.recipes.adapters.RecipesAdapter;
import mobi.publishing.recipes.models.database.RecipeCategoryModel;
import mobi.publishing.recipes.models.database.RecipeModel;

public class RecipesActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseInstance;
    private DatabaseReference firebaseDatabase;

    private RecyclerView recyclerView;
    private RecipesAdapter adapter;

    private RecipeCategoryModel recipeCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        assignList();

        firebaseInstance = FirebaseDatabase.getInstance();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.containsKey("recipeCategory")){
                recipeCategory = (RecipeCategoryModel) extras.getSerializable("recipeCategory");
                firebaseDatabase = firebaseInstance.getReference("recipes/" +
                        recipeCategory.getCookbook().getAuthorId() + "/" +
                        recipeCategory.getCookbook().getId() + "/" +
                        recipeCategory.getId() + "/");
            }
        }

        firebaseDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("added", "child added");
                //for(DataSnapshot node : dataSnapshot.getChildren()) {
                RecipeModel recipe = dataSnapshot.getValue(RecipeModel.class);
                recipe.setId(dataSnapshot.getKey());
                recipe.setRecipeCategory(recipeCategory);

                adapter.add(recipe);
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
        adapter = new RecipesAdapter(this, new ArrayList<RecipeModel>());
        recyclerView.setAdapter(adapter);
    }
}
