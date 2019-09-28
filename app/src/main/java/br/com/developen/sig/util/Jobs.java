package br.com.developen.sig.util;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

public class Jobs {

    public static void scheduleJob(Context context) {

        ComponentName serviceComponent = new ComponentName(context, JobsService.class);

        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);

        builder.setMinimumLatency(1 * 1000); // wait at least

        builder.setOverrideDeadline(3 * 1000); // maximum delay

        JobScheduler jobScheduler;

        jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        jobScheduler.schedule(builder.build());

    }

}