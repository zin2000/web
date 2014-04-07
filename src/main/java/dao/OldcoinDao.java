package dao;

import java.util.List;

import dto.DonjonEquItem;
import dto.OldcoinCharacter;
import dto.OldcoinDetail;
import dto.OldcoinMaster;

public interface OldcoinDao {
    void insert(OldcoinDetail oldcoin);

    OldcoinDetail queryDetailId(int itemDetailId);
    
    void update(DonjonEquItem item);

    void delete(int itemDetailId);
    
    List<OldcoinDetail> findAll();
    
    List<OldcoinDetail> findPage(int page);
    
    int findCountAll();
    
    List<OldcoinMaster> findCoinMaster(String c1, String c2, String c3, String c4);
    
    List<OldcoinCharacter> findCoinCharacter1();
    List<OldcoinCharacter> findCoinCharacter2();
    List<OldcoinCharacter> findCoinCharacter3();
    List<OldcoinCharacter> findCoinCharacter4();
}
