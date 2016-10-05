
organization in Global := "io.verizon.delorean"

scalaVersion in Global := crossScalaVersions.value.head

crossScalaVersions in Global := Seq("2.11.7", "2.10.4")

lazy val delorean = project.in(file(".")).aggregate(core)

lazy val core = project

enablePlugins(DisablePublishingPlugin)
