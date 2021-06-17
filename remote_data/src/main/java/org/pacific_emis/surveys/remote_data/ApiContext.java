package org.pacific_emis.surveys.remote_data;

import org.pacific_emis.surveys.core.preferences.entities.AppRegion;

public enum ApiContext {
    FEDEMIS("https://fedemis.doe.fm/api/", AppRegion.FSM), MIEMIS("http://data.pss.edu.mh/miemis/api/", AppRegion.RMI);

    String baseUrl;
    AppRegion appRegion;

    ApiContext(String baseUrl, AppRegion appRegion) {
        this.baseUrl = baseUrl;
        this.appRegion = appRegion;
    }
}
