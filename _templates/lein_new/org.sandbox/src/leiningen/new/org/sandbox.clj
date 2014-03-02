(ns leiningen.new.org.sandbox
  (:require
    [leiningen.new.templates :refer [renderer project-name name-to-path
        multi-segment sanitize-ns ->files *dir*]]
      [leiningen.core.main :as main]
      [clojure.java.shell :as shell]
      [clojure.string :as str]
      [clojure.java.io :as io]
      [clojure.data.json]
      )
  (:import 
    [java.util.jar JarEntry JarFile]
    [java.lang Thread]
    )
  )

(defn config-defaults
  [datajson skel-dir]
  (let [jsonstrm (atom nil) jsonobj (atom nil)]
    (try
      (reset! jsonstrm (io/input-stream (format 
        "resources/leiningen/new/%s/%s" skel-dir datajson)))
    (catch java.io.IOException exc
      (print (format "(exc: %s) Bad file path: resources/leiningen/new/%s/%s\n\n"
        (.getMessage exc) skel-dir datajson))
      (flush)
      (reset! jsonstrm (io/input-stream (io/resource (format 
        "leiningen/new/%s/%s" skel-dir datajson))))))
    (try
      (reset! jsonobj (clojure.data.json/read (io/reader @jsonstrm)
        :key-fn clojure.core/keyword))
    (catch java.io.IOException exc
      (println (.printStackTrace exc))
      (System/exit 1)))
    @jsonobj))

(defn org.sandbox
  "A Leiningen template for mixed JVM project (choices skeleton: clojuremixed, groovymixed, javamixed, scalamixed).
  
usage: [LEIN_JVM_OPTS='-Dskeleton=clojuremixed'] lein new org.sandbox [groupId/]artifactId
LEIN_JVM_OPTS='-DffiLib=jna' lein new org.sandbox org.sandbox/introclj.util"
  [raw-name]
  (let [skel-dir (format "org/sandbox/%s" (System/getProperty "skeleton" "clojuremixed"))
    start-dir (format "%s/{{name}}" skel-dir) render (renderer start-dir)
    main-ns (multi-segment (sanitize-ns raw-name))
    initdata (merge (config-defaults "data.json" skel-dir)
      {:raw-name raw-name :name (project-name raw-name)
      :namespace main-ns :nesteddirs (name-to-path main-ns)
      :project (last (str/split (project-name raw-name) #"\."))
      :parent (str/join "." (butlast (str/split (project-name raw-name) #"\.")))
      :date (System/getProperty "date" (.format 
        (java.text.SimpleDateFormat. "yyyy-MM-dd") (java.util.Date.)))})
    data (merge initdata {:groupid (System/getProperty "groupId" 
        (get initdata :groupid))
      :parentcap (str/capitalize (get initdata :parent))
      :projectcap (str/capitalize (get initdata :project))
      :repohost (System/getProperty "repoHost" (get initdata :repohost))
      :repoacct (System/getProperty "repoAcct" (get initdata :repoacct))
      :year (first (str/split (System/getProperty "date" 
        (get initdata :date)) #"-"))
      :readmeext (System/getProperty "readmeExt" (get initdata :readmeext))
      :license (System/getProperty "license" (get initdata :license))
      :buildtool (System/getProperty "buildTool" (get initdata :buildtool))
      :testfrwk (System/getProperty "testFrwk" (get initdata :testfrwk))
      :ffilib (System/getProperty "ffiLib" (get initdata :ffilib))}
      
      (if (System/getProperty "author")
        {:author (System/getProperty "author")} {})
      (if (System/getProperty "email") 
        {:email (System/getProperty "email")} {})
      (if (System/getProperty "executable") 
        {:executable (System/getProperty "executable")} {})
      )
    files-skel (if (io/resource (format 
        "resources/leiningen/new/%s/data.json" skel-dir))
      (map (fn [p] (str/replace p (format "resources/leiningen/new/%s/" 
        start-dir) "")) (filter (fn [f] (.isFile f)) (file-seq (io/file
        (format "resources/leiningen/new/%s" start-dir)))))
      (let [url (first (filter (fn [f] (re-matches #".*\.jar$" 
          (.getPath f))) (seq (.getURLs (-> (Thread/currentThread) 
          (.getContextClassLoader)))))) jFile (JarFile. (.getPath url))]
        (map (fn [p] (str/replace (.toString p) (format 
          "leiningen/new/%s/" start-dir) "")) (filter (fn [e] (and 
          (re-matches (re-pattern (format "leiningen/new/%s/.*" skel-dir)) 
          (.toString e)) (not (re-matches #".*data\.json" (.toString e))))) (filter (fn [e] (not (.isDirectory e))) 
          (enumeration-seq (.entries jFile)))))
          )
      )
    ]
    
    (main/info "Generating fresh 'lein new' org.sandbox project.")
    (main/info (format "... %d files processing ..." (.size files-skel)))
    (apply (partial ->files data) (map (fn [f] 
        [(str/replace f #"\.mustache$" "") (render f data)]) files-skel))
    
    ;(println (:out (apply shell/sh ["echo" "Post generation message"])))
    (->> (apply shell/sh ["echo" "Post generation message"]) :out println)
    (->> (apply shell/sh (str/split (format 
      "groovy -DtemplateGroupId=%s -DtemplateVersion=%s -DtoDir=%s %s/choices/postgen.groovy" 
      (System/getProperty "templateGroupId" "org.sandbox")
      (System/getProperty "templateVersion" "0.1.0") 
      (System/getProperty "toDir" (or *dir* (get data :name)))
      (or *dir* (get data :name))) #" ")) :out println)
    ))
