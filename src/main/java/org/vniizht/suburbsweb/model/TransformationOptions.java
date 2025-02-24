package org.vniizht.suburbsweb.model;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;

import java.util.Date;

@AllArgsConstructor
public class TransformationOptions {
    @DateTimeFormat(pattern = "yyyyMMdd")
    public Date date;
    public boolean prig;
    public boolean pass;
}
