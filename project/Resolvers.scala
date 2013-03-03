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
 * Contains resolvers for use within the project
 */
object Resolvers {
  /**
   * This is a repo hosted on spdev to hold our own libraries etc.
   */
  val spdevArchivaRepo = "SyBard" at "http://jira/archiva/repository/SyBard/"

  /**
   * This is a local mirror of external repositories - should mean we don't need to use a proxy.
   */
  val spdevMirrorRepo = "SpDev Mirror" at "http://jira/archiva/repository/internal/"

  /**
   * Additional repositories to use when resolving dependencies
   */
  val repos = Seq(
    spdevMirrorRepo,
    "Excilys" at "http://repository.excilys.com/content/groups/public",
    Classpaths.typesafeReleases,
    spdevArchivaRepo)

  /**
   * Credentials required to publish to the local archiva repository
   */
  val spdevArchivaCredentials = Credentials("Repository Archiva Managed SyBard Repository", "jira", "archiva-upload", "12aardvark")
}
