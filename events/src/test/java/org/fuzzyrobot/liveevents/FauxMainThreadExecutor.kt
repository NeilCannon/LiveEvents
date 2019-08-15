package org.fuzzyrobot.liveevents

import androidx.arch.core.executor.TaskExecutor
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object FauxMainThreadExecutor : TaskExecutor() {

    private var jobs = HashSet<Job>()

    override fun executeOnDiskIO(runnable: Runnable) {
        TODO("not implemented: this Executor is for mainThread only")
    }

    override fun isMainThread() = true

    override fun postToMainThread(runnable: Runnable) {
        jobs.add(GlobalScope.launch {
            runnable.run()
        })
    }

    fun waitForJobs() {
        runBlocking {
            for (job in jobs) {
                job.join()
            }
            jobs.clear()
        }
    }

}