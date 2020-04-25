name := "finagle"

version := "0.1"

scalaVersion := "2.12.11"

val netty4Version = "4.1.35.Final"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

libraryDependencies ++= Seq(
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.google.guava" % "guava" % "27.1-jre",
  "org.apache.commons" % "commons-lang3" % "3.8.1",
  /*"com.twitter" %% "finagle-http" % "19.5.1",
  "com.twitter" %% "finagle-core" % "19.5.1",*/
  "com.twitter" %% "finagle-http" % "20.4.0",
  "com.twitter" %% "finagle-core" % "20.4.0",
  "io.netty" % "netty-all" % netty4Version
)

enablePlugins(AkkaGrpcPlugin)
