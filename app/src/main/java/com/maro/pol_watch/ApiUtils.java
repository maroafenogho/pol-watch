package com.maro.pol_watch;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "https://polwatch-gnc.herokuapp.com/auth/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
