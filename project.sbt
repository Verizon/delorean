import verizon.build._

common.settings

publishing.ignore

ghrelease.settings

teamName in Global := Some("inf")

projectName in Global := Some("delorean")

lazy val delorean = project.in(file(".")).aggregate(docs, core)

lazy val core = project

lazy val docs = project
