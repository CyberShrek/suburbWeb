package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.service.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Scope("singleton")
public class Aggregation {

    @Autowired private Logger logger;
    @Autowired private Conversion conversion;

    public Set<Conversion.Converted> aggregate(List<Conversion.Converted> raws) {
        Set<Conversion.Converted> converted = new HashSet<>();

        logger.log("Агрегация");
        for (Conversion.Converted raw : raws) {
            converted.add(raw);
        }

        return converted;
    }

//    @Getter
//    @Setter
//    @Builder
//    static public class Aggregated{
//        private Set<Transformation.Transformed> set = new HashSet<>();
//    }
}
