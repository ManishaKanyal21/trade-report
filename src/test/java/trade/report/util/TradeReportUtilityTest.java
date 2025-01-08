package trade.report.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.entry;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import trade.report.enums.InstructionType;
import trade.report.model.TradeInstruction;

class TradeReportUtilityTest {

  @Test
  @DisplayName("Should return empty USD settlements map when no trade instructions are given to calculate USD settlements.")
  public void testCalculateUSDSettlements_withNoTradeInstructions() {
    final Map<LocalDate, Double> outgoingSettlements = TradeReportUtility.calculateUSDSettlements(
        Collections.emptyList(), InstructionType.BUY);
    assertThat(outgoingSettlements)
        .as("Empty map is returned as result.")
        .isEmpty();
  }

  @Test
  @DisplayName("Should throw Null pointer exception when trade instructions is null to calculate USD settlements.")
  public void testCalculateUSDSettlements_withNullTradeInstructions() {
    assertThatExceptionOfType(NullPointerException.class)
        .as("Null pointer Exception is thrown.")
        .isThrownBy(() -> TradeReportUtility.calculateUSDSettlements(
            null, InstructionType.BUY))
        .withMessageContaining("Trade instructions should not be null.");
  }

  @Test
  @DisplayName("Should return expected values for Outgoing USD settlements for expected dates successfully.")
  public void testCalculateUSDSettlements_forOutgoingInstructions() {
    final List<TradeInstruction> tradeInstructions = TradeReportUtility.getTradeInstructions();
    final Map<LocalDate, Double> outgoingSettlements = TradeReportUtility.calculateUSDSettlements(
        tradeInstructions, InstructionType.BUY);

    assertThat(outgoingSettlements)
        .as("Expected Outgoing USD Settlements are present for different dates.")
        .hasSize(3)
        .contains(entry(LocalDate.of(2016, 1, 5), 25125.00),
            entry(LocalDate.of(2016, 1, 4), 45225.00), entry(LocalDate.of(2016, 1, 1), 5025.00));
  }

  @Test
  @DisplayName("Should return expected values for Incoming USD settlements for expected dates successfully.")
  public void testCalculateUSDSettlements_forIncomingInstructions() {
    final List<TradeInstruction> tradeInstructions = TradeReportUtility.getTradeInstructions();
    final Map<LocalDate, Double> outgoingSettlements = TradeReportUtility.calculateUSDSettlements(
        tradeInstructions, InstructionType.SELL);

    assertThat(outgoingSettlements)
        .as("Expected Incoming USD Settlements are present for different dates.")
        .hasSize(3)
        .contains(entry(LocalDate.of(2016, 1, 11), 16555.00),
            entry(LocalDate.of(2016, 1, 10), 29799.00), entry(LocalDate.of(2016, 1, 7), 3311.00));
  }

  @Test
  @DisplayName("Should return empty USD settlements map when no trade instructions are given to calculate rankings.")
  public void testCalculateRankings_withNoTradeInstructions() {
    final Map<LocalDate, Double> outgoingSettlements = TradeReportUtility.calculateUSDSettlements(
        Collections.emptyList(), InstructionType.BUY);
    assertThat(outgoingSettlements)
        .as("Empty map is returned as result.")
        .isEmpty();
  }

  @Test
  @DisplayName("Should throw Null pointer exception when trade instructions is null to calculate rankings.")
  public void testCalculateRankings_withNullTradeInstructions() {
    assertThatExceptionOfType(NullPointerException.class)
        .as("Null pointer Exception is thrown.")
        .isThrownBy(() -> TradeReportUtility.calculateUSDSettlements(
            null, InstructionType.BUY))
        .withMessageContaining("Trade instructions should not be null.");
  }

  @Test
  @DisplayName("Should return ranking of entities based on Incoming USD amount.")
  public void testCalculateRankings_forIncomingInstructions() {
    final List<TradeInstruction> tradeInstructions = TradeReportUtility.getTradeInstructions();
    final List<Map.Entry<String, Double>> incomingRankings = TradeReportUtility.calculateRankings(
        tradeInstructions, InstructionType.SELL);

    assertThat(incomingRankings)
        .as("Expected rankings are present based on Incoming Settlements.")
        .hasSize(5)
        .containsExactly(entry("doo", 16555.00),
            entry("moo", 13244.00),
            entry("zoo", 9933.00),
            entry("foo", 6622.00),
            entry("bar", 3311.00));
  }

  @Test
  @DisplayName("Should return ranking of entities based on Outgoing USD amount.")
  public void testCalculateRankings_forOutgoingInstructions() {
    final List<TradeInstruction> tradeInstructions = TradeReportUtility.getTradeInstructions();
    final List<Map.Entry<String, Double>> incomingRankings = TradeReportUtility.calculateRankings(
        tradeInstructions, InstructionType.BUY);

    assertThat(incomingRankings)
        .as("Expected rankings are present based on Outgoing Settlements.")
        .hasSize(5)
        .containsExactly(entry("doo", 25125.00),
            entry("moo", 20100.00),
            entry("bar", 15075.00),
            entry("foo", 10050.00),
            entry("zoo", 5025.00));
  }

  @Test
  @DisplayName("Should return list of trade instructions")
  public void testGetTradeInstructions() {
    final List<TradeInstruction> tradeInstructions = TradeReportUtility.getTradeInstructions();
    assertThat(tradeInstructions)
        .as("Trade Instructions is not empty.")
        .hasSize(10);
  }
}