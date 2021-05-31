package com.cwk.crm.settings.service;

import com.cwk.crm.settings.damain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {
    Map<String, List<DicValue>> getAll();
}
