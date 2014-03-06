(in-ns 'org.sandbox.introclj.intro.core-test)
(require '[clojure.set :as set])

(defn class-fixture1
    "Class setUp/tearDown"
    [func]
    (println "setUpClass ...")
    (func)
    (println "... tearDownClass"))

(defn fixture1
    "Per test setUp/tearDown"
    [func]
    (println "setUp ...")
    (func)
    (println "... tearDown"))

(def lst (range 0 5))
(def revlst (range 4 -1 -1))

(test/use-fixtures :once class-fixture1)
(test/use-fixtures :each fixture1)

(test/deftest test-lists
    "test-lists"
    (let [xs '(2 1 0 4 3)]
        (test/is (= (list 1) '(1)) "make-list")
        (test/is (= xs '(2 1 0 4 3)) "equal")
        (test/is (not= xs '(2 1 0)) "inequal")
        (test/is (empty? '()) "empty")
        (test/is (= 2 (first xs)) "head")
        (test/is (= 5 (count xs)) "length")
        (test/is (= (concat xs '(9 9 9 9)) '(2 1 0 4 3 9 9 9 9)) "append")
        (test/is (= (reverse xs) '(3 4 0 1 2)) "reverse")
        (test/is (some (fn [e] (= 1 (mod e 2))) xs) "exists")
        (test/is (= '(2 0 4) (filter even? xs)) "filter")
        (test/is (= '(1 3) (remove (fn [e] (= 0 (mod e 2))) xs)) "remove")
        (test/is (= -10 (reduce - 0 xs)) "fold-left")
        (test/is (= '(4 3 2 6 5) (map (fn [e] (+ e 2)) xs)) "map")
        (test/is (= [2 1 0 4 3] (apply vector xs)) "to-vector")
        (test/is (java.util.Arrays/equals (long-array [2 1 0 4 3]) (into-array Long/TYPE xs)) "to-array")
        (test/is (and (= lst (sort < xs)) (= revlst (sort > xs))) "sort")
        ))

(test/deftest test-vectors
    "test-vectors"
    (let [arr [2 1 0 4 3]]
        (test/is (= (vector 1) [1]) "make-vector")
        (test/is (= arr [2 1 0 4 3]) "equal")
        (test/is (= 2 (nth arr 0)) "nth")
        (test/is (= 5 (count arr)) "length")
        (test/is (= (concat arr [9 9 9 9]) [2 1 0 4 3 9 9 9 9]) "append")
        (test/is (= -10 (reduce - 0 arr)) "fold-left")
        (test/is (= [4 3 2 6 5] (map (fn [e] (+ e 2)) arr)) "map")
        (test/is (= '(2 1 0 4 3) (apply list arr)) "to-list")
        (test/is (java.util.Arrays/equals (long-array [2 1 0 4 3]) (into-array Long/TYPE arr)) "to-array")
        (test/is (and (= (apply vector lst) (sort < arr)) (= (apply vector revlst) (sort > arr))) "sort")
        ))

(defn array2d-equals
    [arr0 arr1]
    (let [acc (atom true) hgt0 (alength arr0) wid0 (count (aget arr0 0))]
        (reset! acc (and @acc (= hgt0 (alength arr1)) (= wid0 (count (aget arr1 0)))))
        (doseq [h (range hgt0) w (range wid0)]
            (reset! acc (and @acc (= (aget arr0 h w) (aget arr1 h w))))
            )
        @acc))

(test/deftest test-2darrays
    "test-2darrays"
    (let [arr2d (to-array-2d [[0 1 2] [3 4 5]])]
        ;(test/is (java.util.Arrays/equals (to-array-2d [[0 0] [0 0]]) (make-array Integer/TYPE 2 2)) "make-array")
        (test/is (array2d-equals (to-array-2d [[0 0] [0 0]]) (make-array Integer/TYPE 2 2)) "make-array")
        (test/is (array2d-equals (to-array-2d [[0 1 2] [3 4 5]]) arr2d) "equals")
        (test/is (= 3 (aget arr2d 1 0)) "aref")
        (test/is (= 2 (alength arr2d)) "length (rows | hgt | dimy)")
        (test/is (= 3 (count (aget arr2d 0))) "(cols | wid | dimx)")
        (aset arr2d 1 2 6)
        (test/is (= 6 (aget arr2d 1 2)) "aset")
        (test/is (= 16 (areduce arr2d idx acc 0 (apply + acc (aget arr2d idx)))) "fold-left")
        (test/is (= [[0 1 2] [3 4 6]] (map (fn [row] (apply vector row)) (into [] arr2d))) "to-vector")
        (test/is (= '((0 1 2) (3 4 6)) (map (fn [row] (apply list row)) (into [] arr2d))) "to-list")
        (comment
        (test/is (= 5 (count arr)) "array-dimensions")
        (test/is (= 5 (count arr)) "length")
        
        (test/is (= '(2 1 0 4 3) (apply list arr)) "to-list")
        (test/is (and (= (apply vector lst) (sort < arr)) (= (apply vector revlst) (sort > arr))) "sort")
        )
        ))

(test/deftest test-sets
    "test-sets"
    (let [xs '(\k \p \a \e \u \k \a) set0 (set xs)]
        (test/is (coll? set0) "coll?")
        (test/is (set? set0) "set?")
        (test/is (= #{\a \e \k \p \u} set0) "equals")
        (test/is (contains? set0 \u) "contains")
        (test/is (= 5 (count set0)) "length")
        (test/is (= #{\a \e \k \p \q \u \z} (set/union set0 #{\q \p \z \u})) "union")
        (test/is (= #{\p \u} (set/intersection set0 #{\q \p \z \u})) "intersection")
        (test/is (= #{\a \e \k} (set/difference set0 #{\q \p \z \u})) "difference")
        (test/is (= #{\a \e \k \q \z} (apply disj (set/union set0 #{\q \p \z \u}) (set/intersection set0 #{\q \p \z \u}))) "xor (union - intersection)")
        ))

(test/deftest test-maps
    "test-maps"
    (let [xs '(\k \p \a \e \u \k \a) map0 (atom {})
            ltrs (map (fn [el] (format "'ltr %d'" (mod el 5))) (range (count xs)))]
        (reset! map0 (apply hash-map (flatten (map list ltrs xs))))
        ;(reset! map0 (apply assoc {} (interleave ltrs xs)))
        ;(reset! map0 (zipmap ltrs xs))
        (test/is (coll? @map0) "coll?")
        (test/is (map? @map0) "map?")
        (test/is (= 5 (count @map0)) "length")
        (test/is (contains? @map0 "'ltr 2'") "contains")
        (swap! map0 (fn [tbl] (assoc tbl "'ltr 3'" \E)))
        (test/is (= \E (get @map0 "'ltr 3'")) "find")
        (swap! map0 (fn [tbl] (dissoc tbl "'ltr 0'")))
        (test/is (= '("'ltr 1'" "'ltr 2'" "'ltr 3'" "'ltr 4'") (sort compare (keys @map0))) "keys")
        (test/is (= '(("'ltr 1'" \a) ("'ltr 2'" \a) ("'ltr 3'" \E) ("'ltr 4'" \u)) (reduce (fn [acc tup] (cons tup acc)) '() @map0)) "to-alist")
        ))
