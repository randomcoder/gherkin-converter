/*
 * Copyright (C) 2017. RandomCoder <randomcoder@randomcoding.co.uk>
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
 */

package uk.co.randomcoding.cucumber.generator.html

import java.io.File

import uk.co.randomcoding.cucumber.generator.writer._

import scala.xml.NodeSeq

trait IndexHtml {

  def writeIndexFiles(baseDir: File, mainTitle: String): Unit = writeIndexFiles(baseDir, false, mainTitle)

  private[this] def writeIndexFiles(htmlDir: File, linkToParent: Boolean, title: String): Unit = {
    val htmlFeatureFiles = htmlDir.list().filter(_.endsWith(".feature.html"))
    val subDirectories = htmlDir.listFiles().filter(_.isDirectory)

    val featureFileLinks = htmlFeatureFiles.map(fileName => <li><a href={fileName}>{linkTextFromName(fileName)}</a></li>)
    val subDirectoryLinks = subDirectories.map { dir => <li><a href={dir.getName + "/index.html"}>{linkTextFromName(dir.getName)}</a></li> }
    val linksList = featureFileLinks ++ subDirectoryLinks

    if (linksList.nonEmpty) {
      writeHtml(IndexHtml(title, <ul>{featureFileLinks}</ul>, <ul>{subDirectoryLinks}</ul>, linkToParent), new File(htmlDir, "index.html"))
      subDirectories.foreach(subDirectory => writeIndexFiles(subDirectory, true, linkTextFromName(subDirectory.getName)))
    }
  }

  private[this] def linkTextFromName(name: String) = {
    name.takeWhile(_ != '.').replaceAll("""([A-Z0-9][a-z0-9])""", """ $1""").trim
  }
}

object IndexHtml {
  def apply(title: String, featureLinks: NodeSeq, subDirectoryLinks: NodeSeq, linkToParent: Boolean) = {
    <html lang="en">
      <head>
        <title>Features: {title}</title>
        {metaTags}
        {jquery}
        {bootstrap}
        {customCss}
      </head>
      <body>
        <div class="container">
          <div class="row">
            <div class="col col-sm-10 col-sm-offset-1">
              <div class="row">
                <div class="col col-sm-12 page_title" id="page_title">
                  {title}
                </div>
              </div>{ if (linkToParent)
              <div class="row">
                <div class="col col-sm-12 parent_link" id="parent_link">
                  <a href="../index.html">Up</a>
                </div>
              </div>}
              <div class="row">
                <div class="col col-sm-12 feature_link" id="feature_links">
                  {featureLinks}
                </div>
              </div>
              <div class="row">
                <div class="col col-sm-12 sub_directory_link" id="sub_directory_links">
                  {subDirectoryLinks}
                </div>
              </div>
            </div>
          </div>
        </div>
      </body>
    </html>
  }
}
