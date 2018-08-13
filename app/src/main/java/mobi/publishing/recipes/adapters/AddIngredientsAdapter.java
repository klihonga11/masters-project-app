package mobi.publishing.recipes.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.malinskiy.superrecyclerview.swipe.BaseSwipeAdapter;

import java.util.ArrayList;

import mobi.publishing.recipes.R;

/**
 * Created by HP on 7/10/2018.
 */

public class AddIngredientsAdapter extends BaseSwipeAdapter<AddIngredientsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> data;

    public AddIngredientsAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public AddIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_new_ingredient, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddIngredientsAdapter.ViewHolder holder, int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.editText.setText(data.get(position));

        viewHolder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //data.get(viewHolder.getAdapterPosition()) = (viewHolder.editText.getText().toString());
                data.set(viewHolder.getAdapterPosition(), viewHolder.editText.getText().toString());
            }
        });

        viewHolder.editText.requestFocus();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public boolean areAnySlotsEmpty() {
        for(String ingredient: data) {
            if (ingredient.equalsIgnoreCase("")) {
                return true;
            }
        }
        return false;
    }

    public boolean areThereAnyDuplicates() {
        for(int i = 0; i < data.size();i++) {
            int duplicateCounter = 0;
            String value = data.get(i).trim();

            for(int j = 0;j < data.size();j++) {
                if(value.equalsIgnoreCase(data.get(j).trim())) {
                    duplicateCounter++;
                }
            }

            if(duplicateCounter > 1) {
                return true;
            }
        }
        return false;
    }

    public void add() {
        data.add("");
        notifyItemInserted(data.size());
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public ArrayList<String> getConfirmedIngredients() {
        return data;
    }

    public class ViewHolder extends BaseSwipeAdapter.BaseSwipeableViewHolder {
        public EditText editText;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            editText = itemLayoutView.findViewById(R.id.editTextName);
        }
    }
}
