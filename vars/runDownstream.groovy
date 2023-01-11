def call(Map params){
    List jobs = params.jobs
    for (job in jobs) {
        build wait:false job:"${job}"
    }
}