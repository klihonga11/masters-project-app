package mobi.publishing.recipes.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mobi.publishing.recipes.R;
import mobi.publishing.recipes.models.database.RecipeCategoryModel;
import mobi.publishing.recipes.models.database.RecipeModel;
import mobi.publishing.recipes.views.activities.RecipeCategoriesActivity;
import mobi.publishing.recipes.views.activities.ViewRecipeActivity;

/**
 * Created by HP on 7/11/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<RecipeModel> data;

    public RecipesAdapter(Context context, ArrayList<RecipeModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipes, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RecipeModel recipe = data.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        Picasso.with(context)
                .load(recipe.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.profilePicture);

        viewHolder.name.setText(recipe.getName());
    }

    public void add(RecipeModel recipeModel) {
        data.add(recipeModel);
        notifyItemInserted(data.size());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView profilePicture;
        public TextView name;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            profilePicture = itemLayoutView.findViewById(R.id.imageViewProfilePicture);
            name = itemLayoutView.findViewById(R.id.textViewName);

            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            context.startActivity(new Intent(context, ViewRecipeActivity.class)
                    .putExtra("recipe", data.get(getLayoutPosition())));
        }
    }
}
