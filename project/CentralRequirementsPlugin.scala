package verizon.build

import sbt._, Keys._
import xerial.sbt.Sonatype.autoImport.sonatypeProfileName

object CentralRequirementsPlugin extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = RigPlugin

  override lazy val projectSettings = Seq(
    sonatypeProfileName := "io.verizon",
    pomExtra in Global := {
      <developers>
        <developer>
          <id>djspiewak</id>
          <name>Daniel Spiewak</name>
          <url>http://github.com/djspiewak</url>
        </developer>
        <developer>
          <id>timperrett</id>
          <name>Timothy Perrett</name>
          <url>http://github.com/timperrett</url>
        </developer>
        <developer>
          <id>ceedubs</id>
          <name>Cody Allen</name>
          <url>https://github.com/ceedubs</url>
        </developer>
      </developers>
    },
    licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0.html")),
    homepage := Some(url("http://verizon.github.io/delorean/")),
    scmInfo := Some(ScmInfo(url("https://github.com/verizon/delorean"),
                                "git@github.com:verizon/delorean.git"))
  )
}
