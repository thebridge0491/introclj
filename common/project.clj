(import (java.nio.file Files))

(defproject org.sandbox/introclj.util "0.1.0"
	:description "Utilites sub-package for Clojure Intro examples project."
	:url "http://bitbucket.org/thebridge0491/introclj"
	:license {:name "Apache-2.0"
    :url "http://opensource.org/licenses/Apache-2.0"}
	:min-lein-version "2.0.0"
	
  :require [
    [clojure.string :as str]
    [clojure.java.io :as io]
    ]
  :dependencies [;[leiningen/leiningen "2.3.4"]
    [org.clojure/clojure "1.5.1"]
    [org.clojure/tools.reader "0.8.3"]
    [org.clojure/tools.logging "0.2.6"]
        
    [org.slf4j/slf4j-api "1.7.6"]
    ;[net.java.dev.jna/jna "4.0.0"]
      
    ;[org.slf4j/log4j-over-slf4j "1.7.6"]
    ;[ch.qos.logback/logback-classic "1.1.1"]
    ]
    :profiles {
      :test {:dependencies [[org.clojure/test.check "0.5.7"]]}
    }
    :plugins [
      [codox "0.6.7"] [lein-ancient "0.5.4"] [jonase/eastwood "0.1.0"]
      [lein-autodoc "0.9.0"] [lein-cloverage "1.0.2"]
		]
    ;:offline? true
    
    ;:main org.sandbox.introclj.util.Main
    
    :aot [org.sandbox.introclj.util.core]
    :javac-options ["-Xlint:all" "-deprecation" "-g"]
    :jvm-opts [~(str "-Djna.library.path=" (System/getenv "HOME") "/.local/lib") "-ea" "-Xmx1024m" "-Xms16m" "-Xss16m"]
    ;:library-path ~(str (System/getenv "HOME") "/.m2/repository") ; "lib/" ; "/usr/share/java"
    
    :source-paths ["src/main/clj"]
    :java-source-paths ["src/main/java"]
    :test-paths ["src/test/clj"]
    :resource-paths ["src/main/resources"]
    :dev-resource-path "src/main/resources"
    ;:compile-path "target/%s/classes"
    ;:target-path "target/%s/"
    :clean-targets [:target-path :compile-path]
    ;:jar-name "introclj.util-%s.jar"
    ;:jar-exclusions [#"(?:^|/).svn/"]
    :jar-inclusions [#"\.editorconfig" #"\.hgignore" #"\.gitignore"]
    :omit-source false
    :auto-clean false
    :manifest {
      "Built-By" "thebridge0491"
      ;"Class-Path" ~#(clojure.string/join \space
      ; (leiningen.core.classpath/get-classpath %))
      "Class-Path" ~#(clojure.string/join \space
        (let [jars (filter (fn [e] (re-find #"\.jar" e))
            (leiningen.core.classpath/get-classpath %)) libdir
            (io/file "target" "lib")]
          (when-not (.exists libdir) (.mkdir libdir) (doseq [jar jars]
            (println "... copy depn(s)" (.getName (io/file jar)))
            ;(io/copy jar (io/file libdir (.getName (io/file jar))))
            (Files/createSymbolicLink (.toPath (io/file libdir 
              (.getName (io/file jar)))) (.toPath (io/file jar))
              (into-array java.nio.file.attribute.FileAttribute []))))
          (map (fn [e] (io/file "lib" (.getName (io/file e)))) jars)))
    }
    
    ;:parent [org.sandbox/introclj-parent "0"]
    :pom-plugins [
      [com.theoryinpractise/clojure-maven-plugin "1.3.19"
        {:configuration (
            [:sourceDirectories [:sourceDirectory "src/main/clj"]]
            [:testSourceDirectories [:testSourceDirectory "src/test/clj"]]
          )
          :extensions "true"
          ;:mainClass "org.sandbox.introclj.util.Main"
          }]
		]
    ;:pom-addition [:packaging "clojure"]
    )

;; Note: environment variables to adjust Java cmds/options
;; JAVA_CMD -- example: JAVA_CMD="/usr/share/java/bin/java -cp .:lib"
;; JVM_OPTS -- example: JVM_OPTS="-Djava.library.path=$HOME/javalib"
;; LEIN_JVM_OPTS -- example: LEIN_JVM_OPTS="-DtestFrwk=clojure.test -Dexecutable=yes -DffiLib=jna"
