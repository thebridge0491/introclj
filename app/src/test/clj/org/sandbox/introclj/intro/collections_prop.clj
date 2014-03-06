(in-ns 'org.sandbox.introclj.intro.core-test)

(defn genListInts
    [gen0]
    (gen/such-that not-empty (gen/list gen0)))

(def prop-cons
    (prop/for-all [e-xs (gen/tuple (gen/choose -50 50) (genListInts (gen/choose -50 50)))]
        (let [e (get e-xs 0) xs (get e-xs 1)]
            (= e (first (cons e xs))))))

(def prop-null
    (prop/for-all [xs (genListInts (gen/choose -50 50))]
        (= (= 0 (count xs)) (empty? xs))))

(def prop-equal
    (prop/for-all [xs (genListInts (gen/choose -50 50))]
        (let [ys xs]
            (= xs ys))))

(def prop-append
    (prop/for-all [xs-ys (gen/tuple (genListInts (gen/choose -50 50))
            (genListInts (gen/choose -50 50)))]
        (let [xs (get xs-ys 0) ys (get xs-ys 1)]
            (= (concat xs ys) (reduce (fn [a e] (cons e a)) ys (reverse xs))))))

(def prop-reverse
    (prop/for-all [xs (genListInts (gen/choose -50 50))]
        (= xs (reverse (reverse xs)))))

(def prop-filter
    (prop/for-all [xs (genListInts (gen/choose -50 50))]
        (let [pred1 (fn [e] (= 0 (mod e 2)))]
            (= (every? pred1 (filter pred1 xs))))))

(def prop-map
    (prop/for-all [xs (genListInts (gen/choose -50 50))]
        (let [proc1 (fn [e] (+ e 2)) ys (map proc1 xs)]
            (= ys (map proc1 xs)))))

(defn is-ordered
    [xs & {:keys [cmpop] :or {cmpop <=}}]
    (cond (empty? xs) true
        (empty? (rest xs)) true
        :else (and (cmpop (first xs) (first (rest xs))) (is-ordered (rest xs) :cmpop cmpop))))

(def prop-isordered
    (prop/for-all [xs (genListInts (gen/choose -50 50))]
        (is-ordered (sort < xs))
        (is-ordered (sort > xs) :cmpop >=)
        ))

;(qc/quick-check 100 prop-cons)
;(testqc/defspec cons-prop 100 prop-cons)
(testqc/defspec cons-prop prop-cons)

(testqc/defspec null-prop prop-null)

(testqc/defspec equal-prop prop-equal)

(testqc/defspec append-prop prop-append)

(testqc/defspec reverse-prop prop-reverse)

(testqc/defspec filter-prop prop-filter)

(testqc/defspec map-prop prop-map)

(testqc/defspec isordered-prop prop-isordered)
