package fm.doe.national.wash.ui.questions;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.wash.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionsFragment extends BaseFragment {


    public static QuestionsFragment create(long groupId, long subGroupId) {
        return new QuestionsFragment();
    }


    public QuestionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questions, container, false);
    }

}
