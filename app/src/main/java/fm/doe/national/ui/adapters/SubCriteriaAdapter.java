package fm.doe.national.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Alexander Chibirev on 8/7/2018.
 */
public class SubCriteriaAdapter {

    String[] criterias;
    List<String[]> secondLevel;
    private Context context;
    List<LinkedHashMap<String, String[]>> subCriterias;

}
