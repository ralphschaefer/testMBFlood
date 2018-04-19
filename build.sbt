import Dependencies._

scalaVersion := "2.12.4"

lazy val commonSettings = Seq(
  organization := "org.mbFlood",
  version := "0.1",
  parallelExecution := false
)

lazy val assemblySettings = Seq(
  test in assembly := {},
  aggregate in assembly := false
)

lazy val common = (project in file("common"))
  .settings(
    inThisBuild(
      commonSettings
    ),
    assemblyJarName in assembly := "common.jar",
    assemblySettings,
    name := "common",
    libraryDependencies ++= commonLibs,
  )


lazy val module1 = (project in file("modules/module1"))
  .settings(
    inThisBuild(
      commonSettings
    ),
    assemblyJarName in assembly := "module1.jar",
    assemblySettings,
    name := "module1",
    libraryDependencies ++= commonLibs,
  )
  .dependsOn(common)

lazy val module2 = (project in file("modules/module2"))
  .settings(
    inThisBuild(
      commonSettings
    ),
    assemblyJarName in assembly := "module2.jar",
    assemblySettings,
    name := "module2",
    libraryDependencies ++= commonLibs,
  )
  .dependsOn(common)

lazy val root = (project in file("."))
  .settings(
    inThisBuild(
      commonSettings
    ),
    assemblyJarName in assembly := "mbFloodSeedNode.jar",
    assemblySettings,
    name := "mbFlood",
    libraryDependencies ++= commonLibs,
  )
  .dependsOn(common)
  .aggregate(common, module1, module2)


lazy val allProjects=Seq(
  common, root, module1, module2
).map(_.id)

addCommandAlias("cleanAll", allProjects.map(name => s"; $name/clean").mkString)

addCommandAlias("updateAll", allProjects.map(name => s"; $name/update").mkString)

addCommandAlias("compileAll", allProjects.map(name => s"; $name/compile").mkString)

addCommandAlias("testCompileAll", allProjects.map(name => s"; $name/test:compile").mkString)

addCommandAlias("testAll", allProjects.map(name => s"; $name/test").mkString)

addCommandAlias("assemblyAll", allProjects.map(name => s"; $name/assembly").mkString)

addCommandAlias("downloadSourcesAll", allProjects.map(name => s"; $name/updateClassifiers").mkString)
