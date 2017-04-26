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

import uk.co.randomcoding.cucumber.generator.html.FeatureHtml.customCss
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
      writeHtml(indexHtml(title, <ul>{featureFileLinks}</ul>, <ul>{subDirectoryLinks}</ul>, linkToParent), new File(htmlDir, "index.html"))
      subDirectories.foreach(subDirectory => writeIndexFiles(subDirectory, true, linkTextFromName(subDirectory.getName)))
    }
  }

  private[this] def indexHtml(title: String, featureLinks: NodeSeq, subDirectoryLinks: NodeSeq, linkToParent: Boolean) = {
    <html lang="en">
      <head>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <title>Features: {title}</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous"/>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous"/>
        <script src="https://code.jquery.com/jquery-2.2.4.min.js" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

        <style type="text/css">
          {customCss}
        </style>
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

  private[this] def linkTextFromName(name: String) = {
    name.takeWhile(_ != '.').replaceAll("""([A-Z0-9][a-z0-9])""", """ $1""").trim
  }
}
