package mobi.publishing.recipes.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.malinskiy.superrecyclerview.swipe.SwipeDismissRecyclerViewTouchListener;
import com.malinskiy.superrecyclerview.swipe.SwipeItemManagerInterface;

import java.util.ArrayList;

import mobi.publishing.recipes.R;
import mobi.publishing.recipes.adapters.AddIngredientsAdapter;

public class AddIngredientsActivity extends AppCompatActivity {
    private SuperRecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AddIngredientsAdapter adapter;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredients);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        fab = findViewById(R.id.fab);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.add();
            }
        });

        assignList();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_ingredients, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_confirm_ingredients:
                if(adapter.getItemCount() == 0) {
                    Toast.makeText(this,"Please add ingredients.", Toast.LENGTH_SHORT).show();
                    break;
                } else if (adapter.areAnySlotsEmpty()) {
                    Toast.makeText(this, "Please fill out all available ingredient slots.", Toast.LENGTH_LONG).show();
                    break;
                } else if (adapter.areThereAnyDuplicates()) {
                    Toast.makeText(this, "Please remove any duplicate ingredients.", Toast.LENGTH_LONG).show();
                    break;
                }

                startActivity(new Intent(this, SuggestedRecipesActivity.class)
                        .putExtra("confirmedIngredients", adapter.getConfirmedIngredients()));
                return true;
            default:
                return true;
        }
        return true;
    }

    private void assignList() {
        ArrayList<String> startData = new ArrayList<>();
        startData.add("");

        adapter = new AddIngredientsAdapter(this, startData);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        adapter.setMode(SwipeItemManagerInterface.Mode.Single);
        recyclerView.setupSwipeToDismiss(new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {
                adapter.remove(reverseSortedPositions[0]);
            }
        });
    }
}
