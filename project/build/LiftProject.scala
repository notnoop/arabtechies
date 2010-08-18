import sbt._

class LiftProject(info: ProjectInfo) extends DefaultWebProject(info) {
  val liftVersion = "2.1-SNAPSHOT"

  val scalatoolsSnapshot =
    "Scala Tools Snapshot" at "http://scala-tools.org/repo-snapshots/"

  // If you're using JRebel for Lift development, uncomment
  // this line
  override def scanDirectories = Nil


  override def libraryDependencies = Set(
    "net.liftweb" %% "lift-webkit" % liftVersion % "compile->default",
    "net.liftweb" %% "lift-mapper" % liftVersion % "compile->default",
    "org.mortbay.jetty" % "jetty" % "6.1.22" % "test->default",
    "junit" % "junit" % "4.5" % "test->default",
    "org.scala-tools.testing" %% "specs" % "1.6.5" % "test->default",
    "com.h2database" % "h2" % "1.2.138"
  ) ++ super.libraryDependencies


  // Other non-Lift dependencies
  val t_repo = "t_repo" at "http://tristanhunt.com:8081/content/groups/public/"
  val tex_repo = "SnuggleTex_repo" at "http://www2.ph.ed.ac.uk/maven2"
  val knockoff = "com.tristanhunt" %% "knockoff" % "0.7.3-14"
}
