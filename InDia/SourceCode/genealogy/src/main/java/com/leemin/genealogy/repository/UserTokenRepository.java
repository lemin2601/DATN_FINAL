package com.leemin.genealogy.repository;

import com.leemin.genealogy.model.UserTokenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository()
public interface UserTokenRepository extends JpaRepository<UserTokenModel, Long> {
    @Override
    UserTokenModel findOne(Long aLong);

    UserTokenModel findBySocialId(Long socialId);
}
