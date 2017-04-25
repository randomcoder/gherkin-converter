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

  val scalaLoggingVersion = "3.5.0"
  val scalatestVersion = "3.0.1"
  val logbackVersion = "1.2.3"
  val jodaTimeVersion = "2.9.9"
  val jodaConvertVersion = "1.8.1"
  val groovyVersion = "2.4.10"
  val gherkinVersion = "2.12.1"
  val scalacheckVersion = "1.13.4"

  val exclude = (org: String, packageName: String) => ExclusionRule(organization = org, name = packageName)

  val excludeLog4j = exclude("log4j", "log4j")

  lazy val scalatest = "org.scalatest" %% "scalatest" % scalatestVersion % "test" excludeAll excludeLog4j
  lazy val scalacheck = "org.scalacheck" %% "scalacheck" % scalacheckVersion % "test"

  lazy val logback = "ch.qos.logback" % "logback-classic" % logbackVersion
  lazy val groovy = "org.codehaus.groovy" % "groovy" % groovyVersion

  val loggingDependencies = Seq(logback, groovy)
  val testDependencies = Seq(scalatest, scalacheck)
}
