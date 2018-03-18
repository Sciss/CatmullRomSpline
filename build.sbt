name               := "CatmullRomSpline"
organization       := "de.sciss"
version            := "0.1.0"
scalaVersion       := "2.12.4"
crossScalaVersions := Seq("2.12.4", "2.11.12")
licenses           := Seq("Apache License 2.0" -> url("https://raw.githubusercontent.com/Sciss/CatmullRomSpline/master/LICENSE"))
homepage           := Some(url(s"https://github.com/Sciss/${name.value}"))
description        := "Catmull Rom spline interpolation algorithm"
scalacOptions     ++= Seq("-Xlint", "-deprecation", "-unchecked", "-feature", "-encoding", "utf8", "-Xfuture")

libraryDependencies += "de.sciss" %% "swingplus" % "0.2.4" % "test"

// ---- publishing ----

publishMavenStyle := true

publishTo :=
  Some(if (isSnapshot.value)
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  else
    "Sonatype Releases"  at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
  )

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := { val n = name.value
<scm>
  <url>git@github.com:Sciss/{n}.git</url>
  <connection>scm:git:git@github.com:Sciss/{n}.git</connection>
</scm>
<developers>
  <developer>
    <id>StephanHener</id>
    <name>Stephan Hener</name>
    <url>https://github.com/StephanHener</url>
  </developer>
  <developer>
    <id>sciss</id>
    <name>Hanns Holger Rutz</name>
    <url>http://www.sciss.de</url>
  </developer>
</developers>
}

