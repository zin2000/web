package service;

import java.util.List;

import dto.OldcoinCharacter;
import dto.OldcoinDetail;
import dto.OldcoinKeyword;
import dto.OldcoinMaster;

public interface OldcoinService {
    List<OldcoinDetail> queryAllDetailData();
    
    List<OldcoinDetail> queryPageDetailData(int page); 
    int queryCountDetailAll();
	List<OldcoinMaster> queryMasterData(String c1, String c2, String c3, String c4);
	
    List<OldcoinCharacter> findCoinCharacter1();
    List<OldcoinCharacter> findCoinCharacter2();
    List<OldcoinCharacter> findCoinCharacter3();
    List<OldcoinCharacter> findCoinCharacter4();
	List<OldcoinKeyword> findCoinKeywordName(String keyName);
	List<OldcoinKeyword> findCoinKeywordNote(String keyName);
}
