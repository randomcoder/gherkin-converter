import Dependencies._

name := "gherkin-converter"

organization :=  "uk.co.randomcoding"

version := "0.6.3-SNAPSHOT"

scalaVersion := "2.10.6"

scalacOptions in Compile ++= Seq("-feature", "-unchecked", "-deprecation", "-target:jvm-1.6")

libraryDependencies ++= loggingDependencies ++ testDependencies

testOptions in Test  ++= Seq(Tests.Argument(TestFrameworks.ScalaTest, "-oDSHM"))

sonatypeProfileName := "uk.co.randomcoding"

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra in Global := {
  <url>https://github.com/randomcoder/gherkin-converter</url>
  <licenses>
    <license>
      <name>AGPL v3</name>
      <url>https://www.gnu.org/licenses/agpl-3.0.en.html</url>
    </license>
  </licenses>
  <scm>
    <connection>scm:git:github.com/randomcoder/gherkin-converter.git</connection>
    <developerConnection>scm:git:git@github.com:randomcoder/gherkin-converter.git</developerConnection>
    <url>github.com/randomcoder/gherkin-converter</url>
  </scm>
  <developers>
    <developer>
      <name>Random Coder</name>
      <email>randomcoder@randomcoding.co.uk</email>
      <organization>uk.co.randomcoding</organization>
      <organizationUrl>https://github.com/randomcoder</organizationUrl>
    </developer>
  </developers>
}
