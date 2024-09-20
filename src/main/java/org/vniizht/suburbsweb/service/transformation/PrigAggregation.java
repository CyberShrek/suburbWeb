package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level3.co22.*;
import org.vniizht.suburbsweb.service.Logger;

import java.util.*;

@Service
@Scope("singleton")
public class PrigAggregation {

    @Autowired private Logger logger;

    public Set<T1> aggregate(List<T1> t1List) {
        Map<T1.Key, T1> t1Map = new HashMap<>();

        for (T1 t1 : t1List) {
            T1.Key key = t1.getKey();
            if(t1Map.containsKey(key))
                t1Map.get(key).add(t1);
            else
                t1Map.put(key, t1);
        }

        return new HashSet<>(t1Map.values());
    }
//
//    @Getter
//    @Setter
//    @Builder
//    static public class Aggregated {
//        private Set<T1> t1;
//        private T2 t2;
//        private T3 t3;
//        private T4 t4;
//        private T5 t5;
//        private T6 t6;
//    }
}
