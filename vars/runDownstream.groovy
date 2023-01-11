def call(Map params){
    List jobs = params.jobs
    println(jobs)
    for (_job in jobs) {
        build wait:false job:"${_job}"
    }
}