import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "prezmash"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "mysql" % "mysql-connector-java" % "5.1.18",
      "org.twitter4j" % "twitter4j-core" % "2.2.5"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here      
    )

}
