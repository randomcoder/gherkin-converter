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

/**
 * Build Settings file for Diode Testing Projects
 */

import sbt._
import Keys._

import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseKeys
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseCreateSrc
import com.typesafe.sbteclipse.plugin.EclipsePlugin.EclipseCreateSrc.ValueSet

import com.typesafe.sbt.SbtStartScript

import sbtassembly.Plugin._
import AssemblyKeys._

object ProjectBuild extends Build {
  import BuildSettings._
  import ShellPrompt._
  import Dependencies._

  /**
   * Define the project name here
   *
   * This is used for all components and the main project.
   */
  val projectName = "gherkin-converter"

  /**
   * The root project.
   *
   * This aggregates all the sub projects and provides top level project operations
   * as well as a ''Unidoc'' task to generate the ScalaDoc for all projects in a single output directory.
   */
  lazy val root = Project(projectName,
    file("."),
    settings = buildSettings ++ Unidoc.settings ++ Seq(
      scalacOptions in doc := Seq(),
      publish := {},
      publishLocal := {})) aggregate (coreProject, htmlGenerator, logging)

  /**
   * The core project with support for the start script and assembly plugins.
   *
   * These plugins provide support for deployment and running either from the repo itself
   * or from an executable jar
   */
  lazy val coreProject: Project = Project("%s-core".format(projectName),
    file("core"),
    settings = buildSettings ++ SbtStartScript.startScriptForJarSettings ++ assemblySettings ++ Seq(
      name := "%s-core".format(projectName),
      libraryDependencies ++= coreProjectDependencies,
      jarName in assembly := "%s-core-%s.jar".format(projectName, buildVersion),
      mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
        {
          case "about.html" => MergeStrategy.rename
          case x => old(x)
        }
      })) dependsOn (logging)

  /**
   * Addon project to convert from the core format to HTML
   */
  lazy val htmlGenerator: Project = Project("%s-html".format(projectName),
    file("html-generator"),
    settings = buildSettings ++ SbtStartScript.startScriptForJarSettings ++ assemblySettings ++ Seq(
      name := "%s-html".format(projectName),
      libraryDependencies ++= otherProjectDependencies,
      jarName in assembly := "%s-%s.jar".format(projectName, buildVersion),
      mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
        {
          case "about.html" => MergeStrategy.rename
          case x => old(x)
        }
      })) dependsOn(logging, coreProject)

  /**
   * Separate project for logging. This prevents multiple projects' logging configurations conflicting
   */
  lazy val logging: Project = Project("%s-logging".format(projectName),
    file("logging"),
    settings = buildSettings ++ Seq(
      name := "%s-logging".format(projectName),
      libraryDependencies ++= loggingDependencies,
      EclipseKeys.createSrc := resourceOnlySourceDirs))

  // Eclipse source directories that only create the resources directories under /src/main and /src/test not the scala or java directories
  lazy val resourceOnlySourceDirs: ValueSet = EclipseCreateSrc.ValueSet(EclipseCreateSrc.Unmanaged, EclipseCreateSrc.Resource)

  // common dependencies for all projects - testing & joda time
  val baseDependencies = testDependencies ++ jodaTimeDependencies

  // Dependencies for the core project
  val coreProjectDependencies = baseDependencies

  // Dependencies for the other project
  val otherProjectDependencies = baseDependencies

}
