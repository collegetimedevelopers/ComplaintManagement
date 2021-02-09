package ac.sliet.complaintmanagement.Remote;

import io.reactivex.Observable;

import ac.sliet.complaintmanagement.Model.FCMResponse;
import ac.sliet.complaintmanagement.Model.FCMSendData;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService
{
    //Authorization key is server key of Cloud Messaging
    @Headers({
            "Content-Type:application/json",

            "Authorization:key=AAAA-_otgV0:APA91bGF_1_WHReHhvBaajWhjQUq6mzUuWov8xIaRCreyPgMnjd0aDaL9PtfOst4YKDdbAMZnUxh9l4vJgbwQ5-N9boiN66Oh2_Gg08GN9upoKu4dx5_M2WeBWQsWwJQehohaFIOBGMK"

    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);


}
