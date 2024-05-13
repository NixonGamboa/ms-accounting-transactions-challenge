package com.gamboatech.domain.usecase.movement;

import com.gamboatech.application.adapters.driveradapters.sql.MovementRepositoryAdapter;
import com.gamboatech.domain.model.Movement;
import com.gamboatech.domain.usecase.acount.AccountUseCase;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MoventUseCaseimpl implements MovementUseCase{

    private final MovementRepositoryAdapter repositoryAdapter;

    private final AccountUseCase accountUseCase;

    public MoventUseCaseimpl(MovementRepositoryAdapter repositoryAdapter, AccountUseCase accountUseCase) {
        this.repositoryAdapter = repositoryAdapter;
        this.accountUseCase = accountUseCase;
    }

    @Override
    public Movement register(Movement movement)  {
        Long newBalance = accountUseCase.updateBalance(movement.getAccountId(), movement.getValue());
        Movement movementUpdated= movement.setNewBalance(newBalance);
        return repositoryAdapter.save(movementUpdated);
    }

    @Override
    public Movement cancel(Long id) {
        Movement movement = buildCancelationMovement(repositoryAdapter.findById(id));
        return this.register(movement);
    }

    private Movement buildCancelationMovement(Movement toCancel){
        return new Movement()
                .setAccountId(toCancel.getAccountId())
                .setDate(LocalDateTime.now())
                .setType("Cancellation")
                .setValue(toggleValueSign(toCancel.getValue()));
    }

    private Long toggleValueSign(Long value){
        return value*(-1);
    }

}