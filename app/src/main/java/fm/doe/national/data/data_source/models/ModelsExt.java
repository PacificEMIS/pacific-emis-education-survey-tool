package fm.doe.national.data.data_source.models;

import android.support.annotation.NonNull;

public class ModelsExt {

    public static int getTotalQuestionsCount(@NonNull Standard standard) {
        int count = 0;
        for (Criteria criteria: standard.getCriterias()) {
            count += criteria.getSubCriterias().size();
        }
        return count;
    }

    public static int getAnsweredQuestionsCount(@NonNull Standard standard) {
        return 0; // TODO: waiting for answers impl
    }

    public static int getTotalQuestionsCount(@NonNull GroupStandard group) {
        int count = 0;
        for (Standard standard: group.getStandards()) {
            count += getTotalQuestionsCount(standard);
        }
        return count;
    }

    public static int getAnsweredQuestionsCount(@NonNull GroupStandard group) {
        int count = 0;
        for (Standard standard: group.getStandards()) {
            count += getAnsweredQuestionsCount(standard);
        }
        return count;
    }

    public static int getTotalQuestionsCount(@NonNull SchoolAccreditation accreditation) {
        int count = 0;
        for (GroupStandard groupStandard: accreditation.getGroupStandards()) {
            count += getTotalQuestionsCount(groupStandard);
        }
        return count;
    }

    public static int getAnsweredQuestionsCount(@NonNull SchoolAccreditation accreditation) {
        int count = 0;
        for (GroupStandard groupStandard: accreditation.getGroupStandards()) {
            count += getAnsweredQuestionsCount(groupStandard);
        }
        return count;
    }

}
