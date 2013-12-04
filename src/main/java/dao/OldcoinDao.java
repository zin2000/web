package dao;

import java.util.List;
import dto.DonjonEquItem;
import dto.OldcoinDetail;

public interface OldcoinDao {
    void insert(OldcoinDetail oldcoin);

    OldcoinDetail queryDetailId(int itemDetailId);
    
    void update(DonjonEquItem item);

    void delete(int itemDetailId);
    
    List<OldcoinDetail> findAll();
    
    List<OldcoinDetail> findPage(int page);
    
    int findCountAll();

}
