#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
(import (java.nio.file Files))

(defproject ${groupId}/${artifactId} "${version}"
	:description "${description}"
	:url "http://${repohost}/${repoacct}/${parent}"
	:license {:name "${license}"
	          :url "http://opensource.org/licenses/${license}"}
	:min-lein-version "2.0.0"

    :require [
        [clojure.string :as str]
        [clojure.java.io :as io]
        ]
    :dependencies [;[leiningen/leiningen "2.9.5"]
        [org.clojure/clojure "1.10.3"]
        [org.clojure/tools.reader "1.3.6"]
        [org.clojure/tools.logging "1.2.4"]
        ;[org.clojure/tools.cli "1.0.206"]
        ;[org.clojure/data.json "2.4.0"]

        ;[org.slf4j/slf4j-api "1.7.33"]
        ;[org.slf4j/log4j-over-slf4j "1.7.33"]
        ;[ch.qos.logback/logback-classic "1.2.10"]

        ;[net.java.dev.jna/jna "5.10.0"]
        ;[org.ini4j/ini4j "0.5.4"]
#{if}("yes" == ${executable})

        ;[${groupId}/${parent}.util "0.1.0"]

        [javax.json/javax.json-api "1.1.4"]
        [org.glassfish/javax.json "1.1.4"]
        [org.yaml/snakeyaml "1.30"]
        [com.moandjiezana.toml/toml4j "0.7.2"]
#{end}
        ]
    :profiles {
        :test {:dependencies [[org.clojure/test.check "1.1.1"]]}
    }
    :plugins [
		[lein-codox "0.10.7"] [lein-ancient "0.7.0"] [jonase/eastwood "0.4.3"]
        [autodoc/lein-autodoc "1.1.1"] [lein-cloverage "1.2.2"]
		]
    ;:offline? true
#{if}("yes" == ${executable})
    :main ${package}.Main
#{end}
    :aot [${package}.core]
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
    ;:jar-name "${artifactId}-%s.jar"
    ;:jar-exclusions [${symbol_pound}"(?:^|/).svn/"]
    :omit-source false
    :auto-clean false
    :manifest {
        "Built-By" "${author}"
        ;"Class-Path" ~${symbol_pound}(clojure.string/join ${symbol_escape}space
        ;    (leiningen.core.classpath/get-classpath %))
        "Class-Path" ~${symbol_pound}(clojure.string/join ${symbol_escape}space
            (let [jars (filter (fn [e] (re-find ${symbol_pound}"${symbol_escape}.jar" e))
                    (leiningen.core.classpath/get-classpath %))
                    libdir (io/file "target" "lib")]
                (when-not (.exists libdir) (.mkdir libdir) (doseq [jar jars]
                    (println "... copy depn(s)" (.getName (io/file jar)))
                    ;(io/copy jar (io/file libdir (.getName (io/file jar))))
                    (Files/createSymbolicLink (.toPath (io/file libdir
                        (.getName (io/file jar)))) (.toPath (io/file jar))
                        (into-array java.nio.file.attribute.FileAttribute []))
                    ))
                (map (fn [e] (io/file "lib" (.getName (io/file e)))) jars)))
    }

    ;:parent [${groupId}/${parent}-parent "0"]
    :pom-plugins [
		[com.theoryinpractise/clojure-maven-plugin "1.8.4"
            {:configuration (
                [:sourceDirectories [:sourceDirectory "src/main/clj"]]
                [:testSourceDirectories [:testSourceDirectory "src/test/clj"]]
                )
             :extensions "true"
#{if}("yes" == ${executable})
             :mainClass ${package}.Main
#{end}
            }]
        [com.codehaus.mojo/build-helper-maven-plugin "3.3.0"
            {:configuration (
                [:sourceDirectories [:sourceDirectory "src/main/java"]]
                )
            :executions (
                [:execution [:id "add-source"] [:goals ([:goal "add-source"])]
                [:phase "generate-sources"]])
            }]
		]
    ;:pom-addition [:packaging "clojure"]
    )

;; Note: environment variables to adjust Java cmds/options
;; JAVA_CMD -- example: JAVA_CMD="/usr/share/java/bin/java -cp .:lib"
;; JVM_OPTS -- example: JVM_OPTS="-Djava.library.path=${symbol_dollar}HOME/javalib"
;; LEIN_JVM_OPTS -- example: LEIN_JVM_OPTS="-DtestFrwk=clojure.test -Dexecutable=yes -DffiLib=jna"
