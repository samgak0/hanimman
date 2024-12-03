package org.devkirby.hanimman.service;

public interface ShareFavoriteService {
    void create(Integer userId, Integer parentId);
    void delete(Integer parentId, Integer userId);

    int countByParentId(Integer parentId);
}
