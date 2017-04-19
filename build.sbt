import Dependencies._

name := "gherkin-converter"

organization :=  "uk.co.randomcoding"

version := "0.5.0-SNAPSHOT"

scalaVersion := "2.12.2"

scalacOptions in Compile ++= Seq("-feature", "-unchecked", "-explaintypes", "-deprecation")

libraryDependencies ++= loggingDependencies ++ jodaTimeDependencies ++ testDependencies ++ Seq(gherkin)

crossScalaVersions := Seq("2.12.2", "2.11.10")

testOptions in Test  ++= Seq(
  Tests.Argument(TestFrameworks.ScalaTest, "-oDSHM")
)

// Your profile name of the sonatype account. The default is the same with the organization value
sonatypeProfileName := "uk.co.randomcoding"

// To sync with Maven central, you need to supply the following information:
pomExtra in Global := {
  <url>https://github.com/randomcoder/gherkin-converter</url>
  <licenses>
    <license>
      <name>AGPL v3</name>
      <url>https://www.gnu.org/licenses/agpl-3.0.en.html</url>
    </license>
  </licenses>
  <scm>
    <connection>scm:git:github.com/randomcoder/gherkin-converter</connection>
    <developerConnection>scm:git:git@github.com:randomcoder/gherkin-converter.git</developerConnection>
    <url>github.com/randomcoder/gherkin-converter</url>
  </scm>
  <developers>
    <developer>
      <id>randomcoder</id>
      <name>Random Coder</name>
      <url>https://github.com/randomcoder</url>
    </developer>
  </developers>
}
