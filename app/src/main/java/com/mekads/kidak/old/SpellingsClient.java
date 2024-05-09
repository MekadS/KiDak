package com.mekads.kidak;

import android.app.Activity;
import android.os.Bundle;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.TextView;

import java.util.Locale;

public class SpellingsClient extends Activity implements SpellCheckerSession.SpellCheckerSessionListener {

    private TextView suggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*//super.onCreate(savedInstanceState);

        suggestions = new TextView(this);

        suggestions.setPadding(16,16,16,16);
        suggestions.setTextSize(20);

        setContentView(suggestions);

        fetchSuggestionsFor(testWord);*/
    }

    @Override
    public void onGetSuggestions(SuggestionsInfo[] results) {
        // Unused
    }

    @Override
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
        final StringBuffer sb = new StringBuffer("");
        for(SentenceSuggestionsInfo result:results){
            int n = result.getSuggestionsCount();
            for(int i=0; i < n; i++){
                int m = result.getSuggestionsInfoAt(i).getSuggestionsCount();

                if((result.getSuggestionsInfoAt(i).getSuggestionsAttributes() &
                        SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO) != SuggestionsInfo.RESULT_ATTR_LOOKS_LIKE_TYPO )
                    continue;

                for(int k=0; k < m; k++) {
                    sb.append(result.getSuggestionsInfoAt(i).getSuggestionAt(k))
                            .append("\n");
                }
                sb.append("\n");
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                suggestions.setText(sb.toString());
            }
        });
    }

    private final int NUMBER_OF_SUGGESTIONS=8;

    protected void fetchSuggestionsFor(String input){
        TextServicesManager tsm = (TextServicesManager) getSystemService(TEXT_SERVICES_MANAGER_SERVICE);
        SpellCheckerSession session = tsm.newSpellCheckerSession(null, Locale.ENGLISH, this, true);

        session.getSentenceSuggestions(new TextInfo[]{ new TextInfo(input) }, NUMBER_OF_SUGGESTIONS);
    }
}
