(ns pallet.thread-expr-test
  (:use
   pallet.thread-expr
   clojure.test))

(deftest for->-test
  (is (= 7  (-> 1 (for-> [x [1 2 3]] (+ x)))))
  (is (= 13  (-> 1 (for-> [x [1 2 3]] (+ x) (+ x)))))
  (is (= 1  (-> 1 (for-> [x []] (+ x)))))
  (is (= 55  (-> 1 (for-> [x [1 2 3]
                           y [2 3 4]
                           :let [z (dec x)]] (+ x y z))))))

(deftest for->>-test
  (is (= 7  (->> 1 (for->> [x [1 2 3]] (+ x)))))
  (is (= 13  (->> 1 (for->> [x [1 2 3]] (+ x) (+ x)))))
  (is (= 1  (->> 1 (for->> [x []] (+ x)))))
  (is (= 55  (->> 1 (for->> [x [1 2 3]
                             y [2 3 4]
                             :let [z (dec x)]] (+ x y z))))))

(deftest when->test
  (is (= 2 (-> 1 (when-> true (+ 1)))))
  (is (= 1 (-> 1 (when-> false (+ 1))))))

(deftest when->>test
  (is (= 2 (->> 1 (when->> true (+ 1)))))
  (is (= 1 (->> 1 (when->> false (+ 1))))))

(deftest when-not->test
  (is (= 1 (-> 1 (when-not-> true (+ 1)))))
  (is (= 2 (-> 1 (when-not-> false (+ 1))))))

(deftest when-not->>test
  (is (= 1 (->> 1 (when-not->> true (+ 1)))))
  (is (= 2 (->> 1 (when-not->> false (+ 1))))))

(deftest when-let->test
  (is (= 2) (-> 1 (when-let-> [a 1] (+ a))))
  (is (= 1) (-> 1 (when-let-> [a nil] (+ a)))))

(deftest when-let->>test
  (is (= 2) (->> 1 (when-let->> [a 1] (+ a))))
  (is (= 1) (->> 1 (when-let->> [a nil] (+ a)))))

(deftest let->test
  (is (= 2) (-> 1 (let-> [a 1] (+ a)))))

(deftest let->>test
  (is (= 2) (->> 1 (let->> [a 1] (+ a)))))

(def ^{:dynamic true} *a* 0)
(deftest binding->test
  (is (= 2) (-> 1 (binding-> [*a* 1] (+ *a*)))))

(deftest binding->>test
  (is (= 2) (->> 1 (binding->> [*a* 1] (+ *a*)))))

(deftest if->test
  (is (= 2 (-> 1 (if-> true  (+ 1) (+ 2)))))
  (is (= 3 (-> 1 (if-> false (+ 1) (+ 2)))))
  (is (= 1 (-> 1 (if-> false (+ 1))))))

(deftest if->>test
  (is (= 2 (->> 1 (if->> true  (+ 1) (+ 2)))))
  (is (= 3 (->> 1 (if->> false (+ 1) (+ 2)))))
  (is (= 1 (->> 1 (if->> false (+ 1))))))

(deftest if-not->test
  (is (= 3 (-> 1 (if-not-> true  (+ 1) (+ 2)))))
  (is (= 2 (-> 1 (if-not-> false (+ 1) (+ 2))))))

(deftest if-not->>test
  (is (= 3 (->> 1 (if-not->> true  (+ 1) (+ 2)))))
  (is (= 2 (->> 1 (if-not->> false (+ 1) (+ 2))))))

(deftest arg->test
  (is (= 6 (-> 2 (arg-> [x] (* (inc x)))))))

(deftest arg->>test
  (is (= 6 (->> 2 (arg->> [x] (* (inc x)))))))

(deftest let-with-arg->test
  (is (= 3 (-> 1 (let-with-arg-> arg [a 1] (+ a arg))))))

(deftest let-with-arg->>test
  (is (= 3 (->> 1 (let-with-arg->> arg [a 1] (+ a arg))))))

(deftest apply->test
  (is (= 7 (-> 1 (apply-> + [1 2 3])))))

(deftest apply->>test
  (is (= 7 (->> 1 (apply->> + [1 2 3])))))

(deftest apply-map->test
  (is (= {:a 1 :b 2} (-> :a (apply-map-> hash-map 1 {:b 2})))))

(deftest apply-map->>test
  (is (= {:a 1 :b 2} (->> :a (apply-map->> hash-map 1 {:b 2})))))

(deftest -->test
  (is (= 12 (--> 5
                 (let [y 1]
                   (for [x (range 3)]
                     (+ x y)))
                 (+ 1)))
      (= 11 (--> 5
                 (expose-request-as [x] (+ x))
                 (+ 1)))))

(deftest -->>test
  (is (= 12 (-->> 5
                  (let [y 1]
                    (for [x (range 3)]
                      (+ x y)))
                  (+ 1)))
      (= 11 (-->> 5
                  (expose-request-as [x] (+ x))
                  (+ 1)))))
