package com.starbank.StarProductAdvisor;

import com.starbank.StarProductAdvisor.dto.RecommendationDTO;
import com.starbank.StarProductAdvisor.repository.RecommendationsRepository;
import com.starbank.StarProductAdvisor.rules.TopSavingRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RulesTest {


    @Mock
    private RecommendationsRepository repository;

    @InjectMocks
    private TopSavingRule topSavingRule;

    @Test
    void apply_WhenAllConditionsMetWithDebitDeposits_ShouldReturnRecommendation() {

        UUID userId = UUID.randomUUID();
        when(repository.userHasProductType(userId, "DEBIT")).thenReturn(true);
        when(repository.sumDepositsByUserAndProductType(userId, "DEBIT")).thenReturn(60000.0);
        when(repository.sumDepositsByUserAndProductType(userId, "SAVING")).thenReturn(0.0);
        when(repository.sumWithdrawalsByUserAndProductType(userId, "DEBIT")).thenReturn(40000.0);


        Optional<RecommendationDTO> result = topSavingRule.apply(userId);


        assertTrue(result.isPresent());
        RecommendationDTO recommendation = result.get();
        assertEquals("59efc529-2fff-41af-baff-90ccd7402925", recommendation.getId());
        assertEquals("Top Saving", recommendation.getName());
        assertEquals("Откройте свою собственную «Копилку» с нашим банком!", recommendation.getText());
    }

    @Test
    void apply_WhenAllConditionsMetWithSavingDeposits_ShouldReturnRecommendation() {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(repository.userHasProductType(userId, "DEBIT")).thenReturn(true);
        when(repository.sumDepositsByUserAndProductType(userId, "DEBIT")).thenReturn(41000.0);
        when(repository.sumDepositsByUserAndProductType(userId, "SAVING")).thenReturn(60000.0);
        when(repository.sumWithdrawalsByUserAndProductType(userId, "DEBIT")).thenReturn(40000.0);


        Optional<RecommendationDTO> result = topSavingRule.apply(userId);


        assertTrue(result.isPresent());


        verify(repository).userHasProductType(userId, "DEBIT");
        verify(repository).sumDepositsByUserAndProductType(userId, "DEBIT");
        verify(repository).sumDepositsByUserAndProductType(userId, "SAVING");
        verify(repository).sumWithdrawalsByUserAndProductType(userId, "DEBIT");
    }

    @Test
    void apply_WhenNoDebitAccount_ShouldReturnEmpty() {

        UUID userId = UUID.randomUUID();
        when(repository.userHasProductType(userId, "DEBIT")).thenReturn(false);
        when(repository.sumDepositsByUserAndProductType(userId, "DEBIT")).thenReturn(60000.0);
        when(repository.sumDepositsByUserAndProductType(userId, "SAVING")).thenReturn(60000.0);
        when(repository.sumWithdrawalsByUserAndProductType(userId, "DEBIT")).thenReturn(40000.0);


        Optional<RecommendationDTO> result = topSavingRule.apply(userId);


        assertFalse(result.isPresent());
    }

    @Test
    void apply_WhenDepositsLessThan50000_ShouldReturnEmpty() {

        UUID userId = UUID.randomUUID();
        when(repository.userHasProductType(userId, "DEBIT")).thenReturn(true);
        when(repository.sumDepositsByUserAndProductType(userId, "DEBIT")).thenReturn(49999.0);
        when(repository.sumDepositsByUserAndProductType(userId, "SAVING")).thenReturn(49999.0);
        when(repository.sumWithdrawalsByUserAndProductType(userId, "DEBIT")).thenReturn(40000.0);


        Optional<RecommendationDTO> result = topSavingRule.apply(userId);


        assertFalse(result.isPresent());
    }

    @Test
    void apply_WhenDepositsExactly50000_ShouldReturnRecommendation() {

        UUID userId = UUID.randomUUID();
        when(repository.userHasProductType(userId, "DEBIT")).thenReturn(true);
        when(repository.sumDepositsByUserAndProductType(userId, "DEBIT")).thenReturn(50000.0);
        when(repository.sumDepositsByUserAndProductType(userId, "SAVING")).thenReturn(0.0);
        when(repository.sumWithdrawalsByUserAndProductType(userId, "DEBIT")).thenReturn(40000.0);


        Optional<RecommendationDTO> result = topSavingRule.apply(userId);


        assertTrue(result.isPresent()); // >= 50000
    }

    @Test
    void apply_WhenDepositsNotGreaterThanWithdrawals_ShouldReturnEmpty() {

        UUID userId = UUID.randomUUID();
        when(repository.userHasProductType(userId, "DEBIT")).thenReturn(true);
        when(repository.sumDepositsByUserAndProductType(userId, "DEBIT")).thenReturn(60000.0);
        when(repository.sumDepositsByUserAndProductType(userId, "SAVING")).thenReturn(0.0);
        when(repository.sumWithdrawalsByUserAndProductType(userId, "DEBIT")).thenReturn(60000.0);


        Optional<RecommendationDTO> result = topSavingRule.apply(userId);


        assertFalse(result.isPresent()); // deposits must be > withdrawals
    }
}
