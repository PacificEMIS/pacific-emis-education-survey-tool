package com.micronesia.data_source.db.models;

import android.support.annotation.NonNull;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.micronesia.models.survey.SubCriteria;

@DatabaseTable
public class OrmLiteSubCriteria implements SubCriteria {

    public interface Column {
        String ID = "id";
        String NAME = "name";
        String QUESTION = "question";
        String CRITERIA = "criteria";
    }

    @DatabaseField(generatedId = true, columnName = Column.ID)
    protected long id;
    @DatabaseField(columnName = Column.NAME)
    protected String name;
    @DatabaseField(columnName = Column.QUESTION)
    protected String question;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true, columnName = Column.CRITERIA)
    protected OrmLiteCriteria criteria;

    public OrmLiteSubCriteria() {
    }

    public OrmLiteSubCriteria(String name, String question, OrmLiteCriteria criteria) {
        this.name = name;
        this.question = question;
        this.criteria = criteria;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public long getCriteriaId() {
        return criteria.getId();
    }

    @NonNull
    @Override
    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String getQuestion() {
        return question;
    }

}
