package br.com.developen.sig.job;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import br.com.developen.sig.R;
import br.com.developen.sig.bean.DatasetBean;
import br.com.developen.sig.bean.ExceptionBean;
import br.com.developen.sig.task.ImportAsyncTask;
import br.com.developen.sig.util.Constants;
import br.com.developen.sig.util.Messaging;

public class DownloadJob extends DailyJob implements ImportAsyncTask.Listener {


    static final String TAG = "download_job_tag";


    private static final String NOTIFY_CHANNEL = "download_job";

    private static final long BEGIN = TimeUnit.HOURS.toMillis(1);

    private static final long FINISH = TimeUnit.HOURS.toMillis(6);

//    private static final long BEGIN = TimeUnit.HOURS.toMillis(13) + TimeUnit.MINUTES.toMillis(50) ;

//    private static final long FINISH = TimeUnit.HOURS.toMillis(13) + TimeUnit.MINUTES.toMillis(51);


    private CountDownLatch countDownLatch;

    private NotificationManagerCompat notificationManagerCompat;

    private NotificationCompat.Builder builder;


    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {

        Log.d(TAG, "Iniciando download dos dados do servidor.");

        countDownLatch = new CountDownLatch(2);

        builder = new NotificationCompat.Builder(getContext(), NOTIFY_CHANNEL).
                setSmallIcon(R.drawable.icon_sigesc_24).
                setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL).
                setContentTitle("Atualização").
                setContentText("Donwload em andamento").
                setPriority(NotificationCompat.PRIORITY_MAX).
                setShowWhen(true).
                setOngoing(true).
                setOnlyAlertOnce(true).
                setProgress(0, 0, true);

        notificationManagerCompat = NotificationManagerCompat.from(getContext());

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(NOTIFY_CHANNEL,"Atualização", NotificationManager.IMPORTANCE_DEFAULT);

            notificationManagerCompat.createNotificationChannel(channel);

            builder.setChannelId(NOTIFY_CHANNEL);

        }

        notificationManagerCompat.notify(1,  builder.build());

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");

        Gson gson = gsonBuilder.create();

        StringRequest request = new StringRequest(Request.Method.GET, Constants.SERVER_BASE_URL + "download/dataset",

                response -> {

                    DatasetBean datasetBean = gson.fromJson(response, DatasetBean.class);

                    new ImportAsyncTask<>(this).execute(datasetBean);

                },

                error -> {

                    if (error instanceof NoConnectionError){

                        Log.d(TAG, error.getMessage());

                    } else {

                        try {

                            if (error.networkResponse.data != null) {

                                String responseBody = new String(error.networkResponse.data, "utf-8");

                                Messaging messaging = gson.fromJson(responseBody, ExceptionBean.class);

                                if (messaging != null && messaging.getMessages().length > 0)

                                    Log.d(TAG, messaging.getMessages()[0]);

                            }

                        } catch (UnsupportedEncodingException ignored) {}

                    }

                })

        {

            public String getBodyContentType() {

                return Constants.JSON_CONTENT_TYPE;

            }

            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                String uft8String = new String(response.data, StandardCharsets.UTF_8);

                return Response.success(uft8String, HttpHeaderParser.parseCacheHeaders(response));

            }

            public Map<String, String> getHeaders() {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                Map<String, String> headers = new HashMap<>();

                headers.put(Constants.AUTHORIZATION_HEADER, "Bearer " + preferences.getString(Constants.TOKEN_IDENTIFIER_PROPERTY, null));

                return headers;

            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                1000 * 10,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.addRequestFinishedListener((RequestQueue.RequestFinishedListener<String>) request1 -> {

            countDownLatch.countDown();

        });

        requestQueue.add(request);

        try {

            countDownLatch.await();

        } catch (InterruptedException ignored) {}

        return DailyJobResult.SUCCESS;

    }


    public static Date schedule(JobRequest.NetworkType requiredNetwork) {

        Date nextExecutionAt = null;

        if (JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {

            JobRequest.Builder builder = new JobRequest.Builder(TAG).
                    setRequiresBatteryNotLow(true).
                    setRequiredNetworkType(requiredNetwork);

            DailyJob.schedule(builder, BEGIN, FINISH);

            Log.d(TAG, "Tarefa agendada com sucesso.");

            Calendar calendar = Calendar.getInstance();

            calendar.setTimeInMillis(BEGIN);

            nextExecutionAt = calendar.getTime();

        } else {

            Set<JobRequest> jobRequests = JobManager.instance().getAllJobRequestsForTag(TAG);

            for (JobRequest jobRequest : jobRequests) {

                Calendar calendar = Calendar.getInstance();

                calendar.setTimeInMillis(jobRequest.getStartMs());

                nextExecutionAt = calendar.getTime();

                break;

            }

            Log.d(TAG, "Tarefa já encontra-se agendada.");

        }

        return nextExecutionAt;

    }


    public static Date reschedule(JobRequest.NetworkType requiredNetwork) {

        finish();

        return schedule(requiredNetwork);

    }


    public static void finish(){

        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty())

            JobManager.instance().cancelAllForTag(TAG);

        Log.d(TAG, "Tarefa cancelada com sucesso.");

    }


    public void onImportPreExecute() {

        builder.setContentText("Importando dados");

        builder.setProgress(ImportAsyncTask.MAX_PROGRESS, 0, false);

        notificationManagerCompat.notify(1,  builder.build());

        Log.d(TAG, "Download concluído com sucesso. Iniciando importação dos dados.");

    }


    public void onImportProgressUpdate(Integer status) {

        builder.setProgress(ImportAsyncTask.MAX_PROGRESS, status, false);

        notificationManagerCompat.notify(1,  builder.build());

        Log.d(TAG, "Importando " + status + " de " + ImportAsyncTask.MAX_PROGRESS);

    }


    public void onImportSuccess() {

        countDownLatch.countDown();

        builder.setContentText("Concluída");

        builder.setOngoing(false);

        notificationManagerCompat.notify(1,  builder.build());

        Log.d(TAG, "Importação dos dados executada com sucesso.");

    }


    public void onImportFailure(Messaging messaging) {

        countDownLatch.countDown();

        builder.setContentText("Falhou");

        builder.setOngoing(false);

        notificationManagerCompat.notify(1,  builder.build());

        Log.d(TAG, "Importação dos dados executada com erro: " + messaging.getMessages());


    }


    public void onImportCancelled() {

        countDownLatch.countDown();

        builder.setContentText("Cancelada");

        builder.setOngoing(false);

        notificationManagerCompat.notify(1,  builder.build());

        Log.d(TAG, "Importação dos dados cancelada.");

    }


}