package mobi.publishing.recipes.helpers;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by HP on 7/27/2018.
 */

public class MyFirebaseApp extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
