package trade.report.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InstructionTypeTest {

  @Test
  @DisplayName("Should return BUY Instruction type enum when the instruction type string provided is B")
  public void testFromTypeWithValidInputAsB() {
    final InstructionType instructionType = InstructionType.fromType("B");
    assertThat(instructionType).isEqualTo(InstructionType.BUY);
  }

  @Test
  @DisplayName("Should return SELL Instruction type enum when the instruction type string provided is S")
  public void testFromTypeWithValidInputAsS() {
    InstructionType instructionType = InstructionType.fromType("S");
    assertThat(instructionType).isEqualTo(InstructionType.SELL);
  }

  @Test
  @DisplayName("Should throw IllegalArgument Exception when Instruction type is Invalid.")
  public void testFromTypeWithInvalidInput() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .as("IllegalArgument Exception is thrown.")
        .isThrownBy(() ->  InstructionType.fromType("INVALID"))
        .withMessageContaining("Instruction type provided is invalid.");
  }

  @Test
  @DisplayName("Should throw IllegalArgument Exception when Instruction type is Null.")
  public void testFromTypeWithNullInput() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .as("IllegalArgument Exception is thrown.")
        .isThrownBy(() ->  InstructionType.fromType(null))
        .withMessageContaining("Instruction type provided is invalid.");
  }

  @Test
  @DisplayName("Should throw IllegalArgument Exception when Instruction type is Empty.")
  public void testFromTypeWithEmptyInput() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .as("IllegalArgument Exception is thrown.")
        .isThrownBy(() ->  InstructionType.fromType(""))
        .withMessageContaining("Instruction type provided is invalid.");
  }

  @Test
  @DisplayName("Should throw IllegalArgument Exception when Instruction type is provided as lower case b and s.")
  void fromTypeCaseSensitivity() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .as("IllegalArgument Exception is thrown.")
        .isThrownBy(() ->  InstructionType.fromType("b"))
        .withMessageContaining("Instruction type provided is invalid.");

    assertThatExceptionOfType(IllegalArgumentException.class)
        .as("IllegalArgument Exception is thrown.")
        .isThrownBy(() ->  InstructionType.fromType("s"))
        .withMessageContaining("Instruction type provided is invalid.");
  }
}