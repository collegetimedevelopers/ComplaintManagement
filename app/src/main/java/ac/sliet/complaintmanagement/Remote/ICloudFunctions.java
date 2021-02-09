package ac.sliet.complaintmanagement.Remote;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Query;

public interface ICloudFunctions {
    Observable<ResponseBody> getCustomToken(@Query("acess_token")String accessToken);
}
