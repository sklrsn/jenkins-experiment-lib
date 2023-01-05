import org.sklrsn.utils.ShCommand

def call(Map config) {
    if (config.command?.trim()) {
        // Execute sh command
        def parsedCmd = ShCommand.parse(config.command)
        sh "${parsedCmd}"

        return
    }

    throw new RuntimeException('Incorrect usage of runShCommand.')
}
