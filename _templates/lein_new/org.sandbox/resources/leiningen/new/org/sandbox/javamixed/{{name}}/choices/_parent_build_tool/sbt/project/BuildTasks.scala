import sbt._
import Keys._
import Process._

import Settings._

object BuildTasks {
	//lazy val print = task {log.info("add task"); None}.
	//	dependsOn(compile) describedAs("task print: prints a line after compile")
	//lazy val sampleTask = TaskKey[Unit]("sampleTask", "prints a line")
    lazy val copyDepns = TaskKey[Unit]("copyDepns", "copy depns to {tgtDir}/lib [-DdepnSymlink=%s]".format(sys.props.getOrElse("depnSymlink", "true")))
	lazy val javadoc = TaskKey[Unit]("javadoc", "execute javadoc")
	lazy val checkstyle = TaskKey[Unit]("checkstyle", "execute checkstyle")
	lazy val scalastyle = TaskKey[Unit]("scalastyle", "execute scalastyle")
    lazy val cover = TaskKey[Unit]("cover", "execute jacoco cover")
	lazy val report = TaskKey[Unit]("report", "execute jacoco report")
	
	lazy val otherTasks = Seq(
        copyDepns := {
            if ("true" == sys.props.getOrElse("depnSymlink", "true")) {
                file(target.value.toString + "/lib").mkdirs
                val files: Seq[File] = (dependencyClasspath in Test)
                    .value.files.filter(!_.isDirectory)
                files.foreach { f =>
                    val linkPath = java.nio.file.Paths.get(target.value
                        .toString, "lib", f.getName)
                    try {
                        java.nio.file.Files.createSymbolicLink(linkPath,
                            f.toPath)
                    } catch {
                        case exc: java.io.IOException =>
                            //exc.printStackTrace()
                            println(exc.toString)
                    }
                }
            } else {
                val files: Seq[File] = (dependencyClasspath in Test).value
                    .files.filter(!_.isDirectory)
                files.foreach(f => IO.copyFile(f, file("%s/lib/%s".format(
                    target.value.toString, f.getName()))))
            }}
		, javadoc := {
            lazy val filesJava = ({(baseDirectory.value / "src/main/java") ** 
                "*.java"} getPaths).reduceOption(_ + " " + _).getOrElse("")
            Process("javadoc -use -private -version -author -d %s/target/docs %s".
                format(baseDirectory.value.toString, filesJava)).!
            }
		, checkstyle := {
            /*Process("checkstyle -c %s/config/sun_checks.xml %s/src/main/java".
                format(baseDirectory.value.toString,
                baseDirectory.value.toString)).!*/
            Process("java -jar %s/.ant/lib/ivy.jar -mode dynamic -types jar -confs default -dependency com.puppycrawl.tools checkstyle [5.5,) -main com.puppycrawl.tools.checkstyle.Main -- -c %s/config/sun_checks.xml %s/src/main/java".
                format(sys.env.getOrElse("HOME", "."),
                baseDirectory.value.toString, 
                baseDirectory.value.toString)).!
            }
		, scalastyle := {
            Process("java -jar %s/.ant/lib/ivy.jar -mode dynamic -types jar -confs default -dependency org.scalastyle scalastyle_%s [0.1.0,) -main org.scalastyle.Main -- -c %s/config/scalastyle_config.xml %s/src/main/scala"
                .format(sys.env.getOrElse("HOME", "."),
                sys.props.getOrElse("scala.compat", "2.9"),
                baseDirectory.value.toString, baseDirectory.value.toString)).!
            }
		, cover := {
            /*Process("java -Djava.library.path=%s -javaagent:%s/.ant/lib/jacocoagent-runtime.jar=destfile=%s/target/jacoco.exec -jar %s".format(
                sys.props.getOrElse("java.library.path", "."),
                sys.env.getOrElse("HOME", "."), baseDirectory.value.toString,
                (artifactPath in (Test, packageBin)).value.toString)).!*/
            Process("ant -f %s/build-jacoco.xml -Djava.library.path=%s -Ddist_test.jar=%s cover".format(
                baseDirectory.value.toString,
                sys.props.getOrElse("java.library.path", "."),
                (artifactPath in (Test, packageBin)).value.toString)).!
            }
        , report := {
            Process("ant -f %s/build-jacoco.xml -Ddist.jar=%s report".format(
                baseDirectory.value.toString,
                (artifactPath in (Compile, packageBin)).value.toString)).!
            }
        //, sampleTask := println("sample task")
	)
}
