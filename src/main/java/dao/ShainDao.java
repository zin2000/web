package dao;

import java.util.List;

import dto.Shain;

public interface ShainDao {
    void insert(Shain shain);

    Shain load(String cdShain);

    void update(Shain shain);

    void delete(String cdShain);

    List<Shain> findAll();
}
