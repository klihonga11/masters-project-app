package mobi.publishing.recipes.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import mobi.publishing.recipes.R;
import mobi.publishing.recipes.adapters.SuggestedRecipesAdapter;
import mobi.publishing.recipes.helpers.Common;
import mobi.publishing.recipes.models.database.CookbookModel;
import mobi.publishing.recipes.models.database.RecipeCategoryModel;
import mobi.publishing.recipes.models.database.RecipeIngredientModel;
import mobi.publishing.recipes.models.database.RecipeModel;

public class SuggestedRecipesActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseInstance;
    private DatabaseReference firebaseDatabase;

    //private RecyclerView recyclerView;
    private com.malinskiy.superrecyclerview.SuperRecyclerView recyclerView;
    private SuggestedRecipesAdapter adapter;

    private ArrayList<String> confirmedIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);

        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        assignList();

        firebaseInstance = FirebaseDatabase.getInstance();
        firebaseDatabase = firebaseInstance.getReference("recipeToRecipeIngredients");

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.containsKey("confirmedIngredients")) {
                confirmedIngredients = extras.getStringArrayList("confirmedIngredients");
            }
        }

        firebaseDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot dataSnapshot, String s) {
                int foundIngredientsCounter = 0;

                for(DataSnapshot ingredient: dataSnapshot.child("ingredients").getChildren()){
                    RecipeIngredientModel recipeIngredient = ingredient.getValue(RecipeIngredientModel.class);
                    if(Common.containsCaseInsensitive(recipeIngredient.getName(), confirmedIngredients)) {
                        foundIngredientsCounter++;
                    }

                    if(foundIngredientsCounter == confirmedIngredients.size()) {
                        DatabaseReference recipeRef = firebaseInstance.getReference("recipes/" +
                                dataSnapshot.child("authorId").getValue().toString() + "/" +
                                dataSnapshot.child("cookbookId").getValue().toString() + "/" +
                                dataSnapshot.child("recipeCategoryId").getValue().toString() + "/" +
                                dataSnapshot.getKey());

                        recipeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot recipeSnapshot) {
                                CookbookModel cookbook = new CookbookModel();
                                cookbook.setId(dataSnapshot.child("cookbookId").getValue().toString());
                                cookbook.setAuthorId(dataSnapshot.child("authorId").getValue().toString());

                                RecipeCategoryModel recipeCategory = new RecipeCategoryModel();
                                recipeCategory.setId(dataSnapshot.child("recipeCategoryId").getValue().toString());
                                recipeCategory.setCookbook(cookbook);

                                RecipeModel recipe = recipeSnapshot.getValue(RecipeModel.class);
                                recipe.setId(recipeSnapshot.getKey());
                                recipe.setRecipeCategory(recipeCategory);

                                adapter.add(recipe);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        break;
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_suggested_recipes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_back_to_main:
                startActivity(new Intent(SuggestedRecipesActivity.this, MainActivity.class));
                return true;
            default:
                return true;
        }
    }

    private void assignList() {
        adapter = new SuggestedRecipesAdapter(this, new ArrayList<RecipeModel>());
        recyclerView.setAdapter(adapter);
    }
}
