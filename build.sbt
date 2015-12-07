import com.typesafe.config.{Config, ConfigFactory}

name          := """akka-http-slick"""
organization  := "com.example"
version       := "1.0.0"
scalaVersion  := "2.11.7"
scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaStreamV      = "2.0-M2"
  val scalaTestV       = "3.0.0-M12"
  val scalaMockV       = "3.2.2"
  val scalazScalaTestV = "0.2.3"
  val slickVersion     = "3.1.0"
  Seq(
    "com.typesafe.akka"  %% "akka-stream-experimental"             % akkaStreamV,
    "com.typesafe.akka"  %% "akka-http-core-experimental"          % akkaStreamV,
    "com.typesafe.akka"  %% "akka-http-spray-json-experimental"    % akkaStreamV,
    "com.typesafe.slick" %% "slick"                                % slickVersion,
    "com.typesafe.slick" %% "slick-codegen"                        % slickVersion,
    "org.slf4j"          %  "slf4j-nop"                            % "1.6.4",
    "org.postgresql"     %  "postgresql"                           % "9.4-1201-jdbc41",
    "org.mindrot"        %  "jbcrypt"                              % "0.3m",
    "org.flywaydb"       %  "flyway-core"                          % "3.2.1",
    "org.scalatest"      %% "scalatest"                            % scalaTestV       % "it,test",
    "org.scalamock"      %% "scalamock-scalatest-support"          % scalaMockV       % "it,test",
    "com.typesafe.akka"  %% "akka-http-testkit-experimental"       % akkaStreamV      % "it,test"
  )
}

lazy val root = project.in(file(".")).configs(IntegrationTest)
Defaults.itSettings

lazy val appProperties = settingKey[Config]("The application properties")
//appProperties := ConfigFactory.parseFile(baseDirectory.value / "src/main/resources" / "application.conf").resolve()
appProperties := ConfigFactory.parseFile((resourceDirectory in Compile).value / "application.conf").resolve()
version := appProperties.value.getString("app.version")

lazy val myTask = taskKey[Unit]("Prints application version'")
myTask := {
  streams.value.log.warn(s"reset app version to $version")
}

// slick code generation task, call in sbt using "slick-codegen"
lazy val slick = TaskKey[Seq[File]]("slick-codegen")

lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
  val outputDir = (dir / "slick").getPath // place generated files in sbt's managed sources folder
val url = "jdbc:postgresql://localhost/test"
//val url = "jdbc:h2:mem:test;INIT=runscript from 'src/main/sql/create.sql'" // connection info for a pre-populated throw-away, in-memory db for this demo, which is freshly initialized on every run
val jdbcDriver = "org.postgresql.Driver" //"org.h2.Driver"
  val slickDriver =  "slick.driver.PostgresDriver"
  val pkg = "demo"
  val user = "psql_app"
  val password = "app_psql_pass"
  toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg, user, password), s.log))
  val fname = outputDir + "/demo/Tables.scala"
  Seq(file(fname))
}

//lazy val slickCodeGenTask = (scalaSource in Compile, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
//  toError(r.run("SlickCodeGenerator", cp.files, Array[String](), s.log))
//  val f = new File("app/models/auto_generated/Models.scala")
//  Seq(file(f.getAbsolutePath))
//}
slick <<= slickCodeGenTask

Revolver.settings
enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

dockerExposedPorts := Seq(9000)

dockerEntrypoint := Seq("bin/%s" format executableScriptName.value, "-Dconfig.resource=docker.conf")

parallelExecution in Test := false

fork in run := true

