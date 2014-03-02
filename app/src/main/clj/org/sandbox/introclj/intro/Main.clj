(ns ^{:doc "App main"} org.sandbox.introclj.intro.Main
    (:require
        ;[clojure.tools.reader :as reader]
        [clojure.string :as str]
        [clojure.java.io :as io]
        [clojure.tools.logging :as log]
        [clojure.tools.cli :as cli]
        [clojure.data.json]
        )
    (:require
        [org.sandbox.introclj.intro.Person]
        [org.sandbox.introclj.intro.core :as core]
        )
    (:import 
        [org.slf4j LoggerFactory]
        ;[org.apache.log4j Level Logger]
        [ch.qos.logback.classic Level Logger]
        [org.sandbox.introclj.intro Person User]
        )
    (:gen-class))

;(use '[org.sandbox.introclj.intro.core :as core])

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
(def prog-name (ns-name 'org.sandbox.introclj.intro.Main))

(defn- run-intro
    [progname name]
    (let [re (re-pattern "(?i)quit") m (re-matcher re name)]
        (printf "%s match: %s to %s\n" (if (.matches m) "Good" "Does not")
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
				(doseq [el option-spec] (printf "\t%s%n" el))
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
    (let [iniStrm (atom nil) ini-cfg (org.ini4j.Ini.)
			jsonStrm (atom nil) yamlStrm (atom nil) jsonobj (atom nil)
			rdr (atom nil) yaml (org.yaml.snakeyaml.Yaml.)
			[verbose name] (parse-cmdopts argv)]
        
        (try
			(reset! iniStrm (io/input-stream (str rsrc-path "/prac.conf")))
			(reset! jsonStrm (io/input-stream (str rsrc-path "/prac.json")))
			(reset! yamlStrm (io/input-stream (str rsrc-path "/prac.yaml")))
		(catch java.io.IOException exc0
			(printf "(exc: %s) Bad env var RSRC_PATH: %s\n" exc0 rsrc-path)
            (flush)
			
			(reset! iniStrm (io/input-stream (io/resource "prac.conf")))
			(reset! jsonStrm (io/input-stream (io/resource "prac.json")))
			(reset! yamlStrm (io/input-stream (io/resource "prac.yaml")))
			))
		
		(try
			(.load ini-cfg @iniStrm)
			;(reset! jsonobj (clojure.data.json/read (io/reader @jsonStrm)))
			(reset! rdr (javax.json.Json/createReader @jsonStrm))
			(reset! jsonobj (.readObject @rdr))
		(catch java.io.IOException exc
			;(printf "%s%n" (.getMessage exc))
			(println (.printStackTrace exc))
			(System/exit 1)
			)
		(finally
			(if @rdr (.close @rdr))
			))
        
        (let [yamlmap (.load yaml @yamlStrm)
				tup-arr (vector
				(vector ini-cfg (get (get ini-cfg "default") "domain")
					(get (get ini-cfg "user1") "name"))
				(vector @jsonobj (get @jsonobj "domain")
					(get (get @jsonobj "user1") "name"))
				(vector yamlmap (get yamlmap "domain")
					(get (get yamlmap "user1") "name"))
				)]
			(doseq [row tup-arr]
				(printf "config: %s\n" (get row 0))
				(printf "domain: %s\n" (get row 1))
				(printf "user1Name: %s\n\n" (get row 2))
				))
        
        (run-intro prog-name name)
        
        ;(log/debug "exiting main()")
		(.debug rootLogger "exiting main()")
        (flush)
	))

(when (and (.isFile (clojure.java.io/file *file*)) (re-find  #"Main.clj" *file*))
    (apply -main *command-line-args*))
