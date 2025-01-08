package trade.report.model;

import static trade.report.util.TradeReportUtility.dateFormatter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;
import trade.report.enums.InstructionType;

/**
 * Class that holds trade Instruction
 */
public final class TradeInstruction {

  private final String entityName;
  private final InstructionType instructionType;
  private final double agreedFx;
  private final LocalDate instructionDate;
  private final String currency;
  private final int numberOfUnits;
  private final double pricePerUnit;
  private final LocalDate actualSettlementDate;

  /**
   * @param entityName               name of the entity
   * @param instructionType          "B" or "S"
   * @param agreedFx                 exchange rate
   * @param currency                 name of the currency
   * @param instructionDate          date when the transaction was instructed
   * @param instructedSettlementDate date when transaction is instructed to be settled
   * @param numberOfUnits            number of units to buy or sell
   * @param pricePerUnit             price of a unit
   */
  public TradeInstruction(final String entityName, final String instructionType,
      final double agreedFx,
      final String currency, final String instructionDate,
      final String instructedSettlementDate,
      final int numberOfUnits, final double pricePerUnit) {
    Objects.requireNonNull(entityName, "Entity name should be provided.");
    Objects.requireNonNull(instructionType, "Instruction type should be provided.");
    Objects.requireNonNull(currency, "Currency should be provided.");
    Objects.requireNonNull(instructedSettlementDate, "Settlement date should be provided.");

    this.entityName = entityName;
    this.instructionType = InstructionType.fromType(instructionType);
    this.agreedFx = agreedFx;
    this.instructionDate = LocalDate.parse(instructionDate, dateFormatter);
    this.numberOfUnits = numberOfUnits;
    this.pricePerUnit = pricePerUnit;
    this.currency = currency;
    this.actualSettlementDate = calculateActualSettlementDate(instructedSettlementDate, currency);
  }

  /**
   * This method calculates the working day when settlement can take place based on the instructed
   * settlement date and currency.
   *
   * @param instructedSettlementDate - date instructed by the client for settlement of trade.
   * @param currency                 - currency of the trade
   * @return actual date when settlement will take place.
   */
  private LocalDate calculateActualSettlementDate(final String instructedSettlementDate,
      final String currency) {
    final LocalDate settlementDate = LocalDate.parse(instructedSettlementDate, dateFormatter);
    final DayOfWeek dayOfWeek = settlementDate.getDayOfWeek();

    final boolean isAEDorSAR = "AED".equalsIgnoreCase(currency) || "SAR".equalsIgnoreCase(currency);

    return switch (dayOfWeek) {
      case FRIDAY -> isAEDorSAR ? settlementDate.plusDays(2) : settlementDate;
      case SATURDAY -> settlementDate.plusDays(isAEDorSAR ? 1 : 2);
      case SUNDAY -> isAEDorSAR ? settlementDate : settlementDate.plusDays(1);
      default -> settlementDate;
    };
  }

  public String getEntityName() {
    return entityName;
  }

  public InstructionType getInstructionType() {
    return instructionType;
  }

  public LocalDate getActualSettlementDate() {
    return actualSettlementDate;
  }

  public LocalDate getInstructionDate() {
    return instructionDate;
  }

  public String getCurrency() {
    return currency;
  }

  public double calculateUSDAmount() {
    return pricePerUnit * numberOfUnits * agreedFx;
  }
}
