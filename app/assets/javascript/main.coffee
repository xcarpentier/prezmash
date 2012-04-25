# Just a log helper
log = (args...) ->
    console.log.apply console, args if console.log?
