package service;

import java.util.List;

import dto.Shain;

public interface ShainService {
    void registShain(Shain shain);

    Shain getShain(String cdShain);

    List<Shain> getAllShain();

    void removeShain(String cdShain);
}
