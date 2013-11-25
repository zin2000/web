package service;

import java.util.List;
import dto.DonjonEquItem;

public interface DonjonItemService {
    List<DonjonEquItem> queryEquItemVersion(int itemVersionId);
}
