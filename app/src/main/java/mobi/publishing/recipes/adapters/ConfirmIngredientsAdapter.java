package mobi.publishing.recipes.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mobi.publishing.recipes.R;
import mobi.publishing.recipes.models.other.SelectableIngredient;

/**
 * Created by HP on 6/14/2018.
 */

public class ConfirmIngredientsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private ArrayList<SelectableIngredient> data;

    public ConfirmIngredientsAdapter(Context context, ArrayList<SelectableIngredient> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_confirm_ingredients, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final SelectableIngredient ingredient = data.get(position);
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.checkBox.setChecked(data.get(position).isSelected());
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.get(holder.getAdapterPosition()).setSelected(isChecked);
            }
        });

        viewHolder.name.setText(ingredient.getName());

        viewHolder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.overflow_confirm_ingredients, popup.getMenu());
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString().toLowerCase()) {
                            case "edit":
                                editIngredient(ingredient.getName(), holder.getAdapterPosition());
                                break;
                        }
                        return false;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void editIngredient(String name, final int position) {
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_ingredient_name, null);
        final EditText editTextName = view.findViewById(R.id.editTextName);

        editTextName.setText(name);
        editTextName.setSelection(name.length());

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Edit")
                .setView(view)
                .setCancelable(false)
                .setPositiveButton("Okay", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editTextName.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(context, "Please enter a name", Toast.LENGTH_LONG).show();
                        } else {
                            data.get(position).setName(editTextName.getText().toString());
                            notifyItemChanged(position);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });

        dialog.show();
    }

    public ArrayList<String> getConfirmedIngredients() {
        ArrayList<String> confirmedIngredients = new ArrayList<>();
        for(SelectableIngredient ingredient : data) {
            if(ingredient.isSelected()) {
                confirmedIngredients.add(ingredient.getName());
            }
        }
        return confirmedIngredients;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox checkBox;
        public TextView name;
        public ImageButton overflow;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            checkBox = itemLayoutView.findViewById(R.id.checkBoxSelected);
            name = itemLayoutView.findViewById(R.id.textViewName);
            overflow = itemLayoutView.findViewById(R.id.imageButtonOverflow);
        }
    }
}
