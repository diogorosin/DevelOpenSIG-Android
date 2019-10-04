package br.com.developen.sig.job;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class AppJobCreator implements JobCreator {

    public Job create(String tag) {

        switch (tag) {

            case DownloadJob.TAG:

                return new DownloadJob();

            default:

                return null;

        }

    }

}