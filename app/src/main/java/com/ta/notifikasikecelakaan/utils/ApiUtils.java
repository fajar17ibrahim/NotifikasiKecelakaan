package com.ta.notifikasikecelakaan.utils;

import com.ta.notifikasikecelakaan.network.ApiClient;
import com.ta.notifikasikecelakaan.network.ApiInterface;

public class ApiUtils {
    public static final String BASE_URL_API = "http://saltransp.com/restapi/";

    public static ApiInterface getAPIService() {
        return ApiClient.getClient(BASE_URL_API).create(ApiInterface.class);
    }
}
