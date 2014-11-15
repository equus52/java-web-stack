def appName = "java-web-stack"
def patternStr = "%d{yyyy/MM/dd HH:mm:ss:SSS} [%level] [%thread] [%logger{0}] %msg%n"

appender("FILE", RollingFileAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "${patternStr}"
  }
  rollingPolicy(TimeBasedRollingPolicy) {
    fileNamePattern = "log/${appName}.%d{yyyy-MM-dd}.%i.log"
    timeBasedFileNamingAndTriggeringPolicy(SizeAndTimeBasedFNATP){
      maxFileSize = "100MB"
    }
    maxHistory = 30
  }
}

appender("CONSOLE", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
    pattern = "${patternStr}"
  }
}

logger("org.hibernate", INFO)
logger("org.hibernate.SQL", DEBUG)
logger("org.eclipse.jetty", INFO)

root(DEBUG, ["CONSOLE", "FILE"])