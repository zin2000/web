package service;

import dao.IBumonDao;
import dao.IShainDao;
import dto.Bumon;
import dto.Shain;

public class SetupService implements ISetupService {

    private IBumonDao bumonDao;

    private IShainDao shainDao;

    public void setup(Bumon bumon, Shain shain) {
        // •”–å‚Ì“o˜^
        bumonDao.insert(bumon);

        // ŽÐˆõ‚Ì“o˜^
        shainDao.insert(shain);
    }

    public IBumonDao getBumonDao() {
        return bumonDao;
    }

    public void setBumonDao(IBumonDao bumonDao) {
        this.bumonDao = bumonDao;
    }

    public IShainDao getShainDao() {
        return shainDao;
    }

    public void setShainDao(IShainDao shainDao) {
        this.shainDao = shainDao;
    }

}
