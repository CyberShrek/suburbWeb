package org.vniizht.suburbsweb.model.order;

import org.vniizht.suburbsweb.util.ConfigAccess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class FormStatementOrder
{
    public String message = "";
    public Set<String> wrong;

    public Set<String> show = new HashSet<>();
    public Set<String> hide = new HashSet<>();

    public Map<String, Object> setValues = new HashMap<>();
    public Map<String, Map<String, String>> setOptions = new HashMap<>();

    public Map<String, Object> setupServiceBank;

    private Map<String, Object> formValues;

    protected FormStatementOrder(boolean initial, Map<String, Object> formValues) {
        this.formValues = formValues;

        if(initial) {
            setupServiceBank = new HashMap<>();
            putServiceBankSetup("mainSection.carriersField", "carriers.json");
            putServiceBankSetup("mainSection.roadsField", "roads.json");
            wrong = new HashSet<>();
        } else {
            setWrongIfFieldIsEmpty("mainSection.carriersField");
            setWrongIfFieldIsEmpty("mainSection.roadsField");
        }
    }

    protected void setWrongIfFieldIsEmpty(String fieldKey){
        if(((Map<String, String>)formValues.get(fieldKey)).isEmpty()){
            if(wrong == null) wrong = new HashSet<>();
            wrong.add(fieldKey);
        }
    }

    protected void putServiceBankSetup(String fieldKey, String configName){
        setupServiceBank.put(fieldKey, ConfigAccess.getJson("serviceBankSetup/" + configName));
    }

    protected void putOptions(String fieldKey, String configName){
        setOptions.put(fieldKey, ConfigAccess.getMap("options/" + configName));
    }
}