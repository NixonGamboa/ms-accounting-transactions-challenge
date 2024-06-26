package com.gamboatech.domain.usecase.acount;

import com.gamboatech.application.adapters.driveradapters.sql.AccountRepositoryAdapter;
import com.gamboatech.application.adapters.driveradapters.sql.MovementRepositoryAdapter;
import com.gamboatech.domain.commons.AccountType;
import com.gamboatech.domain.commons.BusinessException;
import com.gamboatech.domain.commons.ErrorCodes;
import com.gamboatech.domain.model.Account;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class AccountUseCaseImpl implements AccountUseCase{

    private final AccountRepositoryAdapter repositoryAdapter;
    private final MovementRepositoryAdapter movementRepositoryAdapter;

    public AccountUseCaseImpl(AccountRepositoryAdapter repositoryAdapter, MovementRepositoryAdapter movementRepositoryAdapter) {
        this.repositoryAdapter = repositoryAdapter;
        this.movementRepositoryAdapter = movementRepositoryAdapter;
    }

    @Override
    public Account create(Account account) {
        return repositoryAdapter.save(buildNewAccount(account));
    }

    @Override
    public Account edit(Long id, Boolean status, String type)  {
        return repositoryAdapter.save(getUpdatedAccount(id, status, type));
    }

    @Override
    public Long delete(Long id) {
        Account account = repositoryAdapter.findById(id);
        movementRepositoryAdapter.deleteByAccountId(account.getId());
        repositoryAdapter.delete(account.getId());
        return account.getId();
    }

    @Override
    public Long updateBalance(Long accountId, Long movementValue) {
        Account account = repositoryAdapter.findById(accountId);
        Long newBalance = account.getAvailableBalance()+movementValue;
        if(newBalance < 0){
            throw new BusinessException("Saldo no disponible", ErrorCodes.UNAVAILABLE_BALANCE);
        }
        repositoryAdapter.save(account.setAvailableBalance(newBalance));
        return newBalance;
    }

    @Override
    public List<Account> getByClientId(Long clientId) {
        return repositoryAdapter.findByClientId(clientId);
    }

    private Account buildNewAccount(Account account){
        return account.setAvailableBalance(account.getInitialBalance());
    }

    private Account getUpdatedAccount(Long id, Boolean status, String type){
        Account currentAccount = repositoryAdapter.findById(id);

        return currentAccount
                .setStatus(Objects.nonNull(status)? status : currentAccount.getStatus())
                .setType(Objects.nonNull(type)? AccountType.valueOf(type): currentAccount.getType());
    }
}
