package mobi.publishing.recipes.views.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import mobi.publishing.recipes.R;
import mobi.publishing.recipes.models.database.RecipeDirectionModel;
import mobi.publishing.recipes.models.database.RecipeIngredientModel;
import mobi.publishing.recipes.models.database.RecipeModel;

public class ViewRecipeActivity extends AppCompatActivity {
    private FirebaseDatabase firebaseInstance;

    private RecipeModel recipe;

    private LinearLayout linearLayoutGallery;
    private TextView textViewDifficulty;
    private TextView textViewNumberOfServings;
    private TextView textViewDuration;
    private TextView textViewIngredients;
    private TextView textViewDirections;

    private LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_recipe);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mInflater = LayoutInflater.from(this);

        linearLayoutGallery = findViewById(R.id.gallery);
        textViewDifficulty = findViewById(R.id.textViewDifficulty);
        textViewNumberOfServings = findViewById(R.id.textViewNumberOfServings);
        textViewDuration = findViewById(R.id.textViewDuration);
        textViewIngredients = findViewById(R.id.textViewIngredients);
        textViewDirections = findViewById(R.id.textViewDirections);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.containsKey("recipe")) {
                recipe = (RecipeModel) extras.getSerializable("recipe");
            }
        }

        firebaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference imagesRef = firebaseInstance.getReference("recipeImages/" + recipe.getId());
        DatabaseReference recipeIngredientsRef = firebaseInstance.getReference("recipeIngredients/" +
                recipe.getRecipeCategory().getCookbook().getAuthorId() + "/" +
                recipe.getRecipeCategory().getCookbook().getId() + "/" +
                recipe.getRecipeCategory().getId() + "/" +
                recipe.getId() + "/");
        DatabaseReference recipeDirectionsRef = firebaseInstance.getReference("recipeDirections/" +
                recipe.getRecipeCategory().getCookbook().getAuthorId() + "/" +
                recipe.getRecipeCategory().getCookbook().getId() + "/" +
                recipe.getRecipeCategory().getId() + "/" +
                recipe.getId() + "/");

        imagesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot image: dataSnapshot.getChildren()) {
                    View view = mInflater .inflate(R.layout.list_gallery_item, linearLayoutGallery, false);
                    ImageView img = view.findViewById(R.id.imageViewGalleryItem);

                    String imageUrl = image.child("imageUrl").getValue().toString();
                    Picasso.with(ViewRecipeActivity.this)
                            .load(imageUrl)
                            .placeholder(R.drawable.placeholder)
                            .fit().centerCrop()
                            .into(img);

                    linearLayoutGallery.addView(view);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recipeIngredientsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ingredient: dataSnapshot.getChildren()) {
                    RecipeIngredientModel recipeIngredient = ingredient.getValue(RecipeIngredientModel.class);
                    recipeIngredient.setId(ingredient.getKey());
                    recipeIngredient.setRecipe(recipe);

                    textViewIngredients.setText(textViewIngredients.getText().toString() +
                            recipeIngredient.getDescription() + "\n");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recipeDirectionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot direction: dataSnapshot.getChildren()) {
                    RecipeDirectionModel recipeDirection = direction.getValue(RecipeDirectionModel.class);
                    recipeDirection.setId(direction.getKey());
                    recipeDirection.setRecipe(recipe);

                    textViewDirections.setText(textViewDirections.getText().toString() +
                            recipeDirection.getOrder() + ". " + recipeDirection.getDescription() + "\n\n");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        assignLayout();

        if(recipe != null) {
            getSupportActionBar().setTitle(recipe.getName());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_add_to_favourites:
                Toast.makeText(this, "Functionality currently unavailable.", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_share:
                Toast.makeText(this, "Functionality currently unavailable.", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_view_description:
                Toast.makeText(this, "Functionality currently unavailable.", Toast.LENGTH_LONG).show();
                return true;
            default:
                return true;
        }
    }

    private void assignLayout() {
        textViewDifficulty.setText(recipe.getDifficulty());
        textViewNumberOfServings.setText(String.valueOf(recipe.getNumberOfServings()));
        textViewDuration.setText(getDuration());
    }

    private String getDuration() {
        String duration = "5 mins";
        return duration;
        //if(recipe.get)
    }
}
