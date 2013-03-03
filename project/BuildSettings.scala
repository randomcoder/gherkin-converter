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
import Keys._

/**
 * Common build settings for projects.
 */
object BuildSettings {
  import Resolvers._
  import net.virtualvoid.sbt.graph.Plugin._

  val buildOrganization = "uk.co.randomcoding"
  val buildVersion = "1.0.0-SNAPSHOT"
  val buildScalaVersion = "2.10.0"

  // Defines the default settigs to use for projects
  lazy val buildSettings: Seq[Project.Setting[_]] = Defaults.defaultSettings ++ commonSettings ++ pluginGraphSettings ++ testSettings ++ sourceDirSettings

  // Settings for the dependency graph plugin
  private val pluginGraphSettings = graphSettings

  // Settings that are common to all projects
  private val commonSettings: Seq[Project.Setting[_]] = Seq(
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    shellPrompt := ShellPrompt.buildShellPrompt,
    scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-language:implicitConversions"),
    resolvers ++= repos,
    publishTo := Some(spdevArchivaRepo),
    credentials += spdevArchivaCredentials)

  // Set the default, Scala only source directories
  private val sourceDirSettings = Seq(
    unmanagedSourceDirectories in Compile <<= (scalaSource in Compile)(Seq(_)),
    unmanagedSourceDirectories in Test <<= (scalaSource in Test)(Seq(_)))

  // Default settings for tests. Disable parallel tests
  private val testSettings = Seq(
    parallelExecution in Test := false)
}

