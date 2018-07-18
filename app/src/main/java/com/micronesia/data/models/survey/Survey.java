package com.micronesia.data.models.survey;

public interface Survey {

    long getId();

    School getSchoolToOne();

    int getYear();

}