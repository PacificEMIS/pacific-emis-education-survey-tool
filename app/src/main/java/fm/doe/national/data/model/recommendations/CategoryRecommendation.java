package fm.doe.national.data.model.recommendations;

import fm.doe.national.data.model.Category;

public class CategoryRecommendation extends Recommendation<Category> {

    private static final int LEVEL = 0;

    public CategoryRecommendation(Category object) {
        super(object, object.getTitle(), LEVEL);
    }
}
