package dao;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;

import dto.DonjonEquItem;
import dto.OldcoinDetail;

public interface OldcoinDao {
    void insert(OldcoinDetail oldcoin);

    OldcoinDetail queryDetailId(int itemDetailId);
    
    void update(DonjonEquItem item);

    void delete(int itemDetailId);
    
    List<OldcoinDetail> findAll();
    
    List<OldcoinDetail> findPage(int page) throws ConfigurationException;
    
    int findCountAll();

}
