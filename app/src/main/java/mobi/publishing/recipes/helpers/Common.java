package mobi.publishing.recipes.helpers;

import java.util.List;

/**
 * Created by HP on 7/20/2018.
 */

public class Common {
    public static boolean containsCaseInsensitive(String s, List<String> l){
        for (String string : l){
            if (string.equalsIgnoreCase(s)){
                return true;
            }
        }
        return false;
    }
}
