package service;

import java.util.List;
import dto.DonjonEquItem;

public interface DonjonItemService {
    List<DonjonEquItem> queryEquItemVersion(int itemVersionId);

	List<DonjonEquItem> queryEquItemVersionTo(int itemVersionId);

	List<DonjonEquItem> queryEquItemAll();
	
    List<DonjonEquItem> queryItemVersion(int itemVersionId);

	List<DonjonEquItem> queryItemVersionTo(int itemVersionId);

	List<DonjonEquItem> queryItemAll();
}
