package br.com.developen.sig.util;

import android.app.job.JobParameters;
import android.app.job.JobService;

import br.com.developen.sig.task.ImportAsyncTask;


public class JobsService extends JobService implements ImportAsyncTask.Listener {

    public boolean onStartJob(JobParameters params) {

        DB database = DB.getInstance(App.getInstance());

/*        List<Integer> salesToUpload = database.saleDAO().getSalesToUpload();

        if (salesToUpload != null && !salesToUpload.isEmpty()){

            new UploadSaleAsyncTask(this).execute(salesToUpload.toArray(new Integer[salesToUpload.size()]));

        } else {

            Jobs.scheduleJob(getApplicationContext());

        } */

        return true;

    }

    public boolean onStopJob(JobParameters params) {

        return true;

    }

    public void onImportPreExecute() {}

    public void onImportProgressUpdate(Integer status) {}

    public void onImportSuccess() {

        Jobs.scheduleJob(getApplicationContext());

    }

    public void onImportFailure(Messaging messaging) {

        Jobs.scheduleJob(getApplicationContext());

    }

    public void onImportCancelled() {}

}