package au.com.geardoaustralia.utils;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by DasunPC on 1/23/17.
 */

public class MySuggestionProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "com.example.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}


