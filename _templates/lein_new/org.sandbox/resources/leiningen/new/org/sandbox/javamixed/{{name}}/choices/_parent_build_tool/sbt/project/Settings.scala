import sbt._
import Keys._

object Settings {
    lazy val java_home: File = if ("FreeBSD" == sys.env.getOrElse("OSTYPE", "")) file("/usr/local/openjdk8") else file("/usr/lib/jvm/default")
	lazy val java_lib: File = if ("FreeBSD" == sys.env.getOrElse("OSTYPE", "")) file("/usr/local/share/java/classes") else file("/usr/share/java/lib") 
	lazy val scala_home: File = if ("FreeBSD" == sys.env.getOrElse("OSTYPE", "")) file("/usr/local/share/scala") else file("/usr/share/scala")
	lazy val scala_lib: File = scala_home / "lib"
    
	lazy val defaultSettings = Seq(
        autoScalaLibrary := false
        //, ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }
        //, offline in Global := true
        //, skip in update := true
        //, retrieveManaged := false
        //, javaHome := Some(java_home)
        //, scalaHome := Some(scala_home)
        , crossPaths := false
		
        /*, resolvers += Seq(
            Resolver.url("Local Ivy Repository",
                "file://" + Path.userHome.absolutePath + "/.ivy2/local"
                )(Patterns("[organization]/[module]/[revision]/[artifact]-[revision](-[classifier]).[ext]"))
            //, Resolver.mavenLocal
            //, "Local Maven Repository" at
            //    "file://" + Path.userHome.absolutePath + "/.m2/repository"
            )*/
		//, resolvers += Resolver.jcenterRepo
		
		, envVars := Map("LD_LIBRARY_PATH" -> "%s:%s".
			format(sys.env.getOrElse("LD_LIBRARY_PATH", ".:/usr/local/lib"),
            sys.props.getOrElse("java.library.path", "")))
		, fork in (Test, run) := true
		, cleanFiles ++= { baseDirectory (_ * "*.log" get) value }
		
		, javaOptions ++= Seq("-esa", "-ea","-Xmx1024m", "-Xms16m", "-Xss16m", 
			"-Djava.library.path=%s".format(
            sys.props.getOrElse("java.library.path", "")))
		, javacOptions ++= Seq("-Xlint:all", "-deprecation") ++ (if ("debug" == sys.env.getOrElse("CONFIG", "")) Seq("-g") else Seq())
		, javacOptions in (Compile, doc) ++= Seq("-use", "-private",
            "-version", "-author")
			
		, scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xlint", 
            "-feature") ++ (if ("debug" == sys.env.getOrElse("CONFIG", "")) Seq("-g:vars") else Seq("-optimise"))
		//, scalacOptions in (Compile, doc) ++= Seq("-author")
		
		, packageOptions in (Test, packageBin) += {
  			val classpath: Classpath = (dependencyClasspath in Test) value;
            val filePaths = classpath map { attrFile: Attributed[File] => 
                //attrFile.data.toPath().toString() // absolute paths
                //target.toPath().relativize(attrFile.data.toPath()).
                //  toString()                      // relative paths
                "lib/" + attrFile.data.toPath().getFileName().toString()
            };
            Package.ManifestAttributes(
                java.util.jar.Attributes.Name.CLASS_PATH -> 
                filePaths.reduceOption(_ + " " + _).getOrElse("")) }
		, packageOptions in (Compile, packageBin) += {
            val classpath: Classpath = (externalDependencyClasspath in Runtime) value;
			val filePaths = classpath map { attrFile: Attributed[File] => 
                "lib/" + attrFile.data.toPath().getFileName().toString() }; 
            Package.ManifestAttributes(
                java.util.jar.Attributes.Name.CLASS_PATH -> 
                filePaths.reduceOption(_ + " " + _).getOrElse("")) }
		, exportJars := true
	)
}
