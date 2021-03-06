(ns ^{:doc "Core tests"} org.sandbox.introclj.foreignc.core-test
    (:require
        [clojure.test :as test]
        [clojure.test.check :as qc]
        [clojure.test.check.generators :as gen]
        [clojure.test.check.properties :as prop]
        [clojure.test.check.clojure-test :as testqc]
        )
    (:require
        [org.sandbox.introclj.util.core :as util]
        )
    (:gen-class))

(comment
    --- compile test & run test ---
    java -cp `lein [with-profile test] classpath` clojure.main -m pkg.core-test
    lein test [pkg.core-test]
    
    --- run test w/out compile ---
    [sh|java -cp `lein [with-profile test] classpath` clojure.main] pkg/ts_main.sh
    java -cp `lein [with-profile test] classpath` clojure.main pkg/core_test.clj
)

(def paths [
    "classic_test" "classic_prop"
    ])
(doseq [filenm paths] (load filenm))

(defn -main 
    "Entry point
    @param args - command-line args"
    [& argv]
    ;(apply test/run-all-tests (if argv (map re-pattern argv)
    ;    [#"org.sandbox.introclj.foreignc\..*"]))
    (apply test/run-tests (map symbol (if argv argv [])))
    )

(when (and (.isFile (clojure.java.io/file *file*)) (re-find  #"core_test.clj" *file*))
    (apply -main *command-line-args*))
