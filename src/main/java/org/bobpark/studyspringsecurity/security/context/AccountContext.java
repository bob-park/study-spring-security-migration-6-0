package org.bobpark.studyspringsecurity.security.context;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import org.bobpark.studyspringsecurity.security.model.Account;

public class AccountContext extends User {

    private final Account account;

    public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities) {
        super(account.username(), account.password(), authorities);

        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}
