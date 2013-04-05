/*
 *
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

package uk.co.randomcoding.cucumber.generator

/**
 * Split out certain parts of a file read in as a Sequence of Strings (one per line)
 *
 * @author RandomCoder
 */
trait EntitySplitter {

  /**
   * Given a set of input lines, get the line ranges that contain whole parts of a Gherkin file
   *
   * @param lines The relevant section of the file for the splitter
   * @return The ordered groups of lines that can be further processed
   */
  def split(lines: Seq[String]): Seq[Seq[String]]
}
