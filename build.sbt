val sparkVersion = "3.2.0"

val spark = Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion % Provided,
  "org.apache.spark" %% "spark-sql" % sparkVersion % Provided
)

val logging = Seq(
  "org.slf4j" % "slf4j-api" % "2.0.0-alpha4",
  "ch.qos.logback" % "logback-classic" % "1.3.0-alpha10",
  "ch.qos.logback" % "logback-core" % "1.3.0-alpha10",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4"
)

val config = Seq("com.typesafe" % "config" % "1.4.1", "com.github.andr83" %% "scalaconfig" % "0.7")

val test = Seq(
  "org.scalactic" %% "scalactic" % "3.2.10" % Test,
  "org.scalatest" %% "scalatest" % "3.2.10" % Test
)

lazy val spark_example = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .settings(
    name := "spark_example",
    version := "0.0.1-SNAPSHOT",
    libraryDependencies ++= logging,
    libraryDependencies ++= spark,
    libraryDependencies ++= config,
    libraryDependencies ++= test,
    scalaVersion := "2.13.6"
  )

//set spark_example / Test / classLoaderLayeringStrategy := ClassLoaderLayeringStrategy.Flat

scalacOptions ++= Seq(
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-explaintypes", // Explain type errors in more detail.
  "-Xfatal-warnings", // Fail the compilation if there are any warnings.
  "-Xsource:3", // Warn for Scala 3 features
  "-Ywarn-dead-code" // Warn when dead code is identified.
)

javacOptions ++= Seq("-source", "11", "-target", "11", "-Xlint")

assembly / assemblyShadeRules := Seq(
  ShadeRule.rename("org.apache.kafka.**" -> "kk.@1").inAll,
  ShadeRule.rename("org.apache.spark.streaming.kafka010.**" -> "skk.@1").inAll
)

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*)       => MergeStrategy.discard
  case n if n.startsWith("reference.conf") => MergeStrategy.concat
  case _                                   => MergeStrategy.first
}
