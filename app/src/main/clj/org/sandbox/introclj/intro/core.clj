(ns ^{:doc "Core library"} org.sandbox.introclj.intro.core
    (:require
        [clojure.tools.reader :as reader]
        [clojure.tools.reader.reader-types :as reader-types]
        [clojure.string :as str]
        [clojure.java.io :as io]
        [clojure.tools.logging :as log]
       )
    (:import 
        [org.slf4j LoggerFactory]
        )
    )

(def ^{:private true} rootLogger (LoggerFactory/getLogger "root"))

(defn delay-char
    [delay-msecs]
    (printf "Type any character when ready.")
    (flush)
    (if (not (or (= \newline (reader-types/peek-char *in*))
            (= \0 (reader-types/peek-char *in*))))
        (nth (read-line) 0)
        (do
            (read-line)
            (Thread/sleep delay-msecs)
            (delay-char delay-msecs)))
    )

(defn greeting
    [rsrc-path greet-file name]
    (.info rootLogger "greeting")
    (let [rdr (atom nil)]
        (try
			(reset! rdr (io/reader (str rsrc-path "/" greet-file)))
		(catch java.io.IOException exc0
			(printf "(exc: %s) Bad env var RSRC_PATH: %s\n" exc0 rsrc-path)
            (flush)
			
			(reset! rdr (io/reader (io/resource greet-file)))
			))
        (format "%s%s" (.readLine @rdr) name))
    )


(defn -main
    [& argv]
    (let [msecs (int 2.5e3)]
        (printf "(delay-char %d)\n" msecs)
        (delay-char msecs)
        (flush)))

(when (and (.isFile (clojure.java.io/file *file*)) (re-find  #"core.clj" *file*))
    (apply -main *command-line-args*))
