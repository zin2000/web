package service;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import dto.OldcoinDetail;

public interface OldcoinService {
    List<OldcoinDetail> queryAllDetailData();
    
    List<OldcoinDetail> queryPageDetailData(int page) throws ConfigurationException; 
    
    int queryCountDetailAll();
}
