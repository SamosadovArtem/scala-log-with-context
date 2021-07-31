name := "logExample"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies += "tf.tofu" %% "tofu" % "0.10.3"

libraryDependencies ++= Seq(
  "com.evolutiongaming"        %% "cats-helper"         % "2.3.0",
  "org.http4s"                 %% "http4s-blaze-server" % "0.23.0",
  "org.http4s"                 %% "http4s-dsl"          % "0.23.0",
  "org.typelevel"              %% "cats-core"           % "2.3.1",
  "org.typelevel"              %% "cats-effect"         % "2.5.1",
  "org.slf4j"                   % "log4j-over-slf4j"    % "1.7.26",
  "org.slf4j"                   % "jcl-over-slf4j"      % "1.7.26",
  "com.typesafe.scala-logging" %% "scala-logging"       % "3.9.2",
)
