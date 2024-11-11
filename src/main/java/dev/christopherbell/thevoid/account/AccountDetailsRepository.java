package dev.christopherbell.thevoid.account;

import dev.christopherbell.thevoid.account.model.entity.AccountDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountDetailsRepository extends JpaRepository<AccountDetailsEntity, Long> {

}