package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Wizard {

    @Autowired private Level2Data level2Data;
    @Autowired private Level3Data level3Data;

    public void startTransformation() {
        level3Data.getLastId();
    }
}
