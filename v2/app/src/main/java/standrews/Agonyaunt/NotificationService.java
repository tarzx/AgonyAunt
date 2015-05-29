package standrews.Agonyaunt;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/** This class handles notifications
 * @author Jiachun Liu
 * @author Patomporn Loungvara
 */
public class NotificationService extends Service {

    // Enables change notification
    private static final int mId = 1;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		notificationCreator();
		return super.onStartCommand(intent, flags, startId);
	}

	/** Creates a notification */
	private void notificationCreator() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.new_icon)
                .setContentTitle("Agony Aunt")
                .setContentText("It's time for a chat!");
        mBuilder.setAutoCancel(true);
		Intent intent = new Intent(this, NotificationReceiverActivity.class);

        // Load Updated Neural Network File
        new updateNeuralNet().execute();

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
		mNotificationManager.notify(mId, mBuilder.build());
	}

    /**
     * Background Async Task to update neural network
     * */
    class updateNeuralNet extends AsyncTask<String, String, String> {

        /**
         * Invoke the encog file in server
         * */
        protected String doInBackground(String... args) {
            //LoadUpdated Files
            Util.loadNet(NotificationService.this.getBaseContext(), Util.FREQUENCY_INTERVENTION_NET_URL, Util.FREQUENCY_INTERVENTION_NET_EG);
            Util.loadNet(NotificationService.this.getBaseContext(), Util.SLOT_INTERVENTION_NET_URL, Util.SLOT_INTERVENTION_NET_EG);
            Util.loadNet(NotificationService.this.getBaseContext(), Util.SELECT_SEQUENCE_NET_URL, Util.SELECT_SEQUENCE_NET_EG);
            Util.loadNet(NotificationService.this.getBaseContext(), Util.SELECT_BEHAVIOUR_NET_URL, Util.SELECT_BEHAVIOUR_NET_EG);
            Util.loadNet(NotificationService.this.getBaseContext(), Util.SELECT_GOAL_NET_URL, Util.SELECT_GOAL_NET_EG);
            return null;
        }

    }

}
