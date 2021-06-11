package org.pacific_emis.surveys.remote_data;

public enum ApiContext {
    FEDEMIS("https://fedemis.doe.fm/api/", "FedEMIS"), MIEMIS("http://data.pss.edu.mh/miemis/api/", "MIEMIS");

    String baseUrl;
    String name;
    ApiContext(String baseUrl, String name) {
        this.baseUrl = baseUrl;
        this.name = name;
    }
}
