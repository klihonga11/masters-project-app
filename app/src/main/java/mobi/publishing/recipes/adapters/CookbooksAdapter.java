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
import mobi.publishing.recipes.models.database.CookbookModel;
import mobi.publishing.recipes.views.activities.RecipeCategoriesActivity;

/**
 * Created by HP on 7/3/2018.
 */

public class CookbooksAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<CookbookModel> data;

    public CookbooksAdapter(Context context, ArrayList<CookbookModel> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cookbooks, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CookbookModel cookbookModel = data.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        Picasso.with(context)
                .load(cookbookModel.getImageUrl())
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.profilePicture);

        viewHolder.title.setText(cookbookModel.getTitle());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void add(CookbookModel cookbookModel) {
        data.add(cookbookModel);
        notifyItemInserted(data.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView profilePicture;
        public TextView title;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            profilePicture = itemLayoutView.findViewById(R.id.imageViewProfilePicture);
            title = itemLayoutView.findViewById(R.id.textViewTitle);

            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            context.startActivity(new Intent(context, RecipeCategoriesActivity.class)
                .putExtra("cookbook", data.get(getLayoutPosition())));
        }
    }
}
