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

  private[this] val scalaLoggingVersion = "3.7.2"
  private[this] val scalatestVersion = "3.0.3"
  private[this] val logbackVersion = "1.2.3"
  private[this] val groovyVersion = "2.4.10"
  private[this] val scalacheckVersion = "1.13.5"
  private[this] val scalaXmlVersion = "1.0.6"

  private[this] val exclude = (org: String, packageName: String) => ExclusionRule(organization = org, name = packageName)

  val excludeLog4j = exclude("log4j", "log4j")

  lazy val scalatest = "org.scalatest" %% "scalatest" % scalatestVersion % "test" excludeAll excludeLog4j
  lazy val scalacheck = "org.scalacheck" %% "scalacheck" % scalacheckVersion % "test"

  lazy val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
  lazy val logback = "ch.qos.logback" % "logback-classic" % logbackVersion
  lazy val groovy = "org.codehaus.groovy" % "groovy" % groovyVersion

  lazy val scalaXml = "org.scala-lang.modules" %% "scala-xml" % scalaXmlVersion

  val compileDependencies = Seq(logback, groovy, scalaXml)
  val testDependencies = Seq(scalatest, scalacheck)
}
