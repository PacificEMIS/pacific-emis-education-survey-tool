package fm.doe.national.ui.screens.report.recommendations;

import fm.doe.national.data.model.Category;

public class CategoryRecommendation extends Recommendation<Category> {

    public CategoryRecommendation(Category object) {
        super(object, object.getTitle(), 0);
    }
}
