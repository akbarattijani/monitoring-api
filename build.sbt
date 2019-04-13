name := "MonitoringAPI"

version := "1.0"

lazy val `monitoringapi` = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  "mysql" % "mysql-connector-java" % "6.0.4",
  jdbc , cache , ws   , specs2 % Test
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"  