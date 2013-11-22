package service;

import java.util.List;

import dao.IShainDao;
import dto.Shain;

public class ShainService implements IShainService {

    private IShainDao shainDao;

    public void registShain(Shain shain) {
        shainDao.insert(shain);
    }

    public Shain getShain(String cdShain) {
        return shainDao.load(cdShain);
    }

    public List<Shain> getAllShain() {
        return shainDao.findAll();
    }

    public void removeShain(String cdShain) {
        shainDao.delete(cdShain);
    }

    public IShainDao getShainDao() {
        return shainDao;
    }

    public void setShainDao(IShainDao shainDao) {
        this.shainDao = shainDao;
    }

}
