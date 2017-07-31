import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "agile-management"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "org.eclipse.mylyn.github" % "org.eclipse.egit.github.core" % "2.1.5",
    "com.google.guava" % "guava" % "14.0.1",
    "redis.clients" % "jedis" % "2.1.0",
    javaCore,
    javaJdbc,
    javaEbean
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
