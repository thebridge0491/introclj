#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
(ns ^{:doc "Classic library"} ${package}.classic
    ;{:author "${author}"}
    ;(:require
    ;   ;[clojure.tools.logging :as log]
    ;   )
    (:import [${package} Classic_c])
    )

; env LD_LIBRARY_PATH=.:/usr/local/lib
; or
; -Djava.library.path=".:/usr/local/lib"
(System/loadLibrary "${artifactId}_stubs")

(defn fact-i
	[n]
    (Classic_c/fact_i n))

(defn fact-lp
	[n]
    (Classic_c/fact_lp n))

(defn expt-i
	[b n]
    (Classic_c/expt_i b n))

(defn expt-lp
	[b n]
    (Classic_c/expt_lp b n))


(defn -main
    [& argv]
    (printf "(fact %d): %d${symbol_escape}n" 5 (fact-i 5))
    (flush))

(when (and (.isFile (clojure.java.io/file *file*)) (re-find  ${symbol_pound}"classic.clj" *file*))
    (apply -main *command-line-args*))
