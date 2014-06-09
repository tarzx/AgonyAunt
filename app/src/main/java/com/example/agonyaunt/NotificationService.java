package com.example.agonyaunt;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/** This class handles notifications
 * @author Jiachun Liu
 */
public class NotificationService extends Service {

	public static final String QUESMANAGER = "com.example.agonyaunt.CreateNotificationActivity";
	QuestionManager quesManager;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		notifcationCreator();
		return super.onStartCommand(intent, flags, startId);
	}

	/** Creates a notification */
	private void notifcationCreator() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Agony Aunt")
				.setContentText("It's time for a chat!");
		mBuilder.setAutoCancel(true);
		Intent intent = new Intent(this, NotificationReceiverActivity.class);
		QuestionManager quesManager = new QuestionManager();
		intent.putExtra(QUESMANAGER, quesManager);

		// The stack builder object will contain an artificial back stack for
		// the started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(NotificationReceiverActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(intent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// Enables change notification
		int mId = 1;
		mNotificationManager.notify(mId, mBuilder.build());
	}

}
