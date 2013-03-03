// Add dependency on the sbteclipse plugin to manage eclipse type projects from the sbt config.

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.1.0")

// Web plugin settings
libraryDependencies <+= sbtVersion(v => v match {
  case "0.11.3" => "com.github.siasia" %% "xsbt-web-plugin" % "0.11.3-0.2.11.1"
  case x if (x.startsWith("0.12")) => "com.github.siasia" %% "xsbt-web-plugin" % "0.12.0-0.2.11.1"
})

// Add assembly capability - executable jar fiel creation
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.8.4")

// Enabel running of project from a script in the project
addSbtPlugin("com.typesafe.sbt" % "sbt-start-script" % "0.6.0")

// Enables creation of a dependency graph for projects
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.0")
