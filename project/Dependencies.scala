/*
 * Copyright (C) 2013 RandomCoder <randomcoder@randomcoding.co.uk>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 * RandomCoder - initial API and implementation and/or initial documentation
 */

import sbt._

/**
 * Declaration of dependencies, library versions etc.
 */
object Dependencies {

  // Define version numbers of libraries that could (and should) change here
  val scalaLoggingVersion = "1.0.1"
  val scalatestVersion = "2.0.M5b"
  val logbackVersion = "1.0.0"
  val jodaTimeVersion = "2.1"
  val jodaConvertVersion = "1.2"
  val groovyVersion = "2.0.5"

  /**
   * This is used to add an exclude to a dependency. See jodaTime for an example
   */
  val exclude = (org: String, packageName: String) => ExclusionRule(organization = org, name = packageName)

  // We want to exclude log4j where possible as we are using logback for logging
  lazy val excludeLog4j = exclude("log4j", "log4j")

  // Use Joda Time rather than the java.util.Date classes
  val jodaTime = "joda-time" % "joda-time" % jodaTimeVersion excludeAll (
    excludeLog4j)

  val jodaConvert = "org.joda" % "joda-convert" % jodaConvertVersion excludeAll (
    excludeLog4j)

  // For testing we use Scalatest and scalatra-scalatest for the web app. See http://scalatest.org
  val scalatest = "org.scalatest" %% "scalatest" % scalatestVersion % "test" excludeAll (
    excludeLog4j)

  // Logging. Uses logback as the logging backend, with groovy for configuration.
  // Grizzled is used to provide access to the logging API in code
  val scalaLogging = "com.typesafe" %% "scalalogging-slf4j" % scalaLoggingVersion excludeAll (
    excludeLog4j)

  val logback = "ch.qos.logback" % "logback-classic" % logbackVersion
  val groovy = "org.codehaus.groovy" % "groovy" % groovyVersion

  val gherkin = "info.cukes" % "gherkin" % "2.11.6"

  // Define the commonly used dependency collections
  val loggingDependencies = Seq(logback, groovy, scalaLogging)
  val jodaTimeDependencies = Seq(jodaTime, jodaConvert)
  val testDependencies = Seq(scalatest)
}
