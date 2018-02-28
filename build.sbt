import scala.util.Try


name := "sews"

organization := "im.mange"

version := Try(sys.env("TRAVIS_BUILD_NUMBER")).map("0.0." + _).getOrElse("1.0-SNAPSHOT")

scalaVersion := "2.12.4"

resolvers ++= Seq(
  "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/",
  "Tim Tennant's repo" at "http://dl.bintray.com/timt/repo/"
)

//TODO: mske it possible to run me by myself ... or maybe just add examples ...
//TODO: upgrade jetty
//TODO: make at least jetty "provided", ideally others too
libraryDependencies ++= Seq(
  "io.shaka" %% "naive-http" % "94",
  "org.eclipse.jetty.websocket" % "websocket-server" % "9.2.10.v20150310", // % "provided",
  //9.4.8.v20171121 - see http://central.maven.org/maven2/org/eclipse/jetty/jetty-distribution/
  "com.github.alexarchambault" %% "argonaut-shapeless_6.2" % "1.2.0-M4",
  "org.reactormonk" % "elmtypes_2.12" % "0.4",
  "im.mange" %% "little" % "[0.0.49,0.0.999]" % "provided"
)

sonatypeSettings

publishTo <<= version { project_version ⇒
  val nexus = "https://oss.sonatype.org/"
  if (project_version.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishMavenStyle := true

publishArtifact in Test := false

homepage := Some(url("https://github.com/alltonp/sews"))

licenses +=("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0.html"))

credentials += Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", System.getenv("SONATYPE_USER"), System.getenv("SONATYPE_PASSWORD"))

pomExtra :=
    <scm>
      <url>git@github.com:alltonp/sews.git</url>
      <connection>scm:git:git@github.com:alltonp/sews.git</connection>
    </scm>
    <developers>
      <developer>
        <id>alltonp</id>
      </developer>
    </developers>
