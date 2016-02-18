import verizon.build._

common.settings

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core"       % "7.1.2",
  "org.scalaz" %% "scalaz-concurrent" % "7.1.2",

  "org.scalacheck" %% "scalacheck" % "1.12.5" % "test")

scalaTestVersion := "2.2.6"