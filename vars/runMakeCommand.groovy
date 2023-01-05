import org.sklrsn.actions.MakeCommand

def call(Map config) {
    if (config.command?.trim()) {
        // Execute make command
        def command = MakeCommand.prepare(config.path, config.command)
        sh "${command}"

        return
    }

    throw new RuntimeException('Incorrect usage of runMakeCommand')
}
