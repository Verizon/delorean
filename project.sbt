import verizon.build._

common.settings

publishing.ignore

ghrelease.settings

teamName in Global := Some("inf")

projectName in Global := Some("delorean")

// removing docs for now, as there are none
lazy val delorean = project.in(file(".")).aggregate(core)

lazy val core = project

lazy val docs = project
