package mobile.example.alarmtest;

import android.app.PendingIntent;
import android.content.*;
import android.widget.*;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class RepeatReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent i) {
		Toast.makeText(context, "Hi all!", Toast.LENGTH_SHORT).show();

		// notification 생성
		Intent intent = new Intent(context, MainActivity.class);   //this 대신 context!! this는 mainActivity에서 수행할때 쓰는것. 여긴 main이 아니기에 onReceive메소드에서 받아온 context로 써주기!
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0); //얘는 activity가 아니기 때문에 this 대신에 onReceive가 받아온 context 사용!

		NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.CHANNEL_ID))  //채널 사용하기 때문에 메인에서 채널 등록해줘야함!
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("기상 시간")
				.setContentText("일어나! 공부할 시간이야!")
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
				.setContentIntent(pendingIntent)
				.addAction(R.drawable.ic_launcher, "Noti", pendingIntent)
				.setAutoCancel(true);

		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

		int notificationId = 100;
		notificationManager.notify(notificationId, builder.build());
	}
}
