package dao;

import java.util.List;
import dto.DonjonEquItem;

public interface DonjonItemDao {
    void insert(DonjonEquItem item);

    DonjonEquItem queryDetailId(int itemDetailId);
    List<DonjonEquItem> queryItemVersion(int itemVersionId);
    List<DonjonEquItem> queryItemVersionToVersion(int itemVersionId);
    
    void update(DonjonEquItem item);

    void delete(int itemDetailId);
    void deleteEqu(int itemDetailId);
    void deleteImg(int itemImgId);
    
    List<DonjonEquItem> findAll();

}
