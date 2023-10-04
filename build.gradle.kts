val setupDirectory = tasks.register<Sync>("setupDirectory") {
    into(layout.buildDirectory.dir("destinationDir"))
    from(".github") // some random dir
    preserve {
        include("logs/**")
    }
}

tasks.register("runProcess") {
    dependsOn(setupDirectory)

    doFirst {
        val logsDir = setupDirectory.get().destinationDir.resolve("logs").apply { mkdirs() }
        val bytes = "a".repeat(8192).toByteArray()
        // generate 10 logs of 1GB each
        repeat(10) { i ->
            val log = logsDir.resolve("log-$i.txt")
            log.outputStream().use { stream ->
                repeat(131072) {
                    stream.write(bytes)
                }
            }
        }
    }
}
