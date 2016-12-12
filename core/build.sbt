
val scalazVersion = sys.env.getOrElse("SCALAZ_VERSION", "7.1.11")
val scalazMajorVersion = scalazVersion.take(3)

libraryDependencies ++= {

  Seq(
    "org.scalaz" %% "scalaz-core"       % scalazVersion,
    "org.scalaz" %% "scalaz-concurrent" % scalazVersion
  )
}

// Add scalaz version to project version
version := {
  version.value match {
    case v if v.endsWith("-SNAPSHOT") =>
      val replacement = s"-scalaz-$scalazMajorVersion-SNAPSHOT"
      v.replaceAll("-SNAPSHOT", replacement)
    case v =>
      s"${v}-scalaz-$scalazMajorVersion"
  }
}

unmanagedSourceDirectories in Compile +=
  (sourceDirectory in Compile).value /
  (s"scala-scalaz-$scalazMajorVersion.x")
