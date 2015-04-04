lazy val personalSettings = Seq(
  organization := "org.github.jmora",
  version := "0.0.1",
  scalaVersion := "2.11.5" 
)

lazy val mainapp = Some("com.github.jmora.scala.util.App")
lazy val maintest = Some("com.github.jmora.scala.util.TestSuite")

lazy val preferredSettings = Seq(
    // pollInterval := 1000,
    javacOptions ++= Seq("-source", "jvm-7", "-target", "jvm-7"),
    scalacOptions += "-deprecation",
    scalaSource in Compile := baseDirectory.value / "src" / "main",
    scalaSource in Test := baseDirectory.value / "src" / "test",
    // watchSources += baseDirectory.value / "input",
    ivyLoggingLevel := UpdateLogging.Full,
    // javaOptions += "-Xmx4G",
    shellPrompt in ThisBuild := { state => Project.extract(state).currentRef.project + "> " },
    shellPrompt := { state => System.getProperty("user.name") + "> " }    
)

lazy val root = (project in file(".")).
  settings(personalSettings: _*).
  settings(preferredSettings: _*).
  settings(
    name := "scala.util",
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % Test,    
    mainClass in (Compile, packageBin) := mainapp,
    mainClass in (Compile, assembly) := mainapp,
    mainClass in (Compile, run) := mainapp,
    mainClass in (Test, run) := maintest
  )
  
/*
val mainrun = Some("com.github.jmora.scala.util.App")
val maintest = Some("com.github.jmora.scala.util.TestSuite")


lazy val root = (project in file(".")).
  settings(
    name := "scala.util",
    version := "0.0.1",
    scalaVersion := "2.11.5",
    mainClass in Compile := mainrun,
    mainClass in assembly := mainrun,
    mainClass in run := mainrun,
    mainClass in test := maintest
  )
  
  */