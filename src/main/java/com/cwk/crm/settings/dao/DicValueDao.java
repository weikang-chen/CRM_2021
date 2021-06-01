package com.cwk.crm.settings.dao;

import com.cwk.crm.settings.damain.DicType;
import com.cwk.crm.settings.damain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getDicValueList(String dicType);
}
