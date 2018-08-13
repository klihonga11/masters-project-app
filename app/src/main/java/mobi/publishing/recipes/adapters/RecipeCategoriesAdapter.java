package mobi.publishing.recipes.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import mobi.publishing.recipes.R;
import mobi.publishing.recipes.models.database.RecipeCategoryModel;
import mobi.publishing.recipes.views.activities.RecipesActivity;

/**
 * Created by HP on 7/11/2018.
 */

public class RecipeCategoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<RecipeCategoryModel> data;

    public RecipeCategoriesAdapter(Context context, ArrayList<RecipeCategoryModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipe_categories, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecipeCategoryModel recipeCategory = data.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.name.setText(recipeCategory.getName());
        viewHolder.description.setText(recipeCategory.getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(RecipeCategoryModel recipeCategoryModel) {
        data.add(recipeCategoryModel);
        notifyItemInserted(data.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView description;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            name = itemLayoutView.findViewById(R.id.textViewName);
            description = itemLayoutView.findViewById(R.id.textViewDescription);

            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            context.startActivity(new Intent(context, RecipesActivity.class)
                    .putExtra("recipeCategory", data.get(getLayoutPosition())));
        }
    }
}
