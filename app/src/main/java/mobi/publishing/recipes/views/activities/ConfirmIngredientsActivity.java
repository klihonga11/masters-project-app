package mobi.publishing.recipes.views.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.malinskiy.superrecyclerview.swipe.SwipeDismissRecyclerViewTouchListener;
import com.malinskiy.superrecyclerview.swipe.SwipeItemManagerInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import mobi.publishing.recipes.R;
import mobi.publishing.recipes.adapters.ConfirmIngredientsAdapter;
import mobi.publishing.recipes.models.other.SelectableIngredient;

public class ConfirmIngredientsActivity extends AppCompatActivity {

    private com.malinskiy.superrecyclerview.SuperRecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ConfirmIngredientsAdapter adapter;

    private FloatingActionButton fab;

    private ArrayList<SelectableIngredient> recognizedIngredients;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ingredients);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.containsKey("response")) {
                if(extras.getString("response").equalsIgnoreCase("10000")) {
                    recognizedIngredients = (ArrayList<SelectableIngredient>) extras.getSerializable("data");
                } else {
                    recognizedIngredients = new ArrayList<>();
                }
            }
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmIngredientsActivity.this, TakePhotoActivity.class)
                        .putExtra("currentIngredients", adapter.getConfirmedIngredients()));
            }
        });

        AssignList();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_ingredients, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_find_recipes:
                startActivity(new Intent(ConfirmIngredientsActivity.this, SuggestedRecipesActivity.class)
                    .putExtra("confirmedIngredients", adapter.getConfirmedIngredients()));
                return true;
            /*case R.id.action_add_ingredients:
                startActivity(new Intent(ConfirmIngredientsActivity.this, TakePhotoActivity.class)
                    .putExtra("currentIngredients", adapter.getConfirmedIngredients()));
                return true;*/
            case R.id.action_clear_ingredients:
                new AlertDialog.Builder(this)
                        .setTitle("Clear Ingredients")
                        .setMessage("Are you sure you want to remove all the listed ingredients and start over?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(ConfirmIngredientsActivity.this, TakePhotoActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));

                                /*startActivity(new Intent(ConfirmIngredientsActivity.this, MainActivity.class)
                                        .putExtra("position", 1)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));*/
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
                return true;
            default:
                return true;
        }
    }

    private void AssignList() {
        Collections.sort(recognizedIngredients, new Comparator<SelectableIngredient>() {
            @Override
            public int compare(SelectableIngredient o1, SelectableIngredient o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        adapter = new ConfirmIngredientsAdapter(this, recognizedIngredients);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        /*adapter.setMode(SwipeItemManagerInterface.Mode.Single);
        recyclerView.setupSwipeToDismiss(new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
                adapter.remove(reverseSortedPositions[0]);
            }
        });*/
    }
}
