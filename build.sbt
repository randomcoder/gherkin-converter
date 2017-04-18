import Dependencies._

name := "gherkin-converter"

organization :=  "uk.co.randomcoding"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.12.2"

scalacOptions in Compile ++= Seq("-feature", "-unchecked", "-explaintypes", "-deprecation")

libraryDependencies ++= loggingDependencies ++ jodaTimeDependencies ++ testDependencies ++ Seq(gherkin)

testOptions in Test  ++= Seq(
  Tests.Argument(TestFrameworks.ScalaTest, "-oDSHM"),
  Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/xmlresults/"),
  Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/htmlresults/")
)
