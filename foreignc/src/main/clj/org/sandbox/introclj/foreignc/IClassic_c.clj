(ns ^{:doc "Interface to native library"} org.sandbox.introclj.foreignc.IClassic-c
    ;(:require
    ;   ;[clojure.tools.logging :as log]
    ;   )
    (:import 
       [com.sun.jna Library Native]
       )
    ;(:gen-class)
    )

(def IClassic-c
    (gen-interface
        :name org.sandbox.introclj.foreignc.IClassic-c
        :extends [com.sun.jna.Library]
        :methods [
            [fact_i [long] long], [fact_lp [long] long],
            [expt_i [float float] float], [expt_lp [float float] float]
            ]
        ))

; env LD_LIBRARY_PATH=.:/usr/local/lib
; or
(comment ; -D[java | jna].library.path=".:/usr/local/lib"
; inside class file
;(doseq [pathX (clojure.string/split ".:/usr/local/lib" #":")]
;    (com.sun.jna.NativeLibrary/addSearchPath "intro_c-practice" pathX))
(System/setProperty "jna.library.path" (System/getProperty "jna.library.path"
    (System/getProperty "java.library.path" ".:/usr/local/lib")))
)

(def CLib (com.sun.jna.Native/loadLibrary "intro_c-practice"
    org.sandbox.introclj.foreignc.IClassic-c))
