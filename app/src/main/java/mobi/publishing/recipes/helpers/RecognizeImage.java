package mobi.publishing.recipes.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.api.ClarifaiResponse;
import clarifai2.dto.input.ClarifaiImage;
import clarifai2.dto.input.ClarifaiInput;
import mobi.publishing.recipes.models.other.SelectableIngredient;
import mobi.publishing.recipes.views.activities.ConfirmIngredientsActivity;
import okhttp3.OkHttpClient;

/**
 * Created by HP on 6/12/2018.
 */

public class RecognizeImage extends AsyncTask<Void, Void, String> {
    private Context context;
    private String path;
    private ProgressDialog progressDialog;
    private ArrayList<String> currentIngredients;

    public RecognizeImage(Context context, String path, ArrayList<String> currentIngredients){
        this.context = context;
        this.path = path;
        this.currentIngredients = currentIngredients;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Processing...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        ClarifaiClient client = new ClarifaiBuilder("bab0fc83017446d2bf844e4f40f096d7")
                .client(new OkHttpClient()) // OPTIONAL. Allows customization of OkHttp by the user
                .buildSync(); // or use .build() to get a Future<ClarifaiClient>

        ClarifaiResponse response = client.getDefaultModels().foodModel().predict()
                .withInputs(
                        ClarifaiInput.forImage(ClarifaiImage.of(new File(path)))
                        //ClarifaiInput.forImage(ClarifaiImage.of("https://tabletotable.org/wp-content/uploads/2016/04/iStock_000005631178Medium.jpg"))
                )
                .executeSync();

        return response.rawBody();
    }

    @Override
    protected void onPostExecute(String result) {
        progressDialog.dismiss();

        super.onPostExecute(result);

        Gson gson = new Gson();
        JsonObject json = gson.fromJson(result, JsonObject.class);

        String response = json.getAsJsonObject("status")
                .get("code").getAsString();

        JsonArray recognized = json.getAsJsonArray("outputs").get(0)
                .getAsJsonObject()
                .getAsJsonObject("data")
                .getAsJsonArray("concepts");

        /*ArrayList list = gson.fromJson(recognized, ArrayList.class);
        Collections.sort(list, new Comparator<JsonObject>() {
            @Override
            public int compare(JsonObject o1, JsonObject o2) {
                Double value1 = o1.get("value").getAsDouble();
                Double value2 = o2.get("value").getAsDouble();

                return value2.compareTo(value1);
            }
        });*/

        ArrayList<SelectableIngredient> ingredients = new ArrayList<>();
        for(int i = 0; i < recognized.size(); i++) {
            if(recognized.get(i).getAsJsonObject().get("value").getAsDouble() >= 0.95) {
                ingredients.add(new SelectableIngredient(recognized.get(i).getAsJsonObject().get("name").getAsString()));
            }
        }

        for(String ingredient: currentIngredients) {
            if(!ingredients.contains(new SelectableIngredient(ingredient))) { //DUPLICATES CHECK
                ingredients.add(new SelectableIngredient(ingredient));
            }
        }

        context.startActivity(new Intent(context, ConfirmIngredientsActivity.class)
            .putExtra("response", response)
            .putExtra("data", ingredients)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}
