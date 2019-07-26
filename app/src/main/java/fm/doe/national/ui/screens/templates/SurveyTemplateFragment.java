package fm.doe.national.ui.screens.templates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.omega_r.libs.omegatypes.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import fm.doe.national.R;
import fm.doe.national.core.ui.screens.base.BaseAdapter;
import fm.doe.national.core.ui.screens.base.BaseFragment;
import fm.doe.national.survey_core.navigation.NavigationItem;
import fm.doe.national.survey_core.navigation.NavigationItemsAdapter;

public abstract class SurveyTemplateFragment extends BaseFragment implements
        SurveyTemplateView,
        BaseAdapter.OnItemClickListener<NavigationItem> {

    @BindView(R.id.textview_title)
    TextView titleTextView;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;

    private NavigationItemsAdapter itemsAdapter = new NavigationItemsAdapter(this);

    public abstract Text getPageTitle();
    public abstract Text getLoadText();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_survey_template, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setAdapter(itemsAdapter);
        getLoadText().applyTo(titleTextView);
    }

    @Override
    public void setItems(List<NavigationItem> items) {
        itemsAdapter.setItems(items);
    }

    @OnClick(R.id.button_load)
    void onLoadPressed() {
        ((SurveyTemplatePresenter) getPresenter()).onLoadPressed();
    }

    @Override
    public void onItemClick(NavigationItem item) {
        // do nothing
    }
}
