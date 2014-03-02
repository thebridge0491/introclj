(in-ns 'org.sandbox.introclj.util.core-test)

(defn in-epsilon
    [tolerance a b]
    (let [delta (Math/abs tolerance)]
        ;(and (<= (- a delta) b) (>= (+ a delta) b))))
        (and (not (< (+ a delta) b)) (not (< (+ b delta) a)))))

(defn genTup2Int
    [gen0 gen1]
    (gen/tuple gen0 gen1))

(defn genTup3Int
    [gen0 gen1 gen2]
    (gen/tuple gen0 gen1 gen2))

(defn genListInts
    [gen0]
    (gen/such-that not-empty (gen/list gen0)))

(def prop-commutativeAdd
    (prop/for-all [x-y (genTup2Int (gen/choose -50 50) (gen/choose -50 50))]
        (let [x (get x-y 0) y (get x-y 1)]
            (= (+ x y) (+ y x)))))

(def prop-associativeAdd
    (prop/for-all [x-y-z (genTup3Int (gen/choose -25 25) (gen/choose -25 25)
            (gen/choose -25 25))]
        (let [x (get x-y-z 0) y (get x-y-z 1) z (get x-y-z 2)]
            (= (+ (+ x y) z) (+ x (+ y z))))))

(def prop-revRev
    (prop/for-all [xs (genListInts (gen/choose -50 50))]
        (= xs (reverse (reverse xs)))))

(def prop-revId
    (prop/for-all [xs (genListInts (gen/choose -50 50))]
        (= xs (reverse xs))))

(def prop-sortRev
    (prop/for-all [xs (genListInts (gen/choose -50 50))]
        (= (sort xs) (sort (reverse xs)))))

(def prop-sortMin
    (prop/for-all [xs (genListInts (gen/choose -50 50))]
        (= (apply min xs) (first (sort xs)))))


;(qc/quick-check 100 prop-commutativeAdd)
;(testqc/defspec commutativeAdd 100 prop-commutativeAdd)
(testqc/defspec commutativeAdd prop-commutativeAdd)

(testqc/defspec associativeAdd prop-associativeAdd)

(testqc/defspec revRev prop-revRev)

(testqc/defspec revId prop-revId)

(testqc/defspec sortRev prop-sortRev)

(testqc/defspec sortMin prop-sortMin)
