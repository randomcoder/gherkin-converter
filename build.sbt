import Dependencies._

name := "gherkin-converter"

organization :=  "uk.co.randomcoding"

version := "0.6.0-SNAPSHOT"

scalaVersion := "2.10.6"

scalacOptions in Compile ++= Seq("-feature", "-unchecked", "-explaintypes", "-deprecation")

libraryDependencies ++= loggingDependencies ++ jodaTimeDependencies ++ testDependencies ++ Seq(gherkin)//, scalaXml)

testOptions in Test  ++= Seq(Tests.Argument(TestFrameworks.ScalaTest, "-oDSHM"))

sonatypeProfileName := "uk.co.randomcoding"

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
      <id>randomcoder</id>
      <name>Random Coder</name>
      <url>https://github.com/randomcoder</url>
    </developer>
  </developers>
}
