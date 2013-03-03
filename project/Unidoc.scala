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
import Project.Initialize

/**
 * Originally from https://github.com/jboner/akka/blob/master/project/Unidoc.scala
 */
object Unidoc {
  val unidocDirectory = SettingKey[File]("unidoc-directory")
  val unidocExclude = SettingKey[Seq[String]]("unidoc-exclude")
  val unidocAllSources = TaskKey[Seq[Seq[File]]]("unidoc-all-sources")
  val unidocSources = TaskKey[Seq[File]]("unidoc-sources")
  val unidocAllClasspaths = TaskKey[Seq[Classpath]]("unidoc-all-classpaths")
  val unidocClasspath = TaskKey[Seq[File]]("unidoc-classpath")
  val unidoc = TaskKey[File]("unidoc", "Create unified scaladoc for all aggregates")

  lazy val settings = Seq(
    unidocDirectory <<= crossTarget / "unidoc",
    unidocExclude := Seq.empty,
    unidocAllSources <<= (thisProjectRef, buildStructure, unidocExclude) flatMap allSources,
    unidocSources <<= unidocAllSources map { _.flatten },
    unidocAllClasspaths <<= (thisProjectRef, buildStructure, unidocExclude) flatMap allClasspaths,
    unidocClasspath <<= unidocAllClasspaths map { _.flatten.map(_.data).distinct },
    unidoc <<= unidocTask)

  def allSources(projectRef: ProjectRef, structure: Load.BuildStructure, exclude: Seq[String]): Task[Seq[Seq[File]]] = {
    val projects = aggregated(projectRef, structure, exclude)
    projects.flatMap { sources in Compile in LocalProject(_) get structure.data }.join
  }

  def allClasspaths(projectRef: ProjectRef, structure: Load.BuildStructure, exclude: Seq[String]): Task[Seq[Classpath]] = {
    val projects = aggregated(projectRef, structure, exclude)
    projects.flatMap { dependencyClasspath in Compile in LocalProject(_) get structure.data }.join
  }

  def aggregated(projectRef: ProjectRef, structure: Load.BuildStructure, exclude: Seq[String]): Seq[String] = {
    val aggregate = Project.getProject(projectRef, structure).toSeq.flatMap(_.aggregate)
    aggregate flatMap { ref =>
      if (exclude contains ref.project) Seq.empty
      else ref.project +: aggregated(ref, structure, exclude)
    }
  }

  def unidocTask: Initialize[Task[File]] = {
    (compilers, cacheDirectory, unidocSources, unidocClasspath, unidocDirectory, scalacOptions in doc, streams) map {
      (compilers, cache, sources, classpath, target, options, s) =>
        {
          val scaladoc = new Scaladoc(100, compilers.scalac)
          scaladoc.cached(cache / "unidoc", "main", sources, classpath, target, options, s.log)
          target
        }
    }
  }
}
