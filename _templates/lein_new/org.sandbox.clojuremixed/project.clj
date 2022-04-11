(def sys-props (System/getProperties))
(defn save-template
  [& {:keys [to-dir] :or {to-dir "../_templates"}}]
  (let [save-dir (format "%s/Templates/lein_new/org.sandbox.clojuremixed" to-dir)]
    (when (.exists (clojure.java.io/as-file to-dir))
      (->> (apply clojure.java.shell/sh ["mkdir" "-vp" save-dir])
        :out println)
      (->> (apply clojure.java.shell/sh ["echo" "Saving to dir: " to-dir]) :out println)
      (->> (apply clojure.java.shell/sh ["sh" "-c" 
        (format "tar --exclude-from=exclude.lst -cf - . | tar -vxpf - -C %s" 
        save-dir)]) :out println))))

(defproject org.sandbox.clojuremixed/lein-template (get sys-props "templateVersion" "0.1.0")
  :description ~(get sys-props "description" "A Leiningen template for mixed Clojure project (choices skeleton: skeleton_clj).")
  :url ~(format "http://%s/%s/clojuremixed" (get sys-props "repoHost" "bitbucket.org")
      (get sys-props "repoAcct" "imcomputer"))
	:license {:name ~(get sys-props "license" "Apache-2.0")
    :url ~(str "http://opensource.org/licenses/" (get sys-props "license" "Apache-2.0"))}
	:min-lein-version "2.0.0"
  :dependencies [;[leiningen/leiningen "2.9.5"]
    [org.clojure/data.json "2.4.0"]
    [org.yaml/snakeyaml "1.30"]
    [com.moandjiezana.toml/toml4j "0.7.2"]
  ]
  :eval-in-leiningen true
  :jar-inclusions [#"\.editorconfig" #"\.hgignore" #"\.gitignore"]
  :manifest {
    "Build-Jdk" ""
    "Created-By" ""
    "Built-By" ~(do (save-template) (get sys-props "repoAcct" "imcomputer"))
  }
  )

;; Note: environment variables to adjust cmds/options for Java or lein
;; JAVA_CMD -- example: JAVA_CMD="/usr/share/java/bin/java -cp .:lib"
;; JVM_OPTS -- example: JVM_OPTS="-Djava.library.path=$HOME/javalib"
;; LEIN_JVM_OPTS -- example: LEIN_JVM_OPTS="-Dskeleton=skeleton_clj -Dexecutable=yes"
