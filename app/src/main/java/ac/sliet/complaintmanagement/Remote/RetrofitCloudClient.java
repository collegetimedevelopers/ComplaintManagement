package ac.sliet.complaintmanagement.Remote;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCloudClient {
    private static Retrofit instance;
    public static Retrofit getInstance()
    {
        if(instance==null)
            instance=new Retrofit.Builder()
                    .baseUrl("https://sliet-complaint-management-default-rtdb.firebaseio.com/").addConverterFactory(GsonConverterFactory.create()).build();
        return instance;
    }
}