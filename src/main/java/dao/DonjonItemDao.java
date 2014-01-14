package dao;

import java.util.List;
import dto.DonjonEquItem;

public interface DonjonItemDao {
    void insert(DonjonEquItem item);

    DonjonEquItem queryDetailId(int itemDetailId);
    List<DonjonEquItem> queryItemAll();
    List<DonjonEquItem> queryItemVersion(int itemVersionId);
    List<DonjonEquItem> queryItemVersionToVersion(int itemVersionId);
    
    List<DonjonEquItem> queryEquItemAll();
    List<DonjonEquItem> queryEquItemVersion(int itemVersionId);
    List<DonjonEquItem> queryEquItemVersionToVersion(int itemVersionId);
    
    int queryEquItemMaxVersion();
    int queryItemMaxVersion();
    
    void update(DonjonEquItem item);

    void delete(int itemDetailId);
    void deleteEqu(int itemDetailId);
    void deleteImg(int itemImgId);
    
    List<DonjonEquItem> findAll();

}
