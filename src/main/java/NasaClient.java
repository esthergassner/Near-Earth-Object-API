import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;

public class NasaClient
{
    private final NasaAPI api;

    public NasaClient()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        api = retrofit.create(NasaAPI.class);
    }

    Observable<NEOResponse> getObject(String startDate, String endDate)
    {
        return api.getObject(startDate, endDate);
    }
}
