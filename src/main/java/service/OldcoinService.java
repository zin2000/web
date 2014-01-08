package service;

import java.util.List;

import dto.OldcoinDetail;

public interface OldcoinService {
    List<OldcoinDetail> queryAllDetailData();
    
    List<OldcoinDetail> queryPageDetailData(int page); 
    
    int queryCountDetailAll();
}
