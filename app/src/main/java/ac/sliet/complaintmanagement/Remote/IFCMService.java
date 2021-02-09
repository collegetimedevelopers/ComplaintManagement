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

            "Authorization:key=AAAAydLKf_w:APA91bGF6Qp-qw5Io2R1czz3YbM0SSHqqlKa6a9L7-O5XahvvZZBDyWwXUkXlaWRF6rR7h_srb7SWHzF8kdGYUBMMp-HsmdppNrfKwwGNSxzoYz4rxK61aJZ6O6vz2u-IWaIrEF0EMa2"

    })
    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);


}
