package fm.doe.national.mock;

import java.util.ArrayList;
import java.util.List;

import fm.doe.national.R;

public class MockRepository {

    private static ArrayList<MockStandard> standards = new ArrayList<>();

    public static void mock() {
        MockStandard standard1 = new MockStandard();
        standard1.setName("Standard 1: Leadership");
        standard1.setIcon(R.drawable.ic_standard_leadership);
        standard1.setIconHighlighted(R.drawable.ic_standard_leadership_hl);
        populateStandard(standard1);
        standards.add(standard1);

        MockStandard standard2 = new MockStandard();
        standard2.setName("Standard 2: Teacher Performance");
        standard2.setIcon(R.drawable.ic_standard_teacher);
        standard2.setIconHighlighted(R.drawable.ic_standard_teacher_hl);
        populateStandard(standard2);
        standards.add(standard2);

        MockStandard standard3 = new MockStandard();
        standard3.setName("Standard 3: Data Management");
        standard3.setIcon(R.drawable.ic_standard_data);
        standard3.setIconHighlighted(R.drawable.ic_standard_data_hl);
        populateStandard(standard3);
        standards.add(standard3);

        MockStandard standard4 = new MockStandard();
        standard4.setName("Standard 4: National Curriculum");
        standard4.setIcon(R.drawable.ic_standard_cirriculum);
        standard4.setIconHighlighted(R.drawable.ic_standard_cirriculum_hl);
        populateStandard(standard4);
        standards.add(standard4);

        MockStandard standard5 = new MockStandard();
        standard5.setName("Standard 5: School Facility");
        standard5.setIcon(R.drawable.ic_standard_facility);
        standard5.setIconHighlighted(R.drawable.ic_standard_facility_hl);
        populateStandard(standard5);
        standards.add(standard5);

        MockStandard standard6 = new MockStandard();
        standard6.setName("Standard 6: School Improvement");
        standard6.setIcon(R.drawable.ic_standard_improvement);
        standard6.setIconHighlighted(R.drawable.ic_standard_improvement_hl);
        populateStandard(standard6);
        standards.add(standard6);

        MockStandard standard7 = new MockStandard();
        standard7.setName("Class Observation");
        standard7.setIcon(R.drawable.ic_standard_observation);
        standard7.setIconHighlighted(R.drawable.ic_standard_observation_hl);
        populateStandard(standard7);
        standards.add(standard7);
    }

    public static List<MockStandard> getStandards() {
        return standards;
    }

    public static MockStandard getNextStandard(MockStandard currentStandard) {
        int currentIndex = standards.indexOf(currentStandard);
        return currentIndex < standards.size() - 1 ? standards.get(currentIndex + 1) : standards.get(0);
    }

    public static MockStandard getPreviousStandard(MockStandard currentStandard) {
        int currentIndex = standards.indexOf(currentStandard);
        return currentIndex > 0 ? standards.get(currentIndex - 1) : standards.get(standards.size() - 1);
    }

    private static void populateStandard(MockStandard standard) {
        ArrayList<MockCriteria> criterias = new ArrayList<>();

        ArrayList<MockSubCriteria> subs1 = new ArrayList<>();
        subs1.add(new MockSubCriteria("First question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs1.add(new MockSubCriteria("Second question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs1.add(new MockSubCriteria("Third question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs1.add(new MockSubCriteria("Forth question", "HINT", MockSubCriteria.State.NOT_ANSWERED));

        criterias.add(new MockCriteria("1.1. Criteria the First", subs1));

        ArrayList<MockSubCriteria> subs2 = new ArrayList<>();
        subs2.add(new MockSubCriteria("1question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs2.add(new MockSubCriteria("2question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs2.add(new MockSubCriteria("3question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs2.add(new MockSubCriteria("4question", "HINT", MockSubCriteria.State.NOT_ANSWERED));

        criterias.add(new MockCriteria("1.2. Criteria the Second", subs2));

        ArrayList<MockSubCriteria> subs3 = new ArrayList<>();
        subs3.add(new MockSubCriteria("long question long question long question long question long question long question long question long question long question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs3.add(new MockSubCriteria("2question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs3.add(new MockSubCriteria("yet another long question yet another long question yet another long question yet another long question yet another long question yet another long question yet another long question yet another long question yet another long questionyet another long questionyet another long questionyet another long question", "HINT", MockSubCriteria.State.NOT_ANSWERED));
        subs3.add(new MockSubCriteria("4question", "HINT", MockSubCriteria.State.NOT_ANSWERED));

        criterias.add(new MockCriteria("1.3. Some long criteria title that should use multiline for debugSome long criteria title that should use multiline for debugSome long criteria title that should use multiline for debug", subs3));

        standard.setCriterias(criterias);
    }
}
