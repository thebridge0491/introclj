(ns ^{:doc "Classic library"} org.sandbox.introclj.foreignc.classic
    ;(:require
    ;   ;[clojure.tools.logging :as log]
    ;   )
    (:require
        [org.sandbox.introclj.foreignc.IClassic-c :as IClassic-c]
        )
    (:import 
       [com.sun.jna Library Native]
       )
    )

; env LD_LIBRARY_PATH=.:/usr/local/lib
; or
; -D[java | jna].library.path=".:/usr/local/lib"
; inside class file
;(doseq [pathX (clojure.string/split ".:/usr/local/lib" #":")]
;    (com.sun.jna.NativeLibrary/addSearchPath "intro_c-practice" pathX))
(System/setProperty "jna.library.path" (System/getProperty "jna.library.path"
    (System/getProperty "java.library.path" ".:/usr/local/lib")))

(defn fact-i
	[n]
	(.fact_i IClassic-c/CLib n))

(defn fact-lp
	[n]
	(.fact_lp IClassic-c/CLib n))

(defn expt-i
	[b n]
	(.expt_i IClassic-c/CLib b n))

(defn expt-lp
	[b n]
	(.expt_lp IClassic-c/CLib b n))


(defn -main
    [& argv]
    (printf "(fact %d): %d\n" 5 (fact-i 5))
    (flush))

(when (and (.isFile (clojure.java.io/file *file*)) (re-find  #"classic.clj" *file*))
    (apply -main *command-line-args*))
