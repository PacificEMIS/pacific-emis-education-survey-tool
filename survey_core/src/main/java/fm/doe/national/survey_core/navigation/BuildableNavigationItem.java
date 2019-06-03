package fm.doe.national.survey_core.navigation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.omega_r.libs.omegatypes.Text;

import fm.doe.national.core.ui.screens.base.BaseFragment;

public abstract class BuildableNavigationItem extends NavigationItem {

    @Nullable
    private BuildableNavigationItem previousItem;

    @Nullable
    private BuildableNavigationItem nextItem;

    public BuildableNavigationItem(Text title, long id) {
        super(title, id);
    }

    @NonNull
    public abstract BaseFragment buildFragment();

    @Nullable
    public BuildableNavigationItem getPreviousItem() {
        return previousItem;
    }

    public void setPreviousItem(@Nullable BuildableNavigationItem previousItem) {
        this.previousItem = previousItem;
    }

    @Nullable
    public BuildableNavigationItem getNextItem() {
        return nextItem;
    }

    public void setNextItem(@Nullable BuildableNavigationItem nextItem) {
        this.nextItem = nextItem;
    }
}
