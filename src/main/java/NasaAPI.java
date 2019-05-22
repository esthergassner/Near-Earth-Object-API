
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface NasaAPI
{
    //this is a test url
    @GET("/neo/rest/v1/feed?api_key=gZgcNDyntlSJLhuu471ktMnT4rUoLITFlzGVhwSq")
    Observable<NEOResponse> getObject(@Query("start_date") String startDate, @Query("end_date") String endDate);

}

