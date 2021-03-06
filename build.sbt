import sbtcrossproject.{crossProject, CrossType}

val scalatestVersion = "3.0.5"
val akkaHttpVersion = "10.1.5"
lazy val server = (project in file("server")).settings(commonSettings).settings(
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % "2.5.11",
    "com.vmunier" %% "scalajs-scripts" % "1.1.2",
    "de.heikoseeberger" %% "akka-http-upickle" % "1.22.0",

    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % "test",
    "org.scalatest" %% "scalatest" % scalatestVersion % "test",
    "org.scalactic" %% "scalactic" % scalatestVersion % "test"
  ),
  WebKeys.packagePrefix in Assets := "public/",
  managedClasspath in Runtime += (packageBin in Assets).value,
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for Twirl templates are present
  EclipseKeys.preTasks := Seq(compile in Compile)
).enablePlugins(SbtWeb, SbtTwirl, JavaAppPackaging).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(commonSettings).settings(
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.5"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(commonSettings)
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

lazy val commonSettings = Seq(
  scalaVersion := "2.12.6",
  organization := "com.strawpay"
)

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen {s: State => "project server" :: s}
