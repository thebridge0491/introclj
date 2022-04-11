#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
(ns ^{:doc "App main"} ${package}.Main
    ;{:author "${author}"}
    (:require
        [clojure.tools.reader :as reader]
        [clojure.tools.reader.reader-types :as reader-types]
        [clojure.string :as str]
        [clojure.java.io :as io]
        [clojure.tools.logging :as log]
        [clojure.tools.cli :as cli]
        [clojure.data.json]
        )
    (:require
        [${package}.Person]
        [${package}.core :as core]
        )
    (:import
        [org.slf4j LoggerFactory]
        ;[org.apache.log4j Level Logger]
        [ch.qos.logback.classic Level Logger]
        [${package} Person User]
        )
    (:gen-class))

;(use '[${package}.core :as core])

; single-line comment
(comment multi-line comment
    --- package manager - init/configure package ---
    lein new [template] projName  ; template: default, app, ?

    --- generate API docs ---
    lein doc

    --- compile & run ---
    java -cp `lein classpath` clojure.main -m pkg.main arg1 argN
    lein jar ; lein run arg1 argN

    --- run w/out compile ---
    [sh|java -cp `lein classpath` clojure.main] pkg/main.sh arg1 argN
    java -cp `lein classpath` clojure.main pkg/main.clj arg1 argN

    --- run REPL, load script, & run ---
    -- java -cp `lein classpath` clojure.main --init pkg/main.clj --repl ; (pkg/-main "arg1" "argN")
    lein repl ; (pkg/-main "arg1" "argN")

    --- help/info tools in REPL ---
    (apropos 'PATTERN) ; (doc OBJ) ; (javadoc CLASS) ; (find-doc STRING)
    (dir LIBRARY) ; (all-ns)
)

; from cmdln: java -Dlog4j.configurationFile=path/log4j2.xml ...
;(System/setProperty "log4j.configurationFile" "log4j2.xml")
; from cmdln: java -Dlogback.configurationFile=path/logback.xml ...
;(System/setProperty "logback.configurationFile" "logback.xml")

(def ^{:private true} rootLogger (LoggerFactory/getLogger "root"))
(def prog-name (ns-name '${package}.Main))

(defn- deserialize-str
	[data-str fmt]
	(let [blank-cfg (atom {"fmt" fmt}) yaml (org.yaml.snakeyaml.Yaml.)
			toml (com.moandjiezana.toml.Toml.)]
		(cond
			(or (= "yaml" fmt) (= "json" fmt)) (let [
					yamlmap (.load yaml data-str)]
				(merge @blank-cfg yamlmap))
			(= "toml" fmt) (let [tomlmap (.toMap (.read toml data-str))]
				(merge @blank-cfg tomlmap))
			;(= "json" fmt) (let [jsonmap (clojure.data.json/read-str data-str)]
			;	(merge @blank-cfg jsonmap)
			;	)
			)
		)
	)

(defn- run-${name}
    [progname name]
    (let [re (re-pattern "(?i)quit") m (re-matcher re name)]
        (printf "%s match: %s to %s${symbol_escape}n" (if (.matches m) "Good" "Does not")
            name (.pattern re))
        ))

(defn- usage
    [progname]
    (format "Usage: %s [-h][-v VERBOSITY][-u USER]%n" progname))

(defn- parse-cmdopts
    [argv]

    ;(log/info "parse-cmdopts")
    (.info rootLogger "parse-cmdopts")

    (def option-spec [
        ["-h" "--help"]
        ["-v" "--verbose VERBOSITY" "Verbosity" :default "3"]
        ["-u" "--user USER" "User name" :default "World"]
        ])
    (let [{:keys [options arguments errors summary]}
            (cli/parse-opts argv option-spec)]

        (cond
            (options :help) (do (printf (usage prog-name))
				(doseq [el option-spec] (printf "${symbol_escape}t%s%n" el))
				(flush) (System/exit 0))
			)

        [(Integer/parseInt (options :verbose)) (options :user)]
        ))

(defn -main
    "Entry point
    @param args - command-line args"
    [& argv]
	(def rsrc-path (or (System/getenv "RSRC_PATH")
		(System/getProperty "rsrcPath" "src/main/resources")))
    (let [ini-cfg (org.ini4j.Ini.) iniStrm (atom nil) jsonStrm (atom nil)
            tomlStrm (atom nil) yamlStrm (atom nil)
			[verbose name num is-expt2] (parse-cmdopts argv)]

        (try
			(reset! iniStrm (io/input-stream (str rsrc-path "/prac.conf")))
		(catch java.io.IOException exc0
			(printf "(exc: %s) Bad env var RSRC_PATH: %s\n" exc0 rsrc-path)
            (flush)

			(reset! iniStrm (io/input-stream (io/resource "prac.conf")))
			))
		(try
			(.load ini-cfg @iniStrm)
		(catch java.io.IOException exc
			;(printf "%s%n" (.getMessage exc))
			(println (.printStackTrace exc))
			(System/exit 1)
			))


        (try
			(reset! jsonStrm (io/input-stream (str rsrc-path "/prac.json")))
			(reset! tomlStrm (io/input-stream (str rsrc-path "/prac.toml")))
			(reset! yamlStrm (io/input-stream (str rsrc-path "/prac.yaml")))
		(catch java.io.IOException exc0
			(printf "(exc: %s) Bad env var RSRC_PATH: %s\n" exc0 rsrc-path)
            (flush)

			(reset! jsonStrm (io/input-stream (io/resource "prac.json")))
			(reset! tomlStrm (io/input-stream (io/resource "prac.toml")))
			(reset! yamlStrm (io/input-stream (io/resource "prac.yaml")))
			))

        (let [json-cfg (deserialize-str (String. (.readAllBytes @jsonStrm)) "json")
                toml-cfg (deserialize-str (String. (.readAllBytes @tomlStrm)) "toml")
                yaml-cfg (deserialize-str (String. (.readAllBytes @yamlStrm)) "yaml")
                tup-arr (vector
				(vector ini-cfg (get (get ini-cfg "default") "domain")
					(get (get ini-cfg "user1") "name"))
				(vector json-cfg (get json-cfg "domain")
					(get (get json-cfg "user1") "name"))
				(vector toml-cfg (get toml-cfg "domain")
					(get (get toml-cfg "user1") "name"))
				(vector yaml-cfg (get yaml-cfg "domain")
					(get (get yaml-cfg "user1") "name"))
                    )]
			(doseq [row tup-arr]
				(printf "config: %s\n" (get row 0))
				(printf "domain: %s\n" (get row 1))
				(printf "user1Name: %s\n\n" (get row 2))
				))

        (run-${name} prog-name name)

        ;(log/debug "exiting main()")
		(.debug rootLogger "exiting main()")
        (flush)
	))

(when (and (.isFile (clojure.java.io/file *file*)) (re-find  ${symbol_pound}"Main.clj" *file*))
    (apply -main *command-line-args*))
