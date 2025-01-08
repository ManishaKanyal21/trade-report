package trade.report.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static trade.report.util.TradeReportUtility.dateFormatter;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import trade.report.enums.InstructionType;

class TradeInstructionTest {

  private final String entityName = "Entity1";
  private final String instructionType = "B";
  private final double agreedFx = 1.5;
  private final String currency = "USD";
  private final String instructionDate = "01 Jan 2016";
  private final String instructedSettlementDate = "01 Jan 2016";
  private final int numberOfUnits = 100;
  private final double pricePerUnit = 150.0;

  @Test
  @DisplayName("Should initialize trade instruction object with correct values.")
  public void testTradeInstructionInitialization() {
    final TradeInstruction tradeInstruction = new TradeInstruction(entityName, instructionType, agreedFx, currency,
        instructionDate, instructedSettlementDate, numberOfUnits, pricePerUnit);
    assertThat(tradeInstruction.getEntityName()).isEqualTo(entityName);
    assertThat(tradeInstruction.getInstructionType()).isEqualTo(InstructionType.BUY);
    assertThat(tradeInstruction.getInstructionDate()).isEqualTo(
        LocalDate.parse(instructionDate, dateFormatter));
    assertThat(tradeInstruction.getActualSettlementDate()).isEqualTo(
        LocalDate.parse(instructedSettlementDate, dateFormatter));
    assertThat(tradeInstruction.getCurrency()).isEqualTo(currency);
  }


  @Test
  @DisplayName("Should calculate USD Amount for the trade instruction.")
  void testCalculateUSDAmount() {
    final TradeInstruction tradeInstruction = new TradeInstruction(entityName, instructionType, agreedFx, currency,
        instructionDate, instructedSettlementDate, numberOfUnits, pricePerUnit);
    final double expectedUSDAmount = numberOfUnits * pricePerUnit * agreedFx;
    assertThat(tradeInstruction.calculateUSDAmount()).isEqualTo(expectedUSDAmount);
  }

  @Test
  @DisplayName("Should throw Null Pointer Exception when Entity name provided is null.")
  public void testNullEntityName() {
    assertThatExceptionOfType(NullPointerException.class)
        .as("Null pointer Exception is thrown.")
        .isThrownBy(
            () -> new TradeInstruction(null, instructionType, agreedFx, currency, instructionDate,
                instructedSettlementDate, numberOfUnits, pricePerUnit))
        .withMessageContaining("Entity name should be provided.");
  }

  @Test
  @DisplayName("Should throw Null Pointer Exception when Instruction type provided is null.")
  public void testNullInstructionType() {
    assertThatExceptionOfType(NullPointerException.class)
        .as("Null pointer Exception is thrown.")
        .isThrownBy(
            () -> new TradeInstruction(entityName, null, agreedFx, currency, instructionDate,
                instructedSettlementDate, numberOfUnits, pricePerUnit))
        .withMessageContaining("Instruction type should be provided.");
  }

  @Test
  @DisplayName("Should throw Null Pointer Exception when currency provided is null.")
  public void testNullCurrency() {
    assertThatExceptionOfType(NullPointerException.class)
        .as("Null pointer Exception is thrown.")
        .isThrownBy(
            () -> new TradeInstruction(entityName, instructionType, agreedFx, null, instructionDate,
                instructedSettlementDate, numberOfUnits, pricePerUnit))
        .withMessageContaining("Currency should be provided.");
  }

  @Test
  @DisplayName("Should throw Null Pointer Exception when Settlement Date provided is null.")
  public void testNullSettlementDate() {
    assertThatExceptionOfType(NullPointerException.class)
        .as("Null pointer Exception is thrown.")
        .isThrownBy(
            () -> new TradeInstruction(entityName, instructionType, agreedFx, currency, instructionDate,
                null, numberOfUnits, pricePerUnit))
        .withMessageContaining("Settlement date should be provided.");
  }

  @Test
  @DisplayName("Should throw IllegalArgument Exception when Instruction type is invalid")
  public void testInvalidInstructionType() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .as("IllegalArgument Exception is thrown.")
        .isThrownBy(
            () -> new TradeInstruction(entityName, "T", agreedFx, currency, instructionDate,
                instructedSettlementDate, numberOfUnits, pricePerUnit))
        .withMessageContaining("Instruction type provided is invalid.");
  }

  @Test
  @DisplayName("Should calculate Actual Settlement Date as Sunday(working day) when currency is AED and day is Friday")
  public void testCalculateActualSettlementDateForAEDOnFriday() {
    final String instructedSettlementDate = "06 Jan 2023";
    final TradeInstruction tradeInstruction = new TradeInstruction(entityName, instructionType,
        agreedFx, "AED", instructionDate, instructedSettlementDate, numberOfUnits, pricePerUnit);

    final LocalDate expectedSettlementDate = LocalDate.parse(instructedSettlementDate,
        dateFormatter).plusDays(2);
    assertThat(tradeInstruction.getActualSettlementDate()).isEqualTo(expectedSettlementDate);
  }

  @Test
  @DisplayName("Should calculate Actual Settlement Date as Monday when currency is USD and day is Saturday.")
  public void testCalculateActualSettlementDateForUSDOnSaturday() {
    final String instructedSettlementDate = "07 Jan 2023";
    final TradeInstruction tradeInstruction = new TradeInstruction(entityName, instructionType,
        agreedFx,
        "USD", instructionDate, instructedSettlementDate, numberOfUnits, pricePerUnit);

    final LocalDate expectedSettlementDate = LocalDate.parse(instructedSettlementDate,
            dateFormatter)
        .plusDays(2);
    assertThat(tradeInstruction.getActualSettlementDate()).isEqualTo(expectedSettlementDate);
  }

  @Test
  @DisplayName("Should calculate Actual Settlement Date as Sunday(working day) when currency is SAR and day is Friday.")
  public void testCalculateActualSettlementDateForSAROnFriday() {
    final String instructedSettlementDate = "06 Jan 2023";
    final TradeInstruction tradeInstruction = new TradeInstruction(entityName, instructionType,
        agreedFx, "SAR", instructionDate, instructedSettlementDate, numberOfUnits, pricePerUnit);

    final LocalDate expectedSettlementDate = LocalDate.parse(instructedSettlementDate,
        dateFormatter).plusDays(2);
    assertThat(tradeInstruction.getActualSettlementDate()).isEqualTo(expectedSettlementDate);
  }

  @Test
  @DisplayName("Should keep Actual Settlement Date as is when currency is SGP and day is Saturday.")
  public void testCalculateActualSettlementDateForSGPOnSaturday() {
    final String instructedSettlementDate = "09 Jan 2023";
    final TradeInstruction tradeInstruction = new TradeInstruction(entityName, instructionType,
        agreedFx,
        "SGP", instructionDate, instructedSettlementDate, numberOfUnits, pricePerUnit);

    final LocalDate expectedSettlementDate = LocalDate.parse(instructedSettlementDate,
        dateFormatter);
    assertThat(tradeInstruction.getActualSettlementDate()).isEqualTo(expectedSettlementDate);
  }
}