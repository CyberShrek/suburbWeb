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
public class PrigAggregation {

    @Autowired private Logger logger;
    @Autowired private PrigConversion prigConversion;

    public Set<PrigConversion.Converted> aggregate(List<PrigConversion.Converted> raws) {
        Set<PrigConversion.Converted> converted = new HashSet<>();

        logger.log("Агрегация");
        for (PrigConversion.Converted raw : raws) {
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
